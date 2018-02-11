package com.lightconf.core.listener;

import com.lightconf.common.util.ThreadPoolUtils;
import com.lightconf.core.core.LightConfPropConf;
import com.lightconf.core.env.Environment;
import com.lightconf.core.netty.ClientBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 初始化监听器.
 *
 * @author wuhf
 * @date 2018/02/11
 */
public class LightConfListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightConfListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            // 加载配置文件的配置.
            int port = Integer.valueOf(LightConfPropConf.get(Environment.LIGHT_CONF_PORT));
            String host = LightConfPropConf.get(Environment.LIGHT_CONF_HOST);

            // 启动netty客户端.
            ClientBootstrap clientBootstrap = new ClientBootstrap(host,port);
            ThreadPoolUtils.getInstance().getThreadPool().submit(new InitClientThread(clientBootstrap));
            LOGGER.info("client start at host : {} ,port ： {}",host,port);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }



    class InitClientThread implements Runnable {

        private ClientBootstrap clientBootstrap;

        public InitClientThread(ClientBootstrap clientBootstrap) {
            this.clientBootstrap = clientBootstrap;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            clientBootstrap.start();
        }
    }

}
