package com.lightconf.common.util;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 心跳检测的消息类型.
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(typeName = "pingMsg")
public class PingMsg extends BaseMsg {
    public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
