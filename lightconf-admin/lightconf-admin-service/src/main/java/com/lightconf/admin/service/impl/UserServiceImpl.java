package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.UserMapper;
import com.lightconf.admin.model.dataobj.User;
import com.lightconf.admin.model.dataobj.UserExample;
import com.lightconf.admin.service.UserService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author wuhf
 * @date 2018/04/21
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * BCrypt 编码器（线程安全，可静态共享）。
     */
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    UserMapper userMapper;

    @Override
    public LightConfResult userLogin(String userName, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(userName);
        List<User> userList = userMapper.selectByExample(userExample);

        if (userList != null && userList.size() > 0) {
            User user = userList.get(0);
            if (StringUtils.isNotBlank(user.getPassword()) && passwordMatches(password, user.getPassword())) {
                return LightConfResult.build(Messages.SUCCESS_CODE, Messages.SUCCESS_MSG, userList.get(0));
            }
        }

        return LightConfResult.build(Messages.USER_LOGIN_ERROR_CODE, Messages.USER_LOGIN_ERROR_MSG);
    }

    /**
     * 密码校验：新密码为 BCrypt；兼容存量 MD5 密码。
     */
    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword.startsWith("$2")) {
            return PASSWORD_ENCODER.matches(rawPassword, storedPassword);
        }
        // 存量数据兼容：MD5
        return DigestUtils.md5DigestAsHex(rawPassword.getBytes()).equalsIgnoreCase(storedPassword);
    }

    @Override
    public LightConfResult addUser(User confUser) {

        // valid
        if (StringUtils.isBlank(confUser.getUserName()) || StringUtils.isBlank(confUser.getPassword())) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE, Messages.INPUT_ERROR_MSG);
        }
        if (!(confUser.getPassword().length() >= 4 && confUser.getPassword().length() <= 100)) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE, Messages.INPUT_ERROR_MSG);
        }

        // password bcrypt
        confUser.setPassword(PASSWORD_ENCODER.encode(confUser.getPassword()));
        userMapper.insert(confUser);
        return LightConfResult.ok(confUser);
    }

    @Override
    public LightConfResult deleteUser(String username) {
        if (StringUtils.isBlank(username)) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE, Messages.INPUT_ERROR_MSG);
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(username);
        userMapper.deleteByExample(userExample);
        return LightConfResult.ok();
    }

    @Override
    public LightConfResult updateUser(User confUser) {

        // valid
        if (StringUtils.isBlank(confUser.getUserName()) || StringUtils.isBlank(confUser.getPassword())) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE, Messages.INPUT_ERROR_MSG);
        }

        if (!(confUser.getPassword().length() >= 4 && confUser.getPassword().length() <= 100)) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE, Messages.INPUT_ERROR_MSG);
        }

        // update password
        if (StringUtils.isNotBlank(confUser.getPassword())) {
            confUser.setPassword(PASSWORD_ENCODER.encode(confUser.getPassword()));
        }
        userMapper.updateByPrimaryKey(confUser);
        return LightConfResult.ok(confUser);
    }
}
