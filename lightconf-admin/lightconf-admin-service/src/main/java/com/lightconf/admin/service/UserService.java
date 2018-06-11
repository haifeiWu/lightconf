package com.lightconf.admin.service;

import com.lightconf.common.util.LightConfResult;

import java.util.Map;

/**
 * @author wuhf
 * @date 2018/04/21
 */
public interface UserService {
    LightConfResult userLogin(String userName, String password);

    Map<String, Object> getUserList(int start, int length, String username, int permission);
}
