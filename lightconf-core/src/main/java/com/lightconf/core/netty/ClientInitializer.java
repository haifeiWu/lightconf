package com.lightconf.core.netty;

import com.lightconf.common.codec.MessageDecoder;
import com.lightconf.common.codec.MessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author wuhf
 */
public class ClientInitializer  extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //IdleStateHandler检测心跳.
        ChannelPipeline p = channel.pipeline();
        p.addLast(new IdleStateHandler(20, 10, 0));
//        p.addLast(new MessageDecoder());
//        p.addLast(new MessageEncoder());
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
        p.addLast(new ObjectEncoder());
        p.addLast(new ClientHandler());
    }
}
