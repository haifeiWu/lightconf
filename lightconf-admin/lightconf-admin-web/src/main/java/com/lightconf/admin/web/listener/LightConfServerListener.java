package com.lightconf.admin.web.listener;

import com.lightconf.admin.web.netty.LightConfServerBootstrap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * lightConf服务端启动bean.
 *
 * @author wuhf
 * @date 2018/02/26
 */
public class LightConfServerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${netty.server.port}")
    private String nettyPort;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                int port = Integer.valueOf(nettyPort);
                new LightConfServerBootstrap(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
