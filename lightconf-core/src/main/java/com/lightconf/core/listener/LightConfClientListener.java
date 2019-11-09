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
public class LightConfClientListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightConfClientListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // 防止二次加载
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                // 加载配置文件的配置.
                int port = Integer.valueOf(LightConfPropConf.get(Environment.LIGHT_CONF_PORT));
                String host = LightConfPropConf.get(Environment.LIGHT_CONF_HOST);
                String appId = LightConfPropConf.get(Environment.APPLICATION_UUID);

                // 启动netty客户端.
                ClientBootstrap clientBootstrap = new ClientBootstrap(host,port,appId);
                ThreadPoolUtils.getInstance().getThreadPool().submit(new InitClientThread(clientBootstrap));
                LOGGER.info(">>>>>>>>>> lightconf client start at host : {} ,port ： {}",host,port);

                // TODO 启动定时器，定时将缓存中的数据持久化到本地文件
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
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
