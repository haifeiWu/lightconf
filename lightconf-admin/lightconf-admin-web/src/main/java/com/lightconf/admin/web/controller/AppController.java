package com.lightconf.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.web.controller.annotation.PermissionLimit;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 应用管理接口.
 *
 * @author wuhf
 * @date 2018/02/24
 */
@Controller
@RequestMapping("/app")
public class AppController extends BaseController {

    @Autowired
    AppService appService;

    @RequestMapping("")
    @PermissionLimit
    public String index(Model model) {
        List<AppWithBLOBs> appList = appService.getAllApp();
        appList.stream().forEach(app -> {
            if (app.getIsConnected()) {
                app.setIsConnectedStr("yes");
            } else {
                app.setIsConnectedStr("no");
            }
        });
        model.addAttribute("list", appList);
        LOGGER.info("method index return value is : {}", JSON.toJSONString(appList));
        return "app/app.index";
    }

    @RequestMapping("/add_app")
    @ResponseBody
    public LightConfResult addApp(AppWithBLOBs app) {
        LOGGER.info("add application , the params is : {}", JSON.toJSONString(app));
        LightConfResult result = appService.addApp(app);
        LOGGER.info("method addApp return value is : {}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/update_app")
    @ResponseBody
    public LightConfResult updateApp(AppWithBLOBs app) {
        LOGGER.info("update application , the params is : {}", JSON.toJSONString(app));
        LightConfResult result = appService.updateAppWithBLOBs(app);
        LOGGER.info("method updateApp return value is : {}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/delete_app")
    @ResponseBody
    public LightConfResult deleteApp(String appId) {
        LOGGER.info("delete application , the appId is : {}", appId);
        LightConfResult result = appService.deleteApp(appId);
        LOGGER.info("method deleteApp return value is : {}", JSON.toJSONString(result));
        return result;
    }
}
