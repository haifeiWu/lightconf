package com.lightconf.admin.web.netty;

import com.alibaba.fastjson.JSON;
import com.lightconf.admin.model.dataobj.App;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.service.ConfService;
import com.lightconf.common.model.*;
import com.lightconf.common.util.CommonConstants;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.NettyChannelMap;
import com.lightconf.common.util.ThreadPoolUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhf
 * @date 2018/02/09
 */
@Slf4j
@Component
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private final AppService appService;

    private final ConfService confService;

    private final ServerAuthConfig authConfig;

    @Autowired
    public ServerHandler(AppService appService, ConfService confService, ServerAuthConfig authConfig) {
        this.appService = appService;
        this.confService = confService;
        this.authConfig = authConfig;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接断开，应该将应用的连接状态重新置为未连接
        final String appUUid = NettyChannelMap.getClientId((SocketChannel) ctx.channel());
        log.error(">>>>>> channel is in channelInactive,the appUUid is : {}", appUUid);

        // 耗时操作另起一个线程来做，提交线程池
        ThreadPoolUtils.getInstance().getThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isNotBlank(appUUid)) {
                    App app = appService.getAppByUUID(appUUid);
                    if (null != app) {
                        app.setIsConnected(false);
                        appService.updateApp(app);
                        log.error(">>>>>> update app connection status : {}", JSON.toJSONString(app));
                    }
                }
            }
        });

        NettyChannelMap.remove((SocketChannel) ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                BaseMsg baseMsg) throws Exception {
        switch (baseMsg.getType()) {

            case LOGIN:
                // 客户端登录
                lightConfClientLogin(channelHandlerContext, baseMsg);
                break;

            case PUSH_CONF:
                log.info("do nothing");
                break;

            case UPLOAD_CONF: {
                PushMsg pushMsg = (PushMsg) baseMsg;
                final String appUUID = pushMsg.getClientId();
                List<Config> configList = pushMsg.getConfigList();
                final List<Conf> confList = new ArrayList<>();
                if (configList != null && configList.size() > 0) {
                    for (Config config : configList) {
                        Conf conf = new Conf();
                        conf.setConfKey(config.getKey());
                        conf.setConfValue(config.getValue());
                        confList.add(conf);
                    }
                }

                // 耗时操作另起一个线程来做，提交线程池
                ThreadPoolUtils.getInstance().getThreadPool().submit(new Runnable() {
                    @Override
                    public void run() {
                        App app = appService.getAppByUUID(appUUID);
                        if (null != app) {
                            LightConfResult result = null;
                            for (Conf conf : confList) {
                                result = confService.add(conf, String.valueOf(app.getId()));
                            }
                            // 配置已存在视为上传成功（幂等上传）
                            if (result != null
                                    && (result.getCode() == Messages.SUCCESS_CODE
                                    || result.getCode() == Messages.CONF_ALREADY_EXISTS_CODE)) {
                                app.setIsPushConf(true);
                                appService.updateApp(app);
                            }
                        } else {
                            log.error(">>>>>> upload conf failed, app not found, uuid : {}", appUUID);
                        }
                    }
                });
            }
            break;
            case PING: {
                PingMsg pingMsg = (PingMsg) baseMsg;
                PingMsg replyPing = new PingMsg();
                Channel channel = NettyChannelMap.get(pingMsg.getClientId());
                if (channel != null) {
                    channel.writeAndFlush(replyPing);
                }
            }
            break;
            case ASK: {
                //收到客户端的请求
                AskMsg askMsg = (AskMsg) baseMsg;
                if ("authToken".equals(askMsg.getParams().getAuth())) {
                    ReplyServerBody replyBody = new ReplyServerBody("server info $$$$ !!!");
                    ReplyMsg replyMsg = new ReplyMsg();
                    replyMsg.setBody(replyBody);
                    NettyChannelMap.get(askMsg.getClientId()).writeAndFlush(replyMsg);
                }
            }
            break;
            case REPLY: {
                //收到客户端回复
                ReplyMsg replyMsg = (ReplyMsg) baseMsg;
                ReplyClientBody clientBody = (ReplyClientBody) replyMsg.getBody();
                log.info("receive util msg: " + clientBody.getClientInfo());
            }
            break;
            default: {
                log.warn(">>>>>> unknown msg type : {}", baseMsg.getType());
                NettyChannelMap.remove((SocketChannel) channelHandlerContext.channel());
                channelHandlerContext.disconnect();
            }
            break;
        }
    }

    /**
     * 客户端登录.
     *
     * @param channelHandlerContext
     * @param baseMsg
     */
    private boolean lightConfClientLogin(ChannelHandlerContext channelHandlerContext,
                                         BaseMsg baseMsg) {
        LoginMsg loginMsg = (LoginMsg) baseMsg;
        String appUUid = loginMsg.getClientId();
        if (StringUtils.isBlank(appUUid)) {
            log.error(">>>>>> 应用的uuid配置有误，请检查配置！");
            channelHandlerContext.close();
            return false;
        }

        // 鉴权：校验应用共享密钥（服务端配置为空时跳过校验，保持向后兼容）
        String secret = authConfig.getSecret();
        if (StringUtils.isNotBlank(secret) && !StringUtils.equals(loginMsg.getSecret(), secret)) {
            log.error(">>>>>> 应用 {} 鉴权失败，secret 不匹配，拒绝连接", appUUid);
            channelHandlerContext.close();
            return false;
        }

        AppWithBLOBs app = appService.getAppByUUID(appUUid);
        if (null != app) {
            app.setIsConnected(true);
            appService.updateAppWithBLOBs(app);
            // 登录成功,把channel存到服务端的map中.
            SocketChannel socketChannel = (SocketChannel) channelHandlerContext.channel();
            NettyChannelMap.add(loginMsg.getClientId(), socketChannel);
            log.info("client {} 登录成功", loginMsg.getClientId());

            if (app.getIsPushConf()) {
                // 配置信息已经上报
                if (app.getIsChange()) {
                    // 下发该应用对应的配置信息.
                    LightConfResult result = appService.getAppConf(String.valueOf(app.getId()));
                    List<Conf> confList = (List<Conf>) result.getContent();
                    if (confList != null && confList.size() > 0) {
                        List<Config> configList = new ArrayList<>();
                        for (Conf conf : confList) {
                            Config config = new Config();
                            config.setKey(conf.getConfKey());
                            config.setValue(conf.getConfValue());
                            configList.add(config);
                        }

                        PushMsg pushMsg = new PushMsg();
                        pushMsg.setConfType(CommonConstants.CONF_TYPE_SEND_OUT);
                        pushMsg.setConfigList(configList);
                        pushMsg.setType(MsgType.SEND_OUT);
                        socketChannel.writeAndFlush(pushMsg);
                        log.info(">>>>>> send out config success!");
                    }
                }
            } else {
                // 通知客户端上报配置信息
                PushMsg pushMsg = new PushMsg();
                pushMsg.setType(MsgType.UPLOAD_CONF);
                socketChannel.writeAndFlush(pushMsg);
                log.info(">>>>>> server say ...... client upload conf");
            }
            return true;
        } else {
            log.error(">>>>>> 应用 {} 不存在，拒绝连接", appUUid);
            channelHandlerContext.close();
            return false;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error(">>>>>> channel is exception over. channel={}", ctx.channel(), cause);
        NettyChannelMap.remove((SocketChannel) ctx.channel());
        ctx.close();
    }
}
