package com.lightconf.admin.service;

import com.lightconf.admin.model.dataobj.User;
import com.lightconf.common.util.LightConfResult;

/**
 * @author wuhf
 * @date 2018/04/21
 */
public interface UserService {
    LightConfResult userLogin(String userName, String password);

    LightConfResult addUser(User confUser);

    LightConfResult deleteUser(String username);

    LightConfResult updateUser(User confUser);
}
