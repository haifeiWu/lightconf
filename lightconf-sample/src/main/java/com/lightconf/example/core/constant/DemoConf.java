package com.lightconf.example.core.constant;

import com.lightconf.core.annotaion.XxlConf;

/**
 *  测试示例（可删除）
 *
 *  @author xuxueli
 */
public class DemoConf {

	@XxlConf("default.key02")
	public String paramByAnno;

	public String paramByXml;

	public void setParamByXml(String paramByXml) {
		this.paramByXml = paramByXml;
	}

}
