package com.lightconf.admin.service;

import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.common.util.LightConfResult;

import java.util.Map;

/**
 * @author wuhf
 * @date 2018/02/11
 */
public interface ConfService {

    Map<String,Object> pageList(int start, int length, String nodeGroup, String nodeKey);

    LightConfResult deleteByKey(String nodeKey);

    LightConfResult add(Conf conf, String appUuid);

    LightConfResult update(Conf conf, String appUuid);

    /**
     * 根据id删除conf信息。
     * @param confId confId
     * @param appId
     * @return
     */
    LightConfResult deleteById(String confId, String appId);
}
