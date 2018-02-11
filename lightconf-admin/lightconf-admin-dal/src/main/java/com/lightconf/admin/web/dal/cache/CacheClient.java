package com.lightconf.admin.web.dal.cache;

import redis.clients.jedis.JedisCommands;

/**
 * Created by xxm on 15/9/15.
 */
public interface CacheClient {
    <T extends Object> T get(String key, Class<T> clazz);

    String get(String key);

    void set(String key, Object value);

    void set(String key, String value);

    void remove(String key);

    JedisCommands getJedisCmds();
}
