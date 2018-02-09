package com.lightconf.common.model;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(typeName = "replyMsg")
public class ReplyMsg extends BaseMsg {
    public ReplyMsg() {
        super();
        setType(MsgType.REPLY);
    }
    private ReplyBody body;

    public ReplyBody getBody() {
        return body;
    }

    public void setBody(ReplyBody body) {
        this.body = body;
    }
}
