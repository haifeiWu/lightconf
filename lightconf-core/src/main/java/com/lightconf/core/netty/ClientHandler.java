package com.lightconf.core.netty;

import com.lightconf.common.model.*;
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
        MsgType msgType = baseMsg.getType();
        switch (msgType) {

            case PUSH_CONF:
                logger.info("update application conf");
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
