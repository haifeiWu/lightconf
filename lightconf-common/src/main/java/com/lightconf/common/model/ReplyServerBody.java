package com.lightconf.common.model;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(typeName = "replyServerBody")
public class ReplyServerBody extends ReplyBody {
    private String serverInfo;
    public ReplyServerBody(String serverInfo) {
        this.serverInfo = serverInfo;
    }
    public String getServerInfo() {
        return serverInfo;
    }
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }
}
