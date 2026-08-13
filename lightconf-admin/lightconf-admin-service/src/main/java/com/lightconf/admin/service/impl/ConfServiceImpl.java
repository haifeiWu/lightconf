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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author wuhf
 * @date 2018/02/11
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class ConfServiceImpl implements ConfService {

    private static final Logger LOGGER = log;

    @Autowired
    AppMapper appMapper;

    @Autowired
    ConfMapper confMapper;

    @Autowired
    AppConfMapper appConfMapper;

    @Override
    public LightConfResult add(Conf conf, String appId) {

        if (StringUtils.isBlank(appId)) {
            LOGGER.info(">>>>>> appUuid is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {

            // 按 (app_id, conf_key) 维度查重，不同应用可拥有同名 key
            Conf dbConf = getConfByAppAndKey(app.getId(), conf.getConfKey());
            if (dbConf != null) {
                return LightConfResult.build(Messages.CONF_ALREADY_EXISTS_CODE, Messages.CONF_ALREADY_EXISTS_MSG);
            }

            conf.setAppId(app.getId());
            confMapper.insert(conf);

            // 若应用与admin连接，则更新配置到客户端.
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> add conf，push conf to client！the client name is : {}", app.getAppName());
                pushConfToApplication(conf, CommonConstants.CONF_TYPE_ADD, app.getUuid());
            }
            LOGGER.info(">>>>>> add conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
        }
    }

    /**
     * 按 (app_id, conf_key) 查询配置。
     */
    private Conf getConfByAppAndKey(Integer appId, String confKey) {
        ConfExample confExample = new ConfExample();
        confExample.createCriteria()
                .andAppIdEqualTo(appId)
                .andConfKeyEqualTo(confKey);
        List<Conf> confList = confMapper.selectByExample(confExample);
        if (confList != null && confList.size() > 0) {
            return confList.get(0);
        }
        return null;
    }

    @Override
    public LightConfResult update(Conf conf, String appId) {

        if (StringUtils.isBlank(appId) || conf == null || conf.getId() == null) {
            LOGGER.error("appUuid or conf id is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {
            // 校验该配置确属当前应用，防止跨应用更新
            Conf dbConf = confMapper.selectByPrimaryKey(conf.getId());
            if (dbConf == null || !app.getId().equals(dbConf.getAppId())) {
                LOGGER.error(">>>>>> conf not found or not belong to app, confId : {}, appId : {}",
                        conf.getId(), appId);
                return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
            }

            conf.setAppId(app.getId());
            confMapper.updateByPrimaryKeySelective(conf);

            // 下发配置到应用
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> update conf , push conf to client! client name is : {}", app.getAppName());
                pushConfToApplication(conf, CommonConstants.CONF_TYPE_UPDATE, app.getUuid());
            }
            LOGGER.info(">>>>>> update conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
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
        if (StringUtils.isBlank(confId) || StringUtils.isBlank(appId)) {
            LOGGER.error("confId or appId is not allow be null");
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
        }

        App app = appMapper.selectByPrimaryKey(Integer.valueOf(appId));
        if (null != app) {

            int id = Integer.valueOf(confId);

            // 获取要删除的配置信息.
            Conf conf = confMapper.selectByPrimaryKey(id);
            if (conf == null || !app.getId().equals(conf.getAppId())) {
                LOGGER.error(">>>>>> conf not found or not belong to app, confId : {}, appId : {}", confId, appId);
                return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
            }

            // 删除配置信息。
            confMapper.deleteByPrimaryKey(id);

            // 清理历史关系表数据（新数据不再写入该表）。
            AppConfExample appConfExample = new AppConfExample();
            appConfExample.createCriteria().andConfIdEqualTo(confId);
            appConfMapper.deleteByExample(appConfExample);

            // 下发配置到应用
            if (app.getIsConnected()) {
                LOGGER.info(">>>>>> update conf , push conf to client! client name is : {}", app.getAppName());
                pushConfToApplication(conf, CommonConstants.CONF_TYPE_DELETE, app.getUuid());
            }

            LOGGER.info(">>>>>> update conf success");
            return LightConfResult.ok();
        } else {
            return LightConfResult.build(Messages.MISSING_INPUT_CODE, Messages.MISSING_INPUT_MSG);
        }
    }
}
