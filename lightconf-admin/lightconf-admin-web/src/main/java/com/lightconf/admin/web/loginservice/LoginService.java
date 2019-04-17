package com.lightconf.admin.web.loginservice;

import com.lightconf.admin.service.UserService;
import com.lightconf.admin.web.core.util.CookieUtil;
import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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

//    @Value("${light.conf.login.username}")
//    private String username;    // can not user @Value or XML in mvc inteceptor，because inteceptor work with mvc, init before service
//
//    @Value("${light.conf.login.password}")
//    private String password;

    @Autowired
    UserService userService;

    private String makeToken(String username, String password){
        String tokenTmp = DigestUtils.md5DigestAsHex(String.valueOf(username + "_" + password).getBytes());	// md5
        tokenTmp = new BigInteger(1, tokenTmp.getBytes()).toString(16);	// md5-hex
        return tokenTmp;
    }

    public boolean login(HttpServletResponse response, String usernameParam, String passwordParam, boolean ifRemember){

        LightConfResult result = userService.userLogin(usernameParam,passwordParam);

//        lo
        if (result.getCode() == Messages.SUCCESS_CODE) {
            // do login
            String paramToken = makeToken(usernameParam, passwordParam);
            CookieUtil.set(response, LOGIN_IDENTITY_KEY, paramToken, ifRemember);
            return true;
        }

        return false;

//        String loginTolen = makeToken(username, password);
//
//        if (!loginTolen.equals(paramToken)){
//            return false;
//        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
    }

    public boolean ifLogin(HttpServletRequest request) {

//        String loginTolen = makeToken(username, password);
//        String paramToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
//
//        if (paramToken==null || !loginTolen.equals(paramToken.trim())) {
//            return false;
//        }
        return true;
    }

}
