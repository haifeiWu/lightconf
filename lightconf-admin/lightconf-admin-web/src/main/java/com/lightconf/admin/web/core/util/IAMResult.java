package com.lightconf.admin.web.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 自定义响应结构.
 * @author wuhf0703
 * @date 2017/11/30
 */
public class IAMResult {

    /**
     * 定义jackson对象.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态.
     */
    private Integer status;

    /**
     * 响应消息.
     */
    private String errMsg;

    /**
     * 响应中的数据.
     */
    private Object rows;

    private Long total;

    /**
     * 构建一个消息体
     *
     * @param status
     * @param msg
     * @param data
     * @return
     */
    public static IAMResult build(Integer status, String msg, Object data) {
        return new IAMResult(status, msg, data);
    }

    /**
     * 构建一个带分页条数的消息体.
     * @param status
     * @param msg
     * @param data
     * @param total
     * @return
     */
    public static IAMResult build(Integer status, String msg, Object data, Long total) {
        return new IAMResult(status, msg, data, total);
    }

    /**
     * 返回成功的结果
     *
     * @param data
     * @return
     */
    public static IAMResult ok(Object data) {
        return new IAMResult(data);
    }

    public static IAMResult ok() {
        return new IAMResult(null);
    }

    public IAMResult() {

    }

    public static IAMResult build(Integer status, String errMsg) {
        return new IAMResult(status, errMsg, null);
    }

    public IAMResult(Integer status, String errMsg, Object rows) {
        this.status = status;
        this.errMsg = errMsg;
        this.rows = rows;
    }

    public IAMResult(Integer status, String errMsg, Object rows, Long total) {
        this.status = status;
        this.errMsg = errMsg;
        this.rows = rows;
        this.total = total;
    }

    public IAMResult(Object rows) {
//        this.status = Messages.SUCCESS_CODE;
//        this.errMsg = Messages.SUCCESS_MSG;
        this.rows = rows;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * 将json结果集转化为TaotaoResult对象
     *
     * @param jsonData
     *            json数据
     * @param clazz
     *            TaotaoResult中的object类型
     * @return
     */
    public static IAMResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, IAMResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static IAMResult format(String json) {
        try {
            return MAPPER.readValue(json, IAMResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static IAMResult formatToIAMResult(String json) {
//        try {
//            JSONObject jsonObject = JSON.parseObject(json);
//            if (Messages.SHARE_SUCCESS_CODE == jsonObject.getInteger("code")) {
//                if (null != jsonObject.get("data")) {
//                    Object data = jsonObject.get("data");
//                    return IAMResult.build(Messages.SUCCESS_CODE,Messages.SUCCESS_MSG,data);
//                }
//                return IAMResult.ok();
//            } else {
//                return IAMResult.build(Messages.IAM_FAIL_CODE,jsonObject.getString("msg"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * Object是集合转化
     *
     * @param jsonData
     *            json数据
     * @param clazz
     *            集合中的类型
     * @return
     */
    public static IAMResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "IAMResult{" + "status=" + status + ", errMsg='" + errMsg + '\'' + ", rows=" + rows
                + ", total=" + total + '}';
    }
}
