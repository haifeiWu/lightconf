package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.AppConf;
import com.lightconf.admin.model.dataobj.AppConfExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppConfMapper {
    long countByExample(AppConfExample example);

    int deleteByExample(AppConfExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppConf record);

    int insertSelective(AppConf record);

    List<AppConf> selectByExample(AppConfExample example);

    AppConf selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppConf record, @Param("example") AppConfExample example);

    int updateByExample(@Param("record") AppConf record, @Param("example") AppConfExample example);

    int updateByPrimaryKeySelective(AppConf record);

    int updateByPrimaryKey(AppConf record);

    AppConf selectForUpdate(Integer id);
}