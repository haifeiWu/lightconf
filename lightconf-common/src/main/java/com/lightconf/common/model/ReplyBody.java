package com.lightconf.common.model;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;

/**
 * @author whfstudio@163.com
 * @date 2017/11/20
 */
@JSONType(seeAlso = {ReplyClientBody.class, ReplyServerBody.class})
public class ReplyBody implements Serializable {
    private static final long serialVersionUID = 1L;
}
