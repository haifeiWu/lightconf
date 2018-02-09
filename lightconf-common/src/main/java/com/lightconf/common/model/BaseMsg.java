package com.lightconf.common.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;

/**
 * 必须实现序列,serialVersionUID 一定要有.
 *
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(seeAlso = {AskMsg.class,LoginMsg.class,ReplyMsg.class,PingMsg.class})
public abstract class BaseMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private MsgType type;
    /**
     * 必须唯一，否者会出现channel调用混乱.
     */
    private String clientId;

    private String requestId;

    private String data;

    /**
     * 初始化客户端id.
     */
    public BaseMsg() {
        this.clientId = Constants.getClientId();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
