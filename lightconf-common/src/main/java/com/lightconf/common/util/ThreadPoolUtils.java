package com.lightconf.common.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池创建.
 * @author wuhf
 * @date 2018/01/16
 */
public class ThreadPoolUtils {

    private static ScheduledExecutorService executorService;

    private ThreadPoolUtils() {
        //手动创建线程池.
        executorService = new ScheduledThreadPoolExecutor(CommonConstants.CORE_POOL_SIZE,
                new BasicThreadFactory.Builder().namingPattern("syncdata-schedule-pool-%d").daemon(true).build());
    }

    private static class PluginConfigHolder {
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getInstance() {
        return PluginConfigHolder.INSTANCE;
    }

    public ScheduledExecutorService getThreadPool(){
        return executorService;
    }

}
