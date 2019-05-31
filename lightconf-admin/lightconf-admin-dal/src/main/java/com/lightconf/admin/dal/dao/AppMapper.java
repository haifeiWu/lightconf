package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.App;
import com.lightconf.admin.model.dataobj.AppExample;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppMapper {
    long countByExample(AppExample example);

    int deleteByExample(AppExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppWithBLOBs record);

    int insertSelective(AppWithBLOBs record);

    List<AppWithBLOBs> selectByExampleWithBLOBs(AppExample example);

    List<App> selectByExample(AppExample example);

    AppWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppWithBLOBs record, @Param("example") AppExample example);

    int updateByExampleWithBLOBs(@Param("record") AppWithBLOBs record, @Param("example") AppExample example);

    int updateByExample(@Param("record") App record, @Param("example") AppExample example);

    int updateByPrimaryKeySelective(AppWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AppWithBLOBs record);

    int updateByPrimaryKey(App record);

    AppWithBLOBs selectForUpdate(Integer id);
}