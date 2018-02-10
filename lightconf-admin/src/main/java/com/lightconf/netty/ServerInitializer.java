package com.lightconf.netty;

import com.lightconf.common.codec.MessageDecoder;
import com.lightconf.common.codec.MessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class ServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline p = channel.pipeline();
        p.addLast(new MessageEncoder());
        p.addLast(new MessageDecoder());
        p.addLast(new ServerHandler());
    }
}
