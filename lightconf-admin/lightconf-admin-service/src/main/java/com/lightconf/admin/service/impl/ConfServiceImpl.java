package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.AppConfMapper;
import com.lightconf.admin.dal.dao.AppMapper;
import com.lightconf.admin.dal.dao.ConfMapper;
import com.lightconf.admin.model.dataobj.*;
import com.lightconf.admin.service.ConfService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.model.MsgType;
import com.lightconf.common.model.PushMsg;
import com.lightconf.common.util.CommonConstants;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.NettyChannelMap;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author wuhf
 * @date 2018/02/11
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ConfServiceImpl implements ConfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfServiceImpl.class);

    @Autowired
    AppMapper appMapper;

    @Autowired
    ConfMapper confMapper;

    @Autowired
    AppConfMapper appConfMapper;

    @Override
    public Map<String, Object> pageList(int start, int length, String nodeGroup, String nodeKey) {
        return null;
    }

    @Override
    public LightConfResult deleteByKey(String nodeKey) {
        return null;
    }

    @Override
    public LightConfResult add(Conf conf, String appId) {

        if (StringUtils.isBlank(appId)) {
            LOGGER.info(">>>>>> appUuid is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {
            confMapper.insert(conf);
            AppConf appConf = new AppConf();
            appConf.setAppId(String.valueOf(app.getId()));
            appConf.setConfId(String.valueOf(conf.getId()));
            appConfMapper.insert(appConf);

            // 若应用与admin连接，则更新配置到客户端.
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> add conf，push conf to client！the client name is : {}",app.getAppName());
                pushConfToApplication(conf,CommonConstants.CONF_TYPE_ADD,app.getUuid());
            }
            LOGGER.info(">>>>>> add conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }
    }

    @Override
    public LightConfResult update(Conf conf, String appId) {

        if (StringUtils.isBlank(appId)) {
            LOGGER.error("appUuid is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {
            confMapper.updateByPrimaryKeySelective(conf);

            // 下发配置到应用
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> update conf , push conf to client! client name is : {}",app.getAppName());
                pushConfToApplication(conf,CommonConstants.CONF_TYPE_UPDATE,app.getUuid());
            }
            LOGGER.info(">>>>>> update conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }
    }

    private void pushConfToApplication(Conf conf, String confTypeUpdate, String uuid) {

        SocketChannel socketChannel = (SocketChannel) NettyChannelMap.get(uuid);

        // 若socketChanel不为空，更新配置到客户端.
        if (socketChannel != null) {
            PushMsg pushMsg = new PushMsg();
            pushMsg.setConfType(confTypeUpdate);
            pushMsg.setKey(conf.getConfKey());
            pushMsg.setValue(conf.getConfValue());
            pushMsg.setType(MsgType.PUSH_CONF);
            socketChannel.writeAndFlush(pushMsg);
            LOGGER.info(">>>>>> push conf value to application");
        }
    }

    @Override
    public LightConfResult deleteById(String confId, String appId) {
        if (StringUtils.isBlank(confId) ) {
            LOGGER.error("confId is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {

            int id = Integer.valueOf(confId);

            // 获取要删除的配置信息.
            Conf conf = confMapper.selectByPrimaryKey(id);

            // 删除配置信息。
            confMapper.deleteByPrimaryKey(id);

            // 删除关系表数据。
            AppConfExample appConfExample = new AppConfExample();
            appConfExample.createCriteria().andConfIdEqualTo(confId);
            appConfMapper.deleteByExample(appConfExample);

            // 下发配置到应用
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> update conf , push conf to client! client name is : {}",app.getAppName());
                pushConfToApplication(conf,CommonConstants.CONF_TYPE_DELETE,app.getUuid());
            }

            LOGGER.info(">>>>>> update conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE,Messages.MISSING_INPUT_MSG);
        }
    }
}
