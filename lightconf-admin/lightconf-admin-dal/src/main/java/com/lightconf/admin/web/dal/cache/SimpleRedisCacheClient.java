package com.lightconf.admin.web.dal.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xxm on 15/9/15.
 */
public class SimpleRedisCacheClient implements CacheClient {

    static Logger logger = LoggerFactory.getLogger(SimpleRedisCacheClient.class);

    private CacheConfig config;

    private JedisCommands jedis;


    public SimpleRedisCacheClient(CacheConfig cacheConfig) {
        try {
            this.config = cacheConfig;
            ArrayList<CacheNode> nodes = getNodes(cacheConfig.getHost());

            if (nodes.size() == 0) {
                throw new Exception("redis node length = 0");
            }

            if (nodes.size() > 1) {
                Set<HostAndPort> jedisClusterNodes = new HashSet<>();
                for (CacheNode node : nodes) {
                    jedisClusterNodes.add(new HostAndPort(node.getHost(), node.getPort()));
                }
                JedisCluster jc = new JedisCluster(jedisClusterNodes);
                this.jedis = jc;
            } else {
                this.jedis = new Jedis(nodes.get(0).getHost(), nodes.get(0).getPort());
            }


        } catch (Exception e) {
            logger.error(e.getMessage() + "redis cluster init error", e);
        }

    }


    public <T extends Object> T get(String key, Class<T> clazz) {
        String r = jedis.get(formatKey(key));

        T t = JSON.parseObject(r, clazz);
        return t;

    }

    @Override
    public String get(String key) {
        return jedis.get(formatKey(key));
    }

    @Override
    public void set(String key, String value) {
        jedis.setex(formatKey(key), config.getExpireTime(), value);
    }

    @Override
    public void set(String key, Object value) {
        String v = JSONObject.toJSONString(value);
        jedis.setex(formatKey(key), config.getExpireTime(), v);
    }

    @Override
    public void remove(String key) {
        jedis.del(formatKey(key));
    }


    @Override
    public JedisCommands getJedisCmds() {
        return this.jedis;
    }

    /**************************************
     * private methods
     **************************************/


    private String formatKey(String key) {
        return String.format("%s_%s", this.config.getPrefix(), key);
    }

    private ArrayList<CacheNode> getNodes(String config) throws Exception {
        if (StringUtils.isEmpty(config)) {
            throw new Exception("config can not be null");
        }
        String[] hosts = config.split(",");
        ArrayList<CacheNode> nodes = new ArrayList<>();

        for (String h : hosts) {
            String[] hp = h.split(":");
            CacheNode cacheNode = new CacheNode();
            cacheNode.setHost(hp[0]);
            cacheNode.setPort(Integer.parseInt(hp[1]));
            nodes.add(cacheNode);
        }

        return nodes;
    }
}
