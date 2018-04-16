package com.lightconf.admin.service;

import com.lightconf.admin.model.dataobj.App;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.common.util.LightConfResult;

import java.util.List;
import java.util.Map;

/**
 * @author wuhf
 * @date 2018/02/11
 */
public interface AppService {

    /**
     * 添加app信息.
     * @param app
     * @return
     */
    LightConfResult addApp(AppWithBLOBs app);

    /**
     * 修改应用配置信息.
     * @param app
     * @return
     */
    LightConfResult updateApp(App app);

    LightConfResult deleteApp(String appId);

    /**
     * 获取应用列表.
     * @param pageSize
     * @param pageNum
     * @return
     */
    LightConfResult getAppList(int pageSize, int pageNum);

    List<AppWithBLOBs> getAllApp();

    /**
     * 获取应用的配置信息.
     * @param appId appId.
     * @return
     */
    LightConfResult getAppConf(String appId);

    /**
     * 获取应用的配置信息，分页显示。
     * @param start start
     * @param length length
     * @param appId appId
     * @param confKey
     * @return
     */
    Map<String, Object> getAppConfByPage(int start, int length, String appId, String confKey);

    /**
     * 根据uuid查询应用信息！
     * @param appUUid
     * @return
     */
    App getAppByUUID(String appUUid);

    LightConfResult updateAppWithBLOBs(AppWithBLOBs app);
}
