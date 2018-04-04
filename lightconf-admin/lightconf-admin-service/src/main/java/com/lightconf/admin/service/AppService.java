package com.lightconf.admin.service;

import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.common.util.LightConfResult;

import java.util.List;

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
    LightConfResult updateApp(AppWithBLOBs app);

    LightConfResult deleteApp(String appId);

    /**
     * 获取应用列表.
     * @param pageSize
     * @param pageNum
     * @return
     */
    LightConfResult getAppList(int pageSize, int pageNum);

    List<AppWithBLOBs> getAllApp();
}
