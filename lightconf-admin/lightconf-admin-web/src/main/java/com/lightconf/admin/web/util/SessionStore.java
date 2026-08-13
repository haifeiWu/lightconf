package com.lightconf.admin.web.util;

/**
 * 登录会话存储抽象：默认内存实现 {@link InMemorySessionStore}；
 * 多实例水平扩展时可提供 Redis 等共享实现（参见 {@link RedisSessionStore}）。
 *
 * @author whfstudio
 */
public interface SessionStore {

    /**
     * 创建随机登录 token。
     */
    String createToken();

    /**
     * 校验 token 是否有效且未过期。
     */
    boolean isValid(String token);

    /**
     * 使 token 失效。
     */
    void remove(String token);
}
