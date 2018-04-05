package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.Conf;

import java.util.List;

public interface ConfMapper2 {
    List<Conf> getAppConf(int appId);
}