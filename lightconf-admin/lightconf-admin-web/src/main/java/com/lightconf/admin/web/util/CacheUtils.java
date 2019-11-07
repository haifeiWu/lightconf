package com.lightconf.admin.web.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuhaifei 2019-06-04
 */
public class CacheUtils {
    public static final Map<String, Object> LOGIN_STATUS = new ConcurrentHashMap<>();
}
