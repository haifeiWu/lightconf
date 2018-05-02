package com.lightconf.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.service.ConfService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.core.annotaion.LightConf;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理接口。
 *
 * @author whfstudio
 */
@Controller
@RequestMapping("/conf")
public class ConfController extends BaseController{
    @Autowired
    AppService appService;

    @Autowired
    private ConfService confService;

    @RequestMapping("")
    @PermessionLimit
    public String index(Model model, String appId) {
        LOGGER.info("get app's conf , the appId is : {}",appId);
        List<AppWithBLOBs> list = appService.getAllApp();
        AppWithBLOBs project = list.get(0);
        if (appId != null) {
            for (AppWithBLOBs item: list) {
                if (item.getId().equals(Integer.valueOf(appId))) {
                    project = item;
                }
            }
        }
        model.addAttribute("appId",appId);
        model.addAttribute("ProjectList", list);
        model.addAttribute("project", project);
        return "conf/conf.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public Map<String,Object> pageList(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "10") int length, String appId,String confKey) {
        Map<String,Object> result = new HashMap<>(16);
        try {
            LOGGER.info(">>>>>>get apps config list");
            result = appService.getAppConfByPage(start,length,appId,confKey);
            LOGGER.info(">>>>>> pageList method return value is : {}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return result;
        }
    }

    /**
     * get
     *
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public LightConfResult delete(String confId,String appId) {
        try {
            LOGGER.info(">>>>>> delete config , the key is : {}",confId);
            LightConfResult result = confService.deleteById(confId,appId);
            LOGGER.info(">>>>>> delete conf method return value is : {}",JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return LightConfResult.build(Messages.SERVER_ERROR_CODE,Messages.SERVER_ERROR_MSG);
        }
    }

    /**
     * create/update
     *
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit
    public LightConfResult add(Conf conf,String appId) {
        try {
            LOGGER.info("add conf key is : {} , appId is :{}",conf.getConfKey(),appId);
            LightConfResult result = confService.add(conf,appId);
            LOGGER.info("method add conf return value is :{}",JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return LightConfResult.build(Messages.SERVER_ERROR_CODE,Messages.SERVER_ERROR_MSG);
        }
    }

    /**
     * create/update
     *
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit
    public LightConfResult update(Conf conf,String appId) {
        try {
            LOGGER.info("update conf key is : {}",conf.getConfKey());
            LightConfResult result = confService.update(conf,appId);
            LOGGER.info("method update conf return value is :{}",JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return LightConfResult.build(Messages.SERVER_ERROR_CODE,Messages.SERVER_ERROR_MSG);
        }
    }
}
