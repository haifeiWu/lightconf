package com.lightconf.common.model;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 请求类型的消息
 *
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(typeName = "askMsg")
public class AskMsg extends BaseMsg {
    public AskMsg() {
        super();
        setType(MsgType.ASK);
    }

    private AskParams params;

    public AskParams getParams() {
        return params;
    }

    public void setParams(AskParams params) {
        this.params = params;
    }
}
