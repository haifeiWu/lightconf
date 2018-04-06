package com.lightconf.admin.web.controller;

import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.AppService;
import com.lightconf.admin.service.ConfService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import com.lightconf.core.annotaion.LightConf;
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
 * 配置管理
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
        LightConfResult result = appService.getAppConf(appId);
        LOGGER.info("method deleteApp return value is : {}",result.toString());
        List<Conf> confList = (List<Conf>) result.getContent();
        model.addAttribute("list", confList);
        model.addAttribute("appId",appId);
        return "conf/conf.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public LightConfResult pageList(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "10") int length, String appId) {
        LOGGER.info("get apps config list");
        LightConfResult result = appService.getAppConfByPage(start,length,appId);
        return result;
    }

    /**
     * get
     *
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public LightConfResult delete(String nodeGroup, String nodeKey) {
        LOGGER.info("delete config , the key is : {}",nodeKey);
        return confService.deleteByKey(nodeKey);
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
        LightConfResult result;
        try {
            LOGGER.info("add conf key is : {} , appId is :{}",conf.getConfKey(),appId);
            result = confService.add(conf,appId);
            LOGGER.info("method add conf return value is :{}",result.toString());
            return result;
        } catch (Exception e) {
            return LightConfResult.build(Messages.INPUT_ERROR_CODE,Messages.MISSING_INPUT_MSG);
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
    public LightConfResult update(Conf conf,String appUuid) {
        LOGGER.info("update conf key is : {}",conf.getConfKey());
        return confService.update(conf,appUuid);
    }
}
