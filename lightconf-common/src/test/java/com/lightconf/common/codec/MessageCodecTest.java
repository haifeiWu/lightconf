package com.lightconf.common.codec;

import com.lightconf.common.model.AskMsg;
import com.lightconf.common.model.AskParams;
import com.lightconf.common.model.BaseMsg;
import com.lightconf.common.model.Config;
import com.lightconf.common.model.LoginMsg;
import com.lightconf.common.model.MsgType;
import com.lightconf.common.model.PingMsg;
import com.lightconf.common.model.PushMsg;
import com.lightconf.common.model.ReplyClientBody;
import com.lightconf.common.model.ReplyMsg;
import com.lightconf.common.model.ReplyServerBody;
import com.lightconf.common.util.CommonConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * JSON 编解码往返测试。
 *
 * 验证多态消息（顶层 seeAlso + 嵌套 seeAlso）在 fastjson 白名单机制下的
 * 序列化/反序列化一致性，以及恶意超长帧会被拒绝。
 */
public class MessageCodecTest {

    private EmbeddedChannel newCodecChannel() {
        return new EmbeddedChannel(new MessageEncoder(), new MessageDecoder());
    }

    private <T extends BaseMsg> T roundTrip(T msg, Class<T> expectedType) {
        EmbeddedChannel ch = newCodecChannel();
        assertTrue(ch.writeOutbound(msg));
        ByteBuf buf = ch.readOutbound();
        assertNotNull(buf);
        assertTrue(ch.writeInbound(buf));
        BaseMsg decoded = ch.readInbound();
        assertEquals(expectedType, decoded.getClass());
        return expectedType.cast(decoded);
    }

    @Test
    public void loginMsgRoundTrip() {
        LoginMsg loginMsg = new LoginMsg();
        loginMsg.setClientId("app-uuid-1");
        loginMsg.setUserName("demo-app");
        loginMsg.setPassword("secret-token");

        LoginMsg decoded = roundTrip(loginMsg, LoginMsg.class);
        assertEquals(MsgType.LOGIN, decoded.getType());
        assertEquals("app-uuid-1", decoded.getClientId());
        assertEquals("demo-app", decoded.getUserName());
        assertEquals("secret-token", decoded.getPassword());
    }

    @Test
    public void pingMsgRoundTrip() {
        PingMsg pingMsg = new PingMsg();
        PingMsg decoded = roundTrip(pingMsg, PingMsg.class);
        assertSame(MsgType.PING, decoded.getType());
    }

    @Test
    public void pushMsgWithConfigListRoundTrip() {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setClientId("app-uuid-1");
        pushMsg.setConfType(CommonConstants.CONF_TYPE_SEND_OUT);
        pushMsg.setType(MsgType.SEND_OUT);
        Config c1 = new Config();
        c1.setKey("k1");
        c1.setValue("v1");
        Config c2 = new Config();
        c2.setKey("k2");
        c2.setValue("v2");
        pushMsg.setConfigList(Arrays.asList(c1, c2));

        PushMsg decoded = roundTrip(pushMsg, PushMsg.class);
        assertEquals(MsgType.SEND_OUT, decoded.getType());
        assertEquals(CommonConstants.CONF_TYPE_SEND_OUT, decoded.getConfType());
        assertEquals(2, decoded.getConfigList().size());
        assertEquals("k1", decoded.getConfigList().get(0).getKey());
        assertEquals("v2", decoded.getConfigList().get(1).getValue());
    }

    @Test
    public void pushMsgSingleConfRoundTrip() {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setConfType(CommonConstants.CONF_TYPE_UPDATE);
        pushMsg.setKey("k1");
        pushMsg.setValue("v1");
        pushMsg.setType(MsgType.PUSH_CONF);

        PushMsg decoded = roundTrip(pushMsg, PushMsg.class);
        assertEquals("k1", decoded.getKey());
        assertEquals("v1", decoded.getValue());
    }

    @Test
    public void askMsgRoundTrip() {
        AskMsg askMsg = new AskMsg();
        askMsg.setClientId("app-uuid-1");
        AskParams params = new AskParams();
        params.setAuth("authToken");
        askMsg.setParams(params);

        AskMsg decoded = roundTrip(askMsg, AskMsg.class);
        assertEquals("authToken", decoded.getParams().getAuth());
    }

    @Test
    public void replyMsgWithClientBodyRoundTrip() {
        ReplyMsg replyMsg = new ReplyMsg();
        replyMsg.setClientId("app-uuid-1");
        ReplyClientBody body = new ReplyClientBody("client-info");
        replyMsg.setBody(body);

        ReplyMsg decoded = roundTrip(replyMsg, ReplyMsg.class);
        assertTrue(decoded.getBody() instanceof ReplyClientBody);
        assertEquals("client-info", ((ReplyClientBody) decoded.getBody()).getClientInfo());
    }

    @Test
    public void replyMsgWithServerBodyRoundTrip() {
        ReplyMsg replyMsg = new ReplyMsg();
        ReplyServerBody body = new ReplyServerBody("server-info");
        replyMsg.setBody(body);

        ReplyMsg decoded = roundTrip(replyMsg, ReplyMsg.class);
        assertTrue(decoded.getBody() instanceof ReplyServerBody);
        assertEquals("server-info", ((ReplyServerBody) decoded.getBody()).getServerInfo());
    }

    @Test
    public void oversizedFrameRejected() {
        EmbeddedChannel ch = new EmbeddedChannel(new MessageDecoder());
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(CommonConstants.MAX_FRAME_LENGTH + 1);
        buf.writeZero(16);
        ch.writeInbound(buf);
        // 超长帧导致连接被关闭
        assertFalse(ch.isActive());
    }

    @Test
    public void nonPositiveFrameRejected() {
        EmbeddedChannel ch = new EmbeddedChannel(new MessageDecoder());
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(0);
        ch.writeInbound(buf);
        assertFalse(ch.isActive());
    }
}
