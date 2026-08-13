package com.lightconf.admin.web.listener;

import com.lightconf.admin.web.netty.LightConfServerBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * lightConf服务端启动bean.
 *
 * @author wuhf
 * @date 2018/02/26
 */
@Component
@Slf4j
public class LightConfServerListener implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {

    @Value("${netty.server.port:9998}")
    private int nettyPort;

    @Autowired
    private LightConfServerBootstrap lightConfServerBootstrap;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            return;
        }
        try {
            log.info(">>>>>> ApplicationListener start");
            lightConfServerBootstrap.start(nettyPort);
        } catch (InterruptedException e) {
            log.error(">>>>>> lightconf server start error", e);
        }
    }

    @Override
    public void destroy() {
        lightConfServerBootstrap.shutdown();
    }
}
