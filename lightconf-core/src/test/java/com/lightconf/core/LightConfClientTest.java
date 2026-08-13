package com.lightconf.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * LightConfClient 三级取值逻辑测试（本地配置文件 > 本地缓存 > 默认值）。
 */
public class LightConfClientTest {

    @Test
    public void getReturnsDefaultWhenKeyMissing() {
        assertNull(LightConfClient.get("key-does-not-exist-" + System.nanoTime()));
        assertEquals("default-value", LightConfClient.get("key-does-not-exist-" + System.nanoTime(), "default-value"));
    }

    @Test
    public void getReturnsCachedValueAfterSet() {
        String key = "test.key." + System.nanoTime();
        LightConfClient.addListener(key, (k, v) -> { });
        // 通过本地缓存直接写入并触发监听
        com.lightconf.core.core.LightConfLocalCacheConf.set(key, "cache-value");
        assertEquals("cache-value", LightConfClient.get(key));
        com.lightconf.core.core.LightConfLocalCacheConf.remove(key);
        assertNull(LightConfClient.get(key));
    }

    @Test(expected = com.lightconf.core.exception.LightConfException.class)
    public void getIntThrowsWhenMissing() {
        LightConfClient.getInt("key-missing-" + System.nanoTime());
    }
}
