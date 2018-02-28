package com.lightconf.admin.service;

import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.common.util.LightConfResult;

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
}
