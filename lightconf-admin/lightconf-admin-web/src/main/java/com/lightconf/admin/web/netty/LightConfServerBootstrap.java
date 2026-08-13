package com.lightconf.admin.web.netty;

import com.lightconf.common.codec.MessageDecoder;
import com.lightconf.common.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * netty服务端启动.
 *
 * @author wuhf
 * @date 2018/02/26
 */
@Slf4j
@Component
public class LightConfServerBootstrap {

    private final ServerHandler serverHandler;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    @Autowired
    public LightConfServerBootstrap(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void start(int port) throws InterruptedException {
        if (boss != null || worker != null) {
            return;
        }
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new MessageDecoder());
                        p.addLast(new MessageEncoder());
                        p.addLast(serverHandler);
                    }
                });

        ChannelFuture f = bootstrap.bind(port).sync();
        if (f.isSuccess()) {
            log.info(String.format(">>>>>>>>>>>> lightconf server started, port:%s", port));
        }
    }

    public void shutdown() {
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
        boss = null;
        worker = null;
    }
}
