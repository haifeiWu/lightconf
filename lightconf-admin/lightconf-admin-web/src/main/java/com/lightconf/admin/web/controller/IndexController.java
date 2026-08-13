package com.lightconf.admin.web.controller;


import com.lightconf.admin.web.controller.annotation.PermissionLimit;
import com.lightconf.admin.web.loginservice.LoginService;
import com.lightconf.common.util.LightConfResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author wuhf
 */
@Controller
public class IndexController extends BaseController {

    @Resource
    private LoginService loginService;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        return "redirect:/app";
    }

    @RequestMapping("/toLogin")
    @PermissionLimit(limit = false)
    public String toLogin(Model model, HttpServletRequest request) {
        if (loginService.ifLogin(request)) {
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public LightConfResult loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember) {
        // valid
        if (loginService.ifLogin(request)) {
            return LightConfResult.ok();
        }

        // param
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            LOGGER.error(">>>>>> login error : {}", "账号或密码为空");
            return LightConfResult.build(500, "账号或密码为空");
        }
        boolean ifRem = (StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember)) ? true : false;

        // do login
        boolean loginRet = loginService.login(response, userName, password, ifRem);
        if (!loginRet) {
            LOGGER.error(">>>>>> login error : {}", "账号或密码错误");
            return LightConfResult.build(500, "账号或密码错误");
        }

        return LightConfResult.ok();
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public LightConfResult logout(HttpServletRequest request, HttpServletResponse response) {
        if (loginService.ifLogin(request)) {
            loginService.logout(request, response);
        }
        LOGGER.info(">>>>>> logout success");
        return LightConfResult.ok();
    }

    @RequestMapping("/help")
    @PermissionLimit
    public String help() {
        return "help";
    }

}
