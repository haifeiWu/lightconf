package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.AppMapper;
import com.lightconf.admin.model.dataobj.App;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.service.AppService;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.NettyChannelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wuhf
 * @date 2018/02/11
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppMapper appMapper;

    @Override
    public LightConfResult addApp(AppWithBLOBs app) {
        appMapper.insert(app);
        return LightConfResult.ok();
    }
}
