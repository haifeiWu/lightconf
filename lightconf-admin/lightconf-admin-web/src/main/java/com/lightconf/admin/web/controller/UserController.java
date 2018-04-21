package com.lightconf.admin.web.controller;

import com.lightconf.admin.service.UserService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录接口。
 * @author wuhaifei
 * @date 2018/04/21
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/login")
    public LightConfResult login(String account,String password) {
        try {
            LOGGER.info("user login,userName is : {},password is : {}",account,password);
            LightConfResult result = userService.userLogin(account,password);
            LOGGER.info("method login return value is : {}",result.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return LightConfResult.build(Messages.SERVER_ERROR_CODE,Messages.SERVER_ERROR_MSG);
        }
    }

}
