package com.lightconf.admin.model.dataobj;

import java.io.Serializable;

public class Conf implements Serializable {
    private Integer id;

    private Integer appId;

    private String confKey;

    private String confValue;

    private String confDesc;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getConfKey() {
        return confKey;
    }

    public void setConfKey(String confKey) {
        this.confKey = confKey == null ? null : confKey.trim();
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue == null ? null : confValue.trim();
    }

    public String getConfDesc() {
        return confDesc;
    }

    public void setConfDesc(String confDesc) {
        this.confDesc = confDesc == null ? null : confDesc.trim();
    }
}