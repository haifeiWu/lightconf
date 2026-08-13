package com.lightconf.core.core;

/**
 * 定时将缓存中的配置信息持久化到本地，实现服务端不可用时的本地容灾。
 *
 * @author wuhaifei 2019-11-09
 */
public class SyncCacheToFile implements Runnable {

    @Override
    public void run() {
        LightConfLocalCacheConf.persistToFile();
    }
}
