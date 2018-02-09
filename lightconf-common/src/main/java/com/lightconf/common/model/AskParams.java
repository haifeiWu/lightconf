package com.lightconf.common.model;

import java.io.Serializable;

/**
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
public class AskParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String auth;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
