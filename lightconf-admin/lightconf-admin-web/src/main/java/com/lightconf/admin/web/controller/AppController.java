package com.lightconf.admin.web.controller;

import com.lightconf.admin.web.controller.annotation.PermessionLimit;
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
    @RequestMapping("")
    @PermessionLimit
    public String index(Model model, String znodeKey) {
        return "app/app.index";
    }
}
