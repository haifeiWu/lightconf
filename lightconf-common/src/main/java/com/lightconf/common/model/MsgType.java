package com.lightconf.common.model;

/**
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
public enum  MsgType {
    PING,ASK,REPLY,
    /**
     * 连接初次建立，客户端登录.
     */
    LOGIN,REQUEST,

    /**
     * 对应用配置的增删改操作
     */
    PUSH_CONF,
    /**
     * 客户端连接成功，配置下发
     */
    SEND_OUT,

    /**
     * 客户端连接成功，客户端配置上传
     */
    UPLOAD_CONF
}
