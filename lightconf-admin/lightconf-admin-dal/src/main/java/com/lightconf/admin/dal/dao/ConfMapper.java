package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.Conf;
import com.lightconf.admin.model.dataobj.ConfExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ConfMapper {
    long countByExample(ConfExample example);

    int deleteByExample(ConfExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Conf record);

    int insertSelective(Conf record);

    List<Conf> selectByExample(ConfExample example);

    Conf selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Conf record, @Param("example") ConfExample example);

    int updateByExample(@Param("record") Conf record, @Param("example") ConfExample example);

    int updateByPrimaryKeySelective(Conf record);

    int updateByPrimaryKey(Conf record);

    Conf selectForUpdate(Integer id);
}