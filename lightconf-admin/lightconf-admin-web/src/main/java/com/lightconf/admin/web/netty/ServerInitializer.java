package com.lightconf.admin.web.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
//        p.addLast(new MessageEncoder());
//        p.addLast(new MessageDecoder());
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));
        p.addLast(new ObjectEncoder());
        p.addLast(new ServerHandler());
    }
}
