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
	 * prop file location, if not empty this file will be replaced with this disk file, like "file:/data/webapps/xxl-conf.properties" or "xxl-conf02.properties"
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

}

