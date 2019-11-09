package com.lightconf.core;

import com.lightconf.core.core.LightConfLocalCacheConf;
import com.lightconf.core.core.LightConfPropConf;
import com.lightconf.core.exception.LightConfException;
import com.lightconf.core.listener.LightConfListener;
import com.lightconf.core.listener.LightConfListenerFactory;

/**
 * lightconf client
 * @author wuhf
 */
public class LightConfClient {

	/**
	 * get conf
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static String get(String key, String defaultVal) {
		// level 1: prop conf
		String propConf = LightConfPropConf.get(key);
		if (propConf != null) {
			return propConf;
		}

		// level 2: local cache
		LightConfLocalCacheConf.CacheNode cacheNode = LightConfLocalCacheConf.get(key);
		if (cacheNode != null) {
			return cacheNode.getValue();
		}

		return defaultVal;
	}

	/**
	 * get conf (string)
	 *
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * get conf (int)
	 *
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new LightConfException("config key [" + key + "] does not exist");
		}
		return Integer.valueOf(value);
	}

	/**
	 * get conf (long)
	 *
	 * @param key
	 * @return
	 */
	public static long getLong(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new LightConfException("config key [" + key + "] does not exist");
		}
		return Long.valueOf(value);
	}

	/**
	 * get conf (boolean)
	 *
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		String value = get(key, null);
		if (value == null) {
			throw new LightConfException("config key [" + key + "] does not exist");
		}
		return Boolean.valueOf(value);
	}

	/**
	 * add listener with xxl conf change
	 *
	 * @param key
	 * @param lightConfListener
	 * @return
	 */
	public static boolean addListener(String key, LightConfListener lightConfListener) {
		return LightConfListenerFactory.addListener(key, lightConfListener);
	}


}
