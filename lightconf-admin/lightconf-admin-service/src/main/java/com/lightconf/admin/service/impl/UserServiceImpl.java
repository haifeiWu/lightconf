package com.lightconf.admin.service.impl;

import com.lightconf.admin.service.UserService;
import com.lightconf.common.util.LightConfResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wuhf
 * @date 2018/04/21
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {
    @Override
    public LightConfResult userLogin(String userName, String password) {
        return null;
    }
}
