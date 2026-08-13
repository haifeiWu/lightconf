package com.lightconf.common.util;

import com.lightconf.common.model.Messages;

/**
 * 统一响应结构。
 *
 * @author whfstudio
 * @date 2017/11/30
 */
public class LightConfResult {

    private int code;
    private String msg;
    private Object content;

    public static LightConfResult build(Integer status, String msg, Object data) {
        return new LightConfResult(status, msg, data);
    }

    public static LightConfResult ok(Object data) {
        return new LightConfResult(data);
    }

    public static LightConfResult ok() {
        return new LightConfResult(null);
    }

    public static LightConfResult build(Integer status, String errMsg) {
        return new LightConfResult(status, errMsg, null);
    }

    public LightConfResult(Integer code, String msg, Object content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    public LightConfResult(Object content) {
        this.code = Messages.SUCCESS_CODE;
        this.msg = Messages.SUCCESS_MSG;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LightConfResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
