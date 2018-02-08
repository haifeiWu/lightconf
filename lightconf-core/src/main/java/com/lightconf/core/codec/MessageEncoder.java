package com.lightconf.core.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器.
 *
 * @author wuhf
 * @date 2018/01/29
 */
@Sharable
public class MessageEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object baseMsg, ByteBuf byteBuf) throws Exception {
        //将对象转换为byte
        byte[] body = JSON.toJSONBytes(baseMsg, SerializerFeature.WriteClassName);

        //读取消息的长度
        int dataLength = body.length;

        //先将消息长度写入，也就是消息头
        byteBuf.writeInt(dataLength);

        //消息体中包含我们要发送的数据
        byteBuf.writeBytes(body);
    }
}
