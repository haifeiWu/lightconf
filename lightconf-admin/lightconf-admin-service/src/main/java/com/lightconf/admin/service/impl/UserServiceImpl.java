package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.UserMapper;
import com.lightconf.admin.model.dataobj.User;
import com.lightconf.admin.model.dataobj.UserExample;
import com.lightconf.admin.service.UserService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;

/**
 * @author wuhf
 * @date 2018/04/21
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public LightConfResult userLogin(String userName, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(userName);
        List<User> userList = userMapper.selectByExample(userExample);

        if (userList != null && userList.size() > 0) {
            User user = userList.get(0);
            if (user.getPassword().equals(password)) {
                return LightConfResult.build(Messages.SUCCESS_CODE, Messages.SUCCESS_MSG, userList.get(0));
            }
        }

        return LightConfResult.build(Messages.USER_LOGIN_ERROR_CODE, Messages.USER_LOGIN_ERROR_MSG);
    }

    @Override
    public Map<String, Object> getUserList(int start, int length, String username, int permission) {
        return null;
    }

    @Override
    public ResultCode<User> addUser(User confUser) {

        ResultCode<User> resultCode = new ResultCode();

        // valid
        if (StringUtils.isBlank(confUser.getUserName()) || StringUtils.isBlank(confUser.getPassword())) {
            resultCode.setCode(Messages.INPUT_ERROR_CODE);
            resultCode.setMsg(Messages.INPUT_ERROR_MSG);
            return resultCode;
        }
        if (!(confUser.getPassword().length() >= 4 && confUser.getPassword().length() <= 100)) {
            resultCode.setCode(Messages.INPUT_ERROR_CODE);
            resultCode.setMsg(Messages.INPUT_ERROR_MSG);
            return resultCode;
        }

        // passowrd md5
        String md5Password = DigestUtils.md5DigestAsHex(confUser.getPassword().getBytes());
        confUser.setPassword(md5Password);
        userMapper.insert(confUser);
        resultCode.setData(confUser);
        return resultCode;
    }

    @Override
    public ResultCode<User> deleteUser(String username) {
        ResultCode<User> resultCode = new ResultCode();
        if (StringUtils.isBlank(username)) {
            resultCode.setCode(Messages.INPUT_ERROR_CODE);
            resultCode.setMsg(Messages.INPUT_ERROR_MSG);
            return resultCode;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(username);
        userMapper.deleteByExample(userExample);
        return resultCode;
    }

    @Override
    public ResultCode<User> updateUser(User confUser) {


        ResultCode<User> resultCode = new ResultCode();

        // valid
        if (StringUtils.isBlank(confUser.getUserName()) || StringUtils.isBlank(confUser.getPassword())) {
            resultCode.setCode(Messages.INPUT_ERROR_CODE);
            resultCode.setMsg(Messages.INPUT_ERROR_MSG);
            return resultCode;
        }

        if (!(confUser.getPassword().length() >= 4 && confUser.getPassword().length() <= 100)) {
            resultCode.setCode(Messages.INPUT_ERROR_CODE);
            resultCode.setMsg(Messages.INPUT_ERROR_MSG);
            return resultCode;
        }

        // update password
        // passowrd md5
        if (StringUtils.isNotBlank(confUser.getPassword())) {
            String md5Password = DigestUtils.md5DigestAsHex(confUser.getPassword().getBytes());
            confUser.setPassword(md5Password);
        }
        userMapper.updateByPrimaryKey(confUser);
        resultCode.setData(confUser);
        return resultCode;
    }
}
