package com.lightconf.netty;

import com.lightconf.common.model.AskMsg;
import com.lightconf.common.util.NettyChannelMap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

    private int port;
    private SocketChannel socketChannel;

    public ServerBootstrap(int port) throws InterruptedException {
        this.port = port;
        bind();
    }

    private void bind() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        io.netty.bootstrap.ServerBootstrap bootstrap = new io.netty.bootstrap.ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServerInitializer());

        ChannelFuture f = bootstrap.bind(port).sync();
        if (f.isSuccess()) {
            logger.info("server start---------------");
        }
    }

    private static final ThreadLocal<Long> TEST = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static void main(String[] args) throws InterruptedException {
        TEST.set(System.currentTimeMillis());
        ServerBootstrap bootstrap = new ServerBootstrap(9999);
        while (true) {
            SocketChannel channel = (SocketChannel) NettyChannelMap.get("001");
            if (channel != null) {
                AskMsg askMsg = new AskMsg();
                channel.writeAndFlush(askMsg);
            }
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
