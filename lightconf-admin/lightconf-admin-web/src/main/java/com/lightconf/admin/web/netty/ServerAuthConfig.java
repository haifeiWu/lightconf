package com.lightconf.admin.web.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务端鉴权配置：客户端连接握手时校验的应用共享密钥。
 *
 * @author wuhf
 */
@Component
public class ServerAuthConfig {

    /**
     * 应用共享密钥，可通过环境变量 LIGHTCONF_SECRET 覆盖。
     */
    @Value("${light.conf.secret:}")
    private String secret;

    public String getSecret() {
        return secret;
    }
}
