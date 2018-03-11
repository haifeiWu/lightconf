package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.AppMapper;
import com.lightconf.admin.model.dataobj.App;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.service.AppService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.NettyChannelMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        app.setUuid(UUID.randomUUID().toString());
        appMapper.insert(app);
        return LightConfResult.ok();
    }

    @Override
    public LightConfResult updateApp(AppWithBLOBs app) {
        appMapper.updateByPrimaryKeySelective(app);
        return LightConfResult.ok();
    }

    @Override
    public LightConfResult deleteApp(String appId) {
        if (StringUtils.isBlank(appId)) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE,Messages.MISSING_INPUT_MSG);
        }
        appMapper.deleteByPrimaryKey(Integer.valueOf(appId));
        return LightConfResult.ok();
    }

    @Override
    public LightConfResult getAppList(int pageSize, int pageNum) {
        return null;
    }
}
