package com.lightconf.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lightconf.admin.model.dataobj.User;
import com.lightconf.admin.service.UserService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.admin.web.loginservice.LoginService;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.common.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("")
    @PermessionLimit()
    public String index(Model model){

//        List<XxlConfProject> projectList = xxlConfProjectDao.findAll();
//        model.addAttribute("projectList", projectList);

        return "user/user.index";
    }

    @RequestMapping("/pageList")
    @PermessionLimit()
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String username,
                                        int permission) {

        Map<String, Object> maps = userService.getUserList(start, length, username, permission);
        return maps;
    }

    /**
     * add
     *
     * @return
     */
    @RequestMapping("/add")
    @PermessionLimit()
    @ResponseBody
    public ResultCode addUser(User confUser){
        ResultCode<User> result = new ResultCode<>();
        try {
            LOGGER.info(">>>>>> params is :{}", JSON.toJSONString(confUser));
            result = userService.addUser(confUser);
            LOGGER.info(">>>>>> method addUser return value : {}",JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            result.setCode(Messages.API_ERROR_CODE);
            result.setMsg(Messages.API_ERROR_MSG);
        }
        return result;
    }

    /**
     * delete
     *
     * @return
     */
    @RequestMapping("/delete")
    @PermessionLimit()
    @ResponseBody
    public ResultCode deleteUser(HttpServletRequest request, String username){
        ResultCode<User> result = new ResultCode<>();
        try {
            LOGGER.info(">>>>>> params is :{}", username);
            result = userService.deleteUser(username);
            LOGGER.info(">>>>>> method addUser return value : {}",JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            result.setCode(Messages.API_ERROR_CODE);
            result.setMsg(Messages.API_ERROR_MSG);
        }
        return result;
    }

    /**
     * update
     *
     * @return
     */
    @RequestMapping("/update")
    @PermessionLimit()
    @ResponseBody
    public ResultCode update(HttpServletRequest request, User confUser){
        ResultCode<User> result = new ResultCode<>();
        try {
            LOGGER.info(">>>>>> params is :{}", JSON.toJSONString(confUser));
            result = userService.updateUser(confUser);
            LOGGER.info(">>>>>> method addUser return value : {}",JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            result.setCode(Messages.API_ERROR_CODE);
            result.setMsg(Messages.API_ERROR_MSG);
        }
        return result;
    }

    @RequestMapping("/updatePermissionProjects")
    @ResponseBody
    public LightConfResult updatePermissionProjects(HttpServletRequest request,
                                                    String username,
                                                    @RequestParam(required = false) String[] permissionProjects){

        String permissionProjectsStr = StringUtils.join(permissionProjects, ",");
//        XxlConfUser existUser = xxlConfUserDao.load(username);
//        if (existUser == null) {
//            return new ReturnT<String>(ReturnT.FAIL.getCode(), "参数非法");
//        }
//        existUser.setPermissionProjects(permissionProjectsStr);
//        xxlConfUserDao.update(existUser);
//
//        return ReturnT.SUCCESS;
        return null;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public LightConfResult updatePwd(HttpServletRequest request, String password){

//        // new password(md5)
//        if (StringUtils.isBlank(password)){
//            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
//        }
//        if (!(password.length()>=4 && password.length()<=100)) {
//            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
//        }
//        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
//
//        // update pwd
//        XxlConfUser loginUser = (XxlConfUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
//
//        XxlConfUser existUser = xxlConfUserDao.load(loginUser.getUsername());
//        existUser.setPassword(md5Password);
//        xxlConfUserDao.update(existUser);
//
//        return ReturnT.SUCCESS;
        return null;
    }
}
