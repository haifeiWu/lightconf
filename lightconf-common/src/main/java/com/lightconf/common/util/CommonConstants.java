package com.lightconf.common.util;

/**
 * 通用常量.
 *
 * @author wuhf
 * @date 2018/02/29
 */
public class CommonConstants {

    public static final int HEAD_LENGTH = 4;
    public static final int RECONNECT = 3;

    /**
     * 核心线程池的个数.
     */
    public static final int CORE_POOL_SIZE = 3;
    public static final int MAX_POOL_SIZE = 10;
    public static final int THREAD_KEEP_ALIVE_TIME = 10;

    public static final int RETRY_DELAY = 10;

    public static final String CONF_TYPE_ADD = "ADD";
    public static final String CONF_TYPE_DELETE = "DELETE";
    public static final String CONF_TYPE_UPDATE = "UPDATE";
    public static final String CONF_TYPE_SEND_OUT = "SEND_OUT";
    public static final String CONF_TYPE_UPLOAD = "UPLOAD";
}
