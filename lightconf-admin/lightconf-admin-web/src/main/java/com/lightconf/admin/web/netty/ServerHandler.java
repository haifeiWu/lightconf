package com.lightconf.admin.web.netty;

import com.lightconf.common.model.*;
import com.lightconf.common.util.NettyChannelMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuhf
 * @date 2018/02/09
 */
public class ServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("in channelInactive.");
        NettyChannelMap.remove((SocketChannel) ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
            BaseMsg baseMsg) throws Exception {
        if (MsgType.LOGIN.equals(baseMsg.getType())) {
            lightConfClientLogin(channelHandlerContext, baseMsg);
        }

        switch (baseMsg.getType()) {

            case PUSH_CONF:
                logger.info("do nothing");
                break;

            case PING: {
                PingMsg pingMsg = (PingMsg) baseMsg;
                PingMsg replyPing = new PingMsg();
                NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
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
                logger.info("receive util msg: " + clientBody.getClientInfo());
            }
            break;
            case LOGIN:
                break;
            default: {
                NettyChannelMap.remove((SocketChannel) channelHandlerContext.channel());
                channelHandlerContext.disconnect();
            }
            break;
        }
        ReferenceCountUtil.release(baseMsg);
    }

    /**
     * 客户端登录.
     * @param channelHandlerContext
     * @param baseMsg
     */
    private void lightConfClientLogin(ChannelHandlerContext channelHandlerContext,
            BaseMsg baseMsg) {
        /**
         * 实现登录成功的逻辑.
         */
        LoginMsg loginMsg = (LoginMsg) baseMsg;
        if ("wuhf".equals(loginMsg.getUserName()) && "abcd".equals(loginMsg.getPassword())) {
            // 登录成功,把channel存到服务端的map中.
            NettyChannelMap.add(loginMsg.getClientId(), (SocketChannel) channelHandlerContext.channel());
            logger.info("client" + loginMsg.getClientId() + " 登录成功");
        } else {
            if (NettyChannelMap.get(baseMsg.getClientId()) == null) {
                // 说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录.
                channelHandlerContext.channel().writeAndFlush(new LoginMsg());
                return;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        super.exceptionCaught(ctx, cause);
        NettyChannelMap.remove((SocketChannel) ctx.channel());
        logger.error(
                "channel is exception over. (SocketChannel)ctx.channel()=" + (SocketChannel) ctx
                        .channel());
    }
}
