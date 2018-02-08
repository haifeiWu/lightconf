package com.lightconf.common.util;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 登录验证类型的消息
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(typeName = "loginMsg")
public class LoginMsg extends BaseMsg {
    private String userName;
    private String password;
    public LoginMsg() {
        super();
        setType(MsgType.LOGIN);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
