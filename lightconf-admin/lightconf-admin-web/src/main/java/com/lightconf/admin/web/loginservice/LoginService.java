package com.lightconf.admin.web.loginservice;

import com.lightconf.admin.service.UserService;
import com.lightconf.admin.web.util.CookieUtil;
import com.lightconf.admin.web.util.SessionStore;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login Service
 *
 * @author xuxueli 2018-02-04 03:25:55
 */
@Component
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "LIGHTCONF_LOGIN_IDENTITY";

    @Autowired
    UserService userService;

    @Autowired
    private SessionStore sessionStore;

    public boolean login(HttpServletResponse response, String usernameParam, String passwordParam, boolean ifRemember) {

        LightConfResult result = userService.userLogin(usernameParam, passwordParam);

        if (result.getCode() == Messages.SUCCESS_CODE) {
            // 生成随机 token 作为登录凭证
            String paramToken = sessionStore.createToken();
            CookieUtil.set(response, LOGIN_IDENTITY_KEY, paramToken, ifRemember);
            return true;
        }
        return false;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (StringUtils.isNotBlank(token)) {
            sessionStore.remove(token);
        }
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
    }

    public boolean ifLogin(HttpServletRequest request) {
        String paramToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        return StringUtils.isNotBlank(paramToken) && sessionStore.isValid(paramToken);
    }

}
