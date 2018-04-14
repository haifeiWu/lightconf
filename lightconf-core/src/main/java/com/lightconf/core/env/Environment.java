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
//	public static final String DEFAULT_PROP = "xxl-conf.properties";

	public static final String LIGHT_CONF_PROP = "light-conf.properties";

	/**
	 * prop file location, if not empty this file will be replaced with this disk file, like "file:/data/webapps/xxl-conf.properties" or "xxl-conf02.properties"
	 */
//	public static final String PROP_FILE_LOCATION = "xxl.conf.prop.file.location";

	public static final String LIGHT_PROP_FILE_LOCATION = "light.conf.prop.file.location";

	/**
	 * zk address, as "ip1:port,ip2:port", zk地址：格式
     */
//	public static final String ZK_ADDRESS = "xxl.conf.zkserver";

	/**
	 * lightconf host.
	 */
	public static final String LIGHT_CONF_HOST = "light.conf.host";

	public static final String LIGHT_CONF_PORT = "light.conf.port";

	/**
	 * 应用的唯一标示uuid.
	 */
	public static final String APPLICATION_UUID = "application.uuid";

	// ---------------------- zk ----------------------
	/**
	 * conf data path in zk
	 */
//	public static final String CONF_DATA_PATH = "/xxl-conf";

	public static final String CONF_DATA_PATH = "/light-conf";
	
    public static final String ZK_PATH = "";
}

