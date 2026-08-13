package com.lightconf.core.core;

import com.lightconf.core.LightConfClient;
import com.lightconf.core.env.Environment;
import com.lightconf.core.listener.LightConfListenerFactory;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * local cache conf
 *
 * @author wuhf
 * @date 2018/02/26
 */
public class LightConfLocalCacheConf {
    private static Logger logger = LoggerFactory.getLogger(LightConfLocalCacheConf.class);

    private static CacheManager cacheManager = null;
    private static Cache<String, CacheNode> lightConfLocalCache = null;
    static {
        // cacheManager
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        // default use ehcche.xml under src
        // .withExpiry、.withEvictionAdvisor （default lru）
        lightConfLocalCache = cacheManager.createCache("lightConfLocalCache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String.class, CacheNode.class, ResourcePoolsBuilder.heap(1000))
        );

        logger.info(">>>>>>>>>> light-conf, LightConfLocalCacheConf init success.");
    }

    /**
     * close cache manager
     */
    public static void destroy(){
        if (cacheManager != null) {
            cacheManager.close();
        }
    }

    /**
     * refresh conf (watch + refresh)
     */
    public static void reloadAll() {
        Set<String> keySet = new HashSet<>();
        Iterator<Cache.Entry<String, CacheNode>> iterator = lightConfLocalCache.iterator();
        while (iterator.hasNext()) {
            Cache.Entry<String, CacheNode> item = iterator.next();
            keySet.add(item.getKey());
        }
        for (String key : keySet) {
            String cacheData = LightConfClient.get(key);
            lightConfLocalCache.put(key, new CacheNode(cacheData));
        }
    }

    /**
     * set conf (invoke listener)
     *
     * @param key
     * @param value
     * @return
     */
    public static void set(String key, String value) {
        lightConfLocalCache.put(key, new CacheNode(value));
        logger.info(">>>>>>>>>> light-conf: SET: [{}={}]", key, value);

        LightConfListenerFactory.onChange(key,value);
    }

    /**
     * update conf  (only update exists key)  (invoke listener)
     *
     * @param key
     * @param value
     */
    public static void update(String key, String value) {
        if (lightConfLocalCache!=null && lightConfLocalCache.containsKey(key)) {
            lightConfLocalCache.put(key, new CacheNode(value));
            logger.info(">>>>>>>>>> light-conf: UPDATE: [{}={}]", key, value);

            LightConfListenerFactory.onChange(key,value);
        }
    }

    /**
     * remove conf
     *
     * @param key
     * @return
     */
    public static void remove(String key) {
        if (lightConfLocalCache!=null && lightConfLocalCache.containsKey(key)) {
            lightConfLocalCache.remove(key);
        }
        logger.info(">>>>>>>>>> light-conf: REMOVE: [{}]", key);
    }

    /**
     * get conf
     *
     * @param key
     * @return
     */
    public static CacheNode get(String key) {
        if (lightConfLocalCache!=null && lightConfLocalCache.containsKey(key)) {
            CacheNode cacheNode = lightConfLocalCache.get(key);
            return cacheNode;
        }
        return null;
    }

    /**
     * 获取全部缓存快照（用于本地持久化容灾）。
     */
    public static Map<String, String> getAll() {
        Map<String, String> snapshot = new HashMap<>();
        Iterator<Cache.Entry<String, CacheNode>> iterator = lightConfLocalCache.iterator();
        while (iterator.hasNext()) {
            Cache.Entry<String, CacheNode> item = iterator.next();
            CacheNode node = item.getValue();
            if (node != null && node.getValue() != null) {
                snapshot.put(item.getKey(), node.getValue());
            }
        }
        return snapshot;
    }

    /**
     * 本地缓存文件路径，可通过系统属性或环境变量 light.conf.cache.file 覆盖。
     */
    public static String resolveCacheFile() {
        String path = System.getProperty(Environment.LIGHT_CACHE_FILE);
        if (path == null) {
            path = System.getenv("LIGHT_CONF_CACHE_FILE");
        }
        if (path == null || path.trim().length() == 0) {
            path = System.getProperty("user.dir") + File.separator + Environment.LIGHT_CONF_CACHE;
        }
        return path;
    }

    /**
     * 启动时从本地缓存文件加载配置（不触发变更监听）。
     */
    public static void loadFromFile() {
        String path = resolveCacheFile();
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(path)) {
            props.load(in);
            for (String key : props.stringPropertyNames()) {
                lightConfLocalCache.put(key, new CacheNode(props.getProperty(key)));
            }
            if (!props.isEmpty()) {
                logger.info(">>>>>>>>>> light-conf, loaded {} conf from local cache file: {}", props.size(), path);
            }
        } catch (IOException e) {
            // 本地缓存文件不存在时属于正常情况（首次启动）
            logger.debug(">>>>>>>>>> light-conf, no local cache file found at: {}", path);
        }
    }

    /**
     * 将缓存快照持久化到本地文件。
     */
    public static void persistToFile() {
        String path = resolveCacheFile();
        Properties props = new Properties();
        props.putAll(getAll());
        try (OutputStream out = new FileOutputStream(path)) {
            props.store(out, "lightconf local cache, auto synced at " + new java.util.Date());
        } catch (IOException e) {
            logger.error(">>>>>>>>>> light-conf, persist local cache to file error, path: {}", path, e);
        }
    }


    /**
     * local cache node
     */
    public static class CacheNode {
        private String value;

        public CacheNode() {
        }

        public CacheNode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
