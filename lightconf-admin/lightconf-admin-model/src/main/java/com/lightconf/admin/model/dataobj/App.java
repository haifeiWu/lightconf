package com.lightconf.admin.model.dataobj;

import java.io.Serializable;

public class App implements Serializable {
    private Integer id;

    private String uuid;

    private String appName;

    private String appDesc;

    private Boolean isConnected;

    private Boolean isChange;

    private Boolean isPushConf;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc == null ? null : appDesc.trim();
    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Boolean getIsChange() {
        return isChange;
    }

    public void setIsChange(Boolean isChange) {
        this.isChange = isChange;
    }

    public Boolean getIsPushConf() {
        return isPushConf;
    }

    public void setIsPushConf(Boolean isPushConf) {
        this.isPushConf = isPushConf;
    }
}