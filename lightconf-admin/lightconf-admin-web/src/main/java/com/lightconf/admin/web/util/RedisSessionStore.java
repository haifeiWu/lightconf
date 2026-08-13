package com.lightconf.admin.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的登录态存储（多实例共享）。
 *
 * <p>通过配置 <code>light.conf.session.store=redis</code> 启用；
 * 需配置 <code>spring.redis.*</code> 连接参数。token 带 2 小时 TTL 自动过期。</p>
 *
 * @author whfstudio
 */
@Component
@ConditionalOnProperty(prefix = "light.conf.session", name = "store", havingValue = "redis")
public class RedisSessionStore implements SessionStore {

    private static final String KEY_PREFIX = "lightconf:session:";

    /** token 有效期：2 小时 */
    private static final long TOKEN_TTL_MILLIS = 2 * 60 * 60 * 1000L;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String createToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        String token = new BigInteger(1, bytes).toString(16);
        redisTemplate.opsForValue().set(KEY_PREFIX + token, "1", TOKEN_TTL_MILLIS, TimeUnit.MILLISECONDS);
        return token;
    }

    @Override
    public boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + token));
    }

    @Override
    public void remove(String token) {
        if (token != null) {
            redisTemplate.delete(KEY_PREFIX + token);
        }
    }
}
