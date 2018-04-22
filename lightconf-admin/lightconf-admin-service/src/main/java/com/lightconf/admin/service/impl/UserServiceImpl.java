package com.lightconf.admin.service.impl;

import com.lightconf.admin.dal.dao.UserMapper;
import com.lightconf.admin.model.dataobj.User;
import com.lightconf.admin.model.dataobj.UserExample;
import com.lightconf.admin.service.UserService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuhf
 * @date 2018/04/21
 */
@Transactional(rollbackFor = Exception.class)
@Service
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
                return LightConfResult.build(Messages.SUCCESS_CODE,Messages.SUCCESS_MSG,userList.get(0));
            }
        }

        return LightConfResult.build(Messages.USER_LOGIN_ERROR_CODE,Messages.USER_LOGIN_ERROR_MSG);
    }
}
