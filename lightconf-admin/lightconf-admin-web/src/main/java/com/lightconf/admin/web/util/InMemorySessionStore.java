package com.lightconf.admin.web.util;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进程内登录态存储（默认实现）。
 *
 * <p>仅单实例部署可用；多实例水平扩展时设置
 * <code>light.conf.session.store=redis</code> 以启用 {@link RedisSessionStore}。</p>
 *
 * @author whfstudio
 */
@Component
@ConditionalOnProperty(prefix = "light.conf.session", name = "store", havingValue = "memory", matchIfMissing = true)
public class InMemorySessionStore implements SessionStore {

    /** token -> 过期时间戳(ms) */
    private static final Map<String, Long> LOGIN_STATUS = new ConcurrentHashMap<>();

    /** token 有效期：2 小时 */
    private static final long TOKEN_TTL_MILLIS = 2 * 60 * 60 * 1000L;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String createToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        String token = new BigInteger(1, bytes).toString(16);
        LOGIN_STATUS.put(token, System.currentTimeMillis() + TOKEN_TTL_MILLIS);
        return token;
    }

    @Override
    public boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        Long expireAt = LOGIN_STATUS.get(token);
        return expireAt != null && expireAt > System.currentTimeMillis();
    }

    @Override
    public void remove(String token) {
        if (token != null) {
            LOGIN_STATUS.remove(token);
        }
    }
}
