package com.lightconf.core.env;

/**
 * light conf environment.
 *
 * @author wuhf
 * @date 2018/02/11
 */
public class Environment {

	// ---------------------- prop ----------------------
	/**
	 * default prop
	 */
	public static final String LIGHT_CONF_PROP = "light-conf.properties";

	/**
	 * default prop
	 */
	public static final String LIGHT_CONF = "light-conf-cache.properties";

	/**
	 * prop file location, if not empty this file will be replaced with this disk file, like "file:/data/webapps/light-conf.properties" or "light-conf02.properties"
	 */
	public static final String LIGHT_PROP_FILE_LOCATION = "light.conf.prop.file.location";

	/**
	 * lightconf host.
	 */
	public static final String LIGHT_CONF_HOST = "light.conf.host";

	public static final String LIGHT_CONF_PORT = "light.conf.port";

	/**
	 * 应用的唯一标示uuid.
	 */
	public static final String APPLICATION_UUID = "application.uuid";

	/**
	 * 应用密钥，登录时用于身份鉴权.
	 */
	public static final String LIGHT_CONF_SECRET = "light.conf.secret";

	/**
	 * 本地缓存文件名（用于定时持久化 + 启动加载容灾）.
	 */
	public static final String LIGHT_CONF_CACHE = "light-conf-cache.properties";

	/**
	 * 本地缓存文件路径覆盖，如 -Dlight.conf.cache.file=/data/light-conf-cache.properties.
	 */
	public static final String LIGHT_CACHE_FILE = "light.conf.cache.file";

}

