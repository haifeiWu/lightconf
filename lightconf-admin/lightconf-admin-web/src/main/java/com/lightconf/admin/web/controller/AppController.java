package com.lightconf.admin.web.controller;

import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @PermessionLimit
    public String index(Model model, String znodeKey) {
        return "app/app.index";
    }

    @RequestMapping("/add_app")
    @PermessionLimit
    public LightConfResult addApp(AppWithBLOBs app) {
        LOGGER.info("add application , the name is : {}",app.getAppName());
        LightConfResult result = appService.addApp(app);
        return result;
    }

    @RequestMapping("/update_app")
    @PermessionLimit
    public LightConfResult updateApp(AppWithBLOBs app) {
        LOGGER.info("update application , the name is : {}",app.getAppName());
        LightConfResult result = appService.updateApp(app);
        return result;
    }

    @RequestMapping("/delete_app")
    @PermessionLimit
    public LightConfResult deleteApp(String appId) {
        LOGGER.info("delete application , the appId is : {}",appId);
        LightConfResult result = appService.deleteApp(appId);
        return result;
    }

    @RequestMapping("/get_app_list")
    @PermessionLimit
    public LightConfResult getAppList() {
        LOGGER.info("delete application , the appId is : {}");
//        LightConfResult result = appService.getAppList();
        return null;
    }
}
