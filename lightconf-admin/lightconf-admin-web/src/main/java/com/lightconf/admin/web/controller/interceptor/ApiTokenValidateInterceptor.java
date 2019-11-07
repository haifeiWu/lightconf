package com.lightconf.admin.web.controller.interceptor;

import com.lightconf.admin.dal.dao.rest.TokenApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证token
 *
 * @author wujianbo
 */
public class ApiTokenValidateInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenApi tokenApi;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        String authorization1 = request.getParameter("Authorization");
        if (StringUtils.isBlank(authorization) && StringUtils.isBlank(authorization1)) {
            //没有token
//            ResultCode<String> resultCode = new ResultCode<>();
//            resultCode.setCode(Messages.API_AUTHENTICATION_FAILED_CODE);
//            resultCode.setMsg(Messages.API_AUTHENTICATION_FAILED_MSG);
//            sendFailRes(resultCode, response);
            return false;
        }
        String token = StringUtils.isBlank(authorization) ? authorization1 : authorization;
//        ResultCode<Map<String, Long>> resultCode = tokenApi.validateToken(token);
//        if (resultCode.getCode().equals(Messages.SUCCESS_CODE)){
//            UserInfoRedis userInfo = SpringContextHolder.getBean(UserInfoRedis.class);
//            userInfo.setUserId(Integer.parseInt(resultCode.getData().get("userId")+""));
//            userInfo.setIncId(Integer.parseInt(resultCode.getData().get("incId")+""));
//            return true;
//        } else {
//            logger.warn("rest 验证失败{}", JsonMapper.toJsonString(resultCode));
//            sendFailRes(resultCode, response);
//            return false;
//        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

//    private void sendFailRes(ResultCode resultCode, HttpServletResponse response) throws IOException {
//        response.reset();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//        response.setStatus(HttpStatus.SC_FORBIDDEN);
//        response.getWriter().print(JsonMapper.toJsonString(resultCode));
//    }
}
