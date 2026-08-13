package com.lightconf.core.listener;

import com.lightconf.common.util.ThreadPoolUtils;
import com.lightconf.core.core.LightConfLocalCacheConf;
import com.lightconf.core.core.LightConfPropConf;
import com.lightconf.core.core.SyncCacheToFile;
import com.lightconf.core.env.Environment;
import com.lightconf.core.netty.ClientBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 初始化监听器.
 *
 * @author wuhf
 * @date 2018/02/11
 */
public class LightConfClientListener implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightConfClientListener.class);

    private static volatile ScheduledExecutorService syncTimer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // 防止二次加载
        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            return;
        }

        // 加载本地缓存文件（服务端不可用时的容灾数据）
        LightConfLocalCacheConf.loadFromFile();

        // 启动netty客户端（构造器内部完成连接与自动重连）
        int port = Integer.valueOf(LightConfPropConf.get(Environment.LIGHT_CONF_PORT));
        String host = LightConfPropConf.get(Environment.LIGHT_CONF_HOST);
        String appId = LightConfPropConf.get(Environment.APPLICATION_UUID);
        ThreadPoolUtils.getInstance().getThreadPool().submit(() -> {
            try {
                new ClientBootstrap(host, port, appId);
            } catch (InterruptedException e) {
                LOGGER.error(">>>>>>>>>> lightconf client start error", e);
            }
        });
        LOGGER.info(">>>>>>>>>> lightconf client start at host : {} ,port ： {}", host, port);

        // 启动定时器，定时将缓存中的数据持久化到本地文件
        startSyncTimer();
    }

    private synchronized void startSyncTimer() {
        if (syncTimer == null) {
            syncTimer = Executors.newSingleThreadScheduledExecutor();
            syncTimer.scheduleAtFixedRate(new SyncCacheToFile(), 0, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void destroy() {
        if (syncTimer != null) {
            syncTimer.shutdownNow();
        }
    }
}
