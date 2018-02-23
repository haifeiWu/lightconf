package com.lightconf.admin.service.impl;

import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.ConfService;
import com.lightconf.common.util.LightConfResult;
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
    @Override
    public Map<String, Object> pageList(int start, int length, String nodeGroup, String nodeKey) {
        return null;
    }

    @Override
    public LightConfResult deleteByKey(String nodeKey) {
        return null;
    }

    @Override
    public LightConfResult add(Conf conf) {
        return null;
    }

    @Override
    public LightConfResult update(Conf conf) {
        return null;
    }
}
