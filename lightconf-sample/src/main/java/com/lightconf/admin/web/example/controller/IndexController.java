package com.lightconf.admin.web.example.controller;

import com.lightconf.core.LightConfClient;
import com.lightconf.core.listener.LightConfListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuxueli 2018-02-04 01:27:30
 */
@Controller
public class IndexController {
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	protected static String key01;

	static {
		/**
		 * 配置变更监听示例：可开发Listener逻辑，监听配置变更事件；可据此实现动态刷新JDBC连接池等高级功能；
		 */
		LightConfClient.addListener("key01", new LightConfListener() {
			@Override
			public void onChange(String key, String value) throws Exception {
				key01 = value;
				logger.info("配置变更事件通知：{}={}", key, value);
			}
		});
	}

	@RequestMapping("")
	public String index(Model model){

        /**
         * 方式1: XML占位符方式
         *
		 * 		- 参考 "applicationcontext-xxl-conf.xml" 中 "DemoConf.paramByXml" 属性配置 ""；示例代码 "<property name="paramByXml" value="${default.key01}" />"；
		 * 		- 用法：占位符方式 "${default.key01}"，支持嵌套占位符；
		 * 		- 优点：
		 * 			- 配置从配置中心自动加载；
		 * 			- 存在LocalCache，不用担心性能问题；
		 * 			- 支持嵌套占位符；
		 * 		- 缺点：不支持支持动态推送更新
         *
         */
		model.addAttribute("key01",key01);
		model.addAttribute("key02", LightConfClient.get("key1"));

		System.err.println(">>>>>>注解 " + key01);

		/**
		 * 方式2: “@XxlConf”注解方式
		 *
		 * 		- 参考 "IndexController.paramByAnno" 属性配置；示例代码 "@XxlConf("default.key02") public String paramByAnno;"；
		 * 		- 用法：对象Field上加注解 ""@XxlConf("default.key02")"，支持设置默认值，支持设置是否开启动态刷新；
		 * 		- 优点：
		 * 			- 配置从配置中心自动加载；
		 * 			- 存在LocalCache，不用担心性能问题；
		 * 			- 支持设置配置默认值；
		 * 			- 支持设置是否开启动态刷新;
		 */
//		model.addAttribute("key02", demoConf.paramByAnno);

        /**
         * 方式3: API方式
         *
		 * 		- 参考 "IndexController" 中 "XxlConfClient.get("key", null)" 即可；
		 * 		- 用法：代码中直接调用API即可，示例代码 ""XxlConfClient.get("key", null)"";
		 * 		- 优点：
		 * 			- 配置从配置中心自动加载；
		 * 			- 支持动态推送更新；
		 * 			- 支持多数据类型；
         */
		model.addAttribute("defaultkey01", LightConfClient.get("default.key01"));

		return "index";
	}
}
