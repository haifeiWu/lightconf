package com.lightconf.admin.web.loginservice;

import com.lightconf.admin.service.UserService;
import com.lightconf.admin.web.util.CacheUtils;
import com.lightconf.admin.web.util.CookieUtil;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * Login Service
 *
 * @author xuxueli 2018-02-04 03:25:55
 */
@Component
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_CONF_LOGIN_IDENTITY";

    @Autowired
    UserService userService;

    private String makeToken(String username, String password) {
        // md5
        String tokenTmp = DigestUtils.md5DigestAsHex(String.valueOf(username + "_" + password).getBytes());
        // md5-hex
        tokenTmp = new BigInteger(1, tokenTmp.getBytes()).toString(16);
        return tokenTmp;
    }

    public boolean login(HttpServletResponse response, String usernameParam, String passwordParam, boolean ifRemember) {

        LightConfResult result = userService.userLogin(usernameParam, passwordParam);

        if (result.getCode() == Messages.SUCCESS_CODE) {
            // do login
            String paramToken = makeToken(usernameParam, passwordParam);
            CookieUtil.set(response, LOGIN_IDENTITY_KEY, paramToken, ifRemember);
            // 登录成功，记录用户登录状态
            CacheUtils.LOGIN_STATUS.put(LOGIN_IDENTITY_KEY,paramToken);
            return true;
        }
        return false;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
    }

    public boolean ifLogin(HttpServletRequest request) {
        String loginTolen = (String) CacheUtils.LOGIN_STATUS.get(LOGIN_IDENTITY_KEY);
        String paramToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (paramToken == null || !loginTolen.equals(paramToken.trim())) {
            return false;
        }
        return true;
    }

}
