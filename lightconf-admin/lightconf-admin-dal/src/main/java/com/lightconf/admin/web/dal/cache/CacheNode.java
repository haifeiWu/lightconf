package com.lightconf.admin.web.dal.cache;

/**
 * Created by qiuliang on 15/11/23.
 */
public class CacheNode {
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String host;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;
}
