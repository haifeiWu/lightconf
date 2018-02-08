package com.lightconf.common.sync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author whfstudio@163.com
 * @date 2017/11/27
 */
public class SyncWriteMap {

    public static Map<String, WriteFuture> syncKey = new ConcurrentHashMap<String, WriteFuture>();

}
