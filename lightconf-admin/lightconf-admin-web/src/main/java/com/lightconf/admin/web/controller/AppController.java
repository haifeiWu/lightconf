package com.lightconf.admin.web.controller;

import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
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
    @PermessionLimit
    public String index(Model model) {
        List<AppWithBLOBs> appList = appService.getAllApp();
		model.addAttribute("list", appList);
        return "app/app.index";
    }

    @RequestMapping("/add_app")
    @ResponseBody
    public LightConfResult addApp(AppWithBLOBs app) {
        LOGGER.info("add application , the name is : {}",app.getAppName());
        LightConfResult result = appService.addApp(app);
        LOGGER.info(result.toString());
        return result;
    }

    @RequestMapping("/update_app")
    @ResponseBody
    public LightConfResult updateApp(AppWithBLOBs app) {
        LOGGER.info("update application , the name is : {}",app.getAppName());
        LightConfResult result = appService.updateApp(app);
        return result;
    }

    @RequestMapping("/delete_app")
    @ResponseBody
    public LightConfResult deleteApp(String appId) {
        LOGGER.info("delete application , the appId is : {}",appId);
        LightConfResult result = appService.deleteApp(appId);
        LOGGER.info("method deleteApp return value is : {}",result.toString());
        return result;
    }

    @RequestMapping("/get_app_conf")
    @ResponseBody
    public LightConfResult getAppConf(String appId) {
        LOGGER.info("get app's conf , the appId is : {}",appId);
//        LightConfResult result = appService.getAppConf(appId);
//        LOGGER.info("method deleteApp return value is : {}",result.toString());
        return LightConfResult.ok();
    }

    @RequestMapping("/get_app_list")
    @ResponseBody
    public LightConfResult getAppList(@RequestParam(required = false, defaultValue = "0") int pageSize,
                                      @RequestParam(required = false, defaultValue = "10") int pageNum) {
        LOGGER.info("delete application , the appId is : {}");
        LightConfResult result = appService.getAppList(pageSize,pageNum);
        LOGGER.info("getAppList return data : {}",result);
        return result;
    }
}
