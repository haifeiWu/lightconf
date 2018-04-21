package com.lightconf.admin.service;

import com.lightconf.common.util.LightConfResult;

/**
 * @author wuhf
 * @date 2018/04/21
 */
public interface UserService {
    LightConfResult userLogin(String userName, String password);
}
