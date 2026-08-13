package com.lightconf.admin.web.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录态缓存：随机 token + 过期时间。
 *
 * <p>注意：当前为进程内存储，多实例部署时各实例不共享登录态，
 * 如需水平扩展应替换为 Redis 等共享存储。</p>
 *
 * @author wuhaifei 2019-06-04
 */
public class CacheUtils {

    /** token -> 过期时间戳(ms) */
    private static final Map<String, Long> LOGIN_STATUS = new ConcurrentHashMap<>();

    /** token 有效期：2 小时 */
    private static final long TOKEN_TTL_MILLIS = 2 * 60 * 60 * 1000L;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CacheUtils() {
    }

    /**
     * 创建随机登录 token。
     */
    public static String createToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        String token = new BigInteger(1, bytes).toString(16);
        LOGIN_STATUS.put(token, System.currentTimeMillis() + TOKEN_TTL_MILLIS);
        return token;
    }

    /**
     * 校验 token 是否有效且未过期。
     */
    public static boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        Long expireAt = LOGIN_STATUS.get(token);
        return expireAt != null && expireAt > System.currentTimeMillis();
    }

    /**
     * 使 token 失效。
     */
    public static void remove(String token) {
        if (token != null) {
            LOGIN_STATUS.remove(token);
        }
    }
}
