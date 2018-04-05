package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.AppMapper;
import com.lightconf.admin.dal.dao.ConfMapper2;
import com.lightconf.admin.model.dataobj.AppExample;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.AppService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author wuhf
 * @date 2018/02/11
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AppServiceImpl implements AppService {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    AppMapper appMapper;

    @Autowired
    ConfMapper2 confMapper2;

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
            LOGGER.info(">>>>>> params is not be null");
            return LightConfResult.build(Messages.INPUT_ERROR_CODE,Messages.MISSING_INPUT_MSG);
        }
        appMapper.deleteByPrimaryKey(Integer.valueOf(appId));
        return LightConfResult.ok();
    }

    @Override
    public LightConfResult getAppList(int pageSize, int pageNum) {
        return null;
    }

    @Override
    public List<AppWithBLOBs> getAllApp() {
        AppExample appExample = new AppExample();
        return appMapper.selectByExampleWithBLOBs(appExample);
    }

    @Override
    public LightConfResult getAppConf(String appId) {
        if (StringUtils.isBlank(appId)) {
            LOGGER.info(">>>>>> params is not be null");
            return LightConfResult.build(Messages.INPUT_ERROR_CODE,Messages.MISSING_INPUT_MSG);
        }
        List<Conf> confList = confMapper2.getAppConf(Integer.valueOf(appId));
        return LightConfResult.build(Messages.SUCCESS_CODE,Messages.SUCCESS_MSG,confList);
    }
}
