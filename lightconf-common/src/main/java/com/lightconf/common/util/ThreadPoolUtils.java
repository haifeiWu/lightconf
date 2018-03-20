package com.lightconf.common.util;

import java.util.concurrent.*;

/**
 * 线程池创建.
 *
 * @author wuhf
 * @date 2018/01/16
 */
public class ThreadPoolUtils {

    private static ExecutorService executorService;

    private ThreadPoolUtils() {
        //手动创建线程池.
        executorService = new ThreadPoolExecutor(CommonConstants.CORE_POOL_SIZE,
                CommonConstants.MAX_POOL_SIZE, CommonConstants.THREAD_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    private static class PluginConfigHolder {
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getInstance() {
        return PluginConfigHolder.INSTANCE;
    }

    public ExecutorService getThreadPool() {
        return executorService;
    }

}
