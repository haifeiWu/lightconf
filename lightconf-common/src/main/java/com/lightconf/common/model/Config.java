package com.lightconf.common.model;

import java.io.Serializable;

/**
 * 配置对象
 * @author wuhaifei
 * @date 2018/04/27
 */
public class Config implements Serializable {
    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
