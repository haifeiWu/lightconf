package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.Conf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfMapper2 {

    /**
     * 获取应用的配置信息。
     *
     * @param appId
     * @return
     */
    List<Conf> getAppConf(int appId);

    /**
     * 获取应用的配置信息，分页实现
     *
     * @param start
     * @param length
     * @param appId
     * @param confKey
     * @return
     */
    List<Conf> getAppConfByPage(@Param("start") int start, @Param("length") int length,
                                @Param("appId") Integer appId, @Param("confKey") String confKey);

    /**
     * 统计条数.
     *
     * @param start
     * @param length
     * @param appId
     * @param confKey
     * @return
     */
    int countAppConfByPage(@Param("start") int start, @Param("length") int length,
                           @Param("appId") Integer appId, @Param("confKey") String confKey);
}