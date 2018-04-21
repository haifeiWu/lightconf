package com.lightconf.admin.model.dataobj;

import java.io.Serializable;

public class ConfLog implements Serializable {
    private Integer id;

    private Integer logId;

    private Integer confId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }
}