package com.lightconf.core.netty;

import com.lightconf.common.model.*;
import com.lightconf.common.util.CommonConstants;
import com.lightconf.core.core.LightConfLocalCacheConf;
import com.lightconf.core.core.LightConfPropConf;
import com.lightconf.core.env.Environment;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuhf
 */
public class ClientHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg = new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 连接建立处理逻辑
        String appId = LightConfPropConf.get(Environment.APPLICATION_UUID);

        // 1，读取本地持久化的配置信息
        // 2，上报配置信息到远程
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // 连接断开处理逻辑
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        MsgType msgType = baseMsg.getType();
        switch (msgType) {
            case PUSH_CONF:
                // 将server推送过来的数据放在本地缓存中
                PushMsg pushMsg = (PushMsg) baseMsg;
                if (CommonConstants.CONF_TYPE_ADD.equals(pushMsg.getConfType())) {
                    LightConfLocalCacheConf.set(pushMsg.getKey(),pushMsg.getValue());
                    logger.info("add application conf");
                } else if (CommonConstants.CONF_TYPE_UPDATE.equals(pushMsg.getConfType())) {
                    LightConfLocalCacheConf.update(pushMsg.getKey(),pushMsg.getValue());
                    logger.info("update application conf");
                } else {
                    LightConfLocalCacheConf.remove(pushMsg.getKey());
                    logger.info("delete application conf");
                }
                // 刷新配置信息
                LightConfLocalCacheConf.reloadAll();
                break;
            case LOGIN: {
                //向服务器发起登录
                LoginMsg loginMsg = new LoginMsg();
                loginMsg.setPassword("abcd");
                loginMsg.setUserName("wuhf");
                channelHandlerContext.writeAndFlush(loginMsg);
            }
            break;
            case PING: {
            }
            break;
            case ASK: {
                ReplyClientBody replyClientBody = new ReplyClientBody("util info **** !!!");
                ReplyMsg replyMsg = new ReplyMsg();
                replyMsg.setBody(replyClientBody);
                channelHandlerContext.writeAndFlush(replyMsg);
            }
            break;
            case REPLY: {
                ReplyMsg replyMsg = (ReplyMsg) baseMsg;
                ReplyServerBody replyServerBody = (ReplyServerBody) replyMsg.getBody();
                logger.info("receive util msg: " + replyServerBody.getServerInfo());
            }
            default:
                break;
        }
        ReferenceCountUtil.release(msgType);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("in client exceptionCaught.");
        super.exceptionCaught(ctx, cause);
        /**
         * 出现异常时，可以发送或者记录相关日志信息，之后，直接断开该链接，并重新登录请求，建立通道.
         */
    }
}
