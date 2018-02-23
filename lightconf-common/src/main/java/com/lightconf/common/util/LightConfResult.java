package com.lightconf.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightconf.common.model.Messages;

import java.util.List;

/**
 * 自定义响应结构.
 *
 * @author whfstudio
 * @date 2017/11/30
 */
public class LightConfResult {

    /**
     * 定义jackson对象.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private int code;
    private String msg;
    private Object content;

    /**
     * 构建一个消息体
     *
     * @param status
     * @param msg
     * @param data
     * @return
     */
    public static LightConfResult build(Integer status, String msg, Object data) {
        return new LightConfResult(status, msg, data);
    }

    /**
     * 返回成功的结果
     *
     * @param data
     * @return
     */
    public static LightConfResult ok(Object data) {
        return new LightConfResult(data);
    }

    public static LightConfResult ok() {
        return new LightConfResult(null);
    }

    public static LightConfResult build(Integer status, String errMsg) {
        return new LightConfResult(status, errMsg, null);
    }

    public LightConfResult(Integer code, String msg, Object content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    public LightConfResult(Object content) {
        this.code = Messages.SUCCESS_CODE;
        this.msg = Messages.SUCCESS_MSG;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
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
    public static LightConfResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, LightConfResult.class);
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
    public static LightConfResult format(String json) {
        try {
            return MAPPER.readValue(json, LightConfResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData
     *            json数据
     * @param clazz
     *            集合中的类型
     * @return
     */
    public static LightConfResult formatToList(String jsonData, Class<?> clazz) {
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
        return "LightConfResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", content=" + content +
                '}';
    }
}
