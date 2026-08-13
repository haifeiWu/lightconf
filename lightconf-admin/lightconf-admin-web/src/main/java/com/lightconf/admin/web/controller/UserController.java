package com.lightconf.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lightconf.admin.model.dataobj.User;
import com.lightconf.admin.service.UserService;
import com.lightconf.admin.web.controller.annotation.PermissionLimit;
import com.lightconf.common.util.LightConfResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关接口.
 *
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
    public LightConfResult login(String account, String password) {
        LOGGER.info("user login,userName is : {}", account);
        LightConfResult result = userService.userLogin(account, password);
        LOGGER.info("method login return value is : {}", result.toString());
        return result;
    }

    @RequestMapping("")
    @PermissionLimit()
    public String index(Model model) {
        LOGGER.info(">>>>>> /user/index");
        return "user/user.index";
    }

    /**
     * add
     *
     * @return
     */
    @RequestMapping("/add")
    @PermissionLimit()
    @ResponseBody
    public LightConfResult addUser(User confUser) {
        LOGGER.info(">>>>>> params is :{}", JSON.toJSONString(confUser));
        return userService.addUser(confUser);
    }

    /**
     * delete
     *
     * @return
     */
    @RequestMapping("/delete")
    @PermissionLimit()
    @ResponseBody
    public LightConfResult deleteUser(HttpServletRequest request, String username) {
        LOGGER.info(">>>>>> params is :{}", username);
        return userService.deleteUser(username);
    }

    /**
     * update
     *
     * @return
     */
    @RequestMapping("/update")
    @PermissionLimit()
    @ResponseBody
    public LightConfResult update(HttpServletRequest request, User confUser) {
        LOGGER.info(">>>>>> params is :{}", JSON.toJSONString(confUser));
        return userService.updateUser(confUser);
    }
}
