package com.lightconf.admin.web.listener;

import com.lightconf.admin.web.netty.LightConfServerBootstrap;
import lombok.extern.slf4j.Slf4j;
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
public class LightConfServerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${netty.server.port}")
    private String nettyPort;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                log.info(">>>>>> ApplicationListener start");
                int port = Integer.valueOf(nettyPort);
                new LightConfServerBootstrap(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
