package com.lightconf.admin.dal.dao.rest;

import com.lightconf.common.util.LightConfResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * @author wujianbo
 */
@Repository public class TokenApi {

    private static Logger logger = Logger.getLogger(TokenApi.class);

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    public LightConfResult validateToken(String token) {

        return null;
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("token", token);
//        String doGet = HttpTookit
//                .doGet(QuarkShareApiConstants.apiBasePath + QuarkShareApiConstants.validate_token,
//                        params);
//        logger.info("request url : " + QuarkShareApiConstants.apiBasePath
//                + QuarkShareApiConstants.validate_token + " ,params : " + HttpTookit
//                .getParams(params) + " ,result : " + doGet);
//        return JSON.parseObject(doGet, new TypeReference<ResultCode<Map<String, Long>>>() {
//        });
    }

    /**
     * 登录 获取token
     *
     * @param userName
     * @param password
     * @return
     */
    public LightConfResult login(String userName, String password) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("userName", userName);
//        params.put("password", password);
//        String doGet = HttpTookit
//                .doGet(QuarkShareApiConstants.apiBasePath + QuarkShareApiConstants.login, params);
//        logger.info(
//                "request url : " + QuarkShareApiConstants.apiBasePath + QuarkShareApiConstants.login
//                        + " ,params : " + HttpTookit.getParams(params) + " ,result : " + doGet);
//        return JSON.parseObject(doGet, new TypeReference<ResultCode<String>>() {
//        });
        return null;
    }

    /**
     * 登出 销毁token
     *
     * @param token
     * @return
     */
    public LightConfResult logout(String token) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("token", token);
//        String doGet = HttpTookit
//                .doGet(QuarkShareApiConstants.apiBasePath + QuarkShareApiConstants.logout, params);
//        logger.info("request url : " + QuarkShareApiConstants.apiBasePath
//                + QuarkShareApiConstants.logout + ", params : " + HttpTookit.getParams(params)
//                + ", result : " + doGet);
//        return JSON.parseObject(doGet, new TypeReference<ResultCode<String>>() {
//        });
        return null;
    }

}
