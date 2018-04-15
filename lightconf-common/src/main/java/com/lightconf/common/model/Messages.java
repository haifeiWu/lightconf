package com.lightconf.common.model;

/**
 * @author wuhf
 * @date 2018/02/23
 */
public class Messages {
    public static int SUCCESS_CODE = 200;
    public static String SUCCESS_MSG = "success";

    public static int MISSING_INPUT_CODE = 10000;
    public static String MISSING_INPUT_MSG = "missing required input params";

    public static int INPUT_ERROR_CODE = 10001;
    public static String INPUT_ERROR_MSG = "input error";

    public static int SERVER_ERROR_CODE = 500;
    public static String SERVER_ERROR_MSG = "server error";

    public static int API_ERROR_CODE = 90000;
    public static String API_ERROR_MSG = "接口繁忙，请稍后重试";
}
