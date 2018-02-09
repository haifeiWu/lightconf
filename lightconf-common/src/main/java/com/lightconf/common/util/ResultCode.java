package com.lightconf.common.util;

import com.lightconf.common.model.Messages;

public class ResultCode<T> {
    private String msg;
    private Integer code;
    private T data;


    public ResultCode() {
        this.msg = Messages.SUCCESS_MSG;
        this.code = Messages.SUCCESS_CODE;
        this.data = null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

