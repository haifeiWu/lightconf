package com.lightconf.admin.web.controller;

import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.service.ConfService;
import com.lightconf.admin.web.controller.annotation.PermessionLimit;
import com.lightconf.common.util.LightConfResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 配置管理
 * @author whfstudio
 */
@Controller
@RequestMapping("/conf")
public class ConfController {

    @Resource
    private ConfService confService;

    @RequestMapping("")
    @PermessionLimit
    public String index(Model model, String znodeKey){
        return "conf/conf.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String nodeGroup, String nodeKey) {
        return confService.pageList(start, length, nodeGroup, nodeKey);
    }

    /**
     * get
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public LightConfResult delete(String nodeGroup, String nodeKey){
        return confService.deleteByKey(nodeKey);
    }

    /**
     * create/update
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit
    public LightConfResult add(Conf conf){
        return confService.add(conf);
    }

    /**
     * create/update
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit
    public LightConfResult update(Conf conf){
        return confService.update(conf);
    }
}
