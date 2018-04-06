package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.Conf;

import java.util.List;

public interface ConfMapper2 {

    /**
     * /获取应用的配置信息。
     * @param appId
     * @return
     */
    List<Conf> getAppConf(int appId);
}