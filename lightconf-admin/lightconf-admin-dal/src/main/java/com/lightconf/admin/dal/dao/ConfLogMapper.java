package com.lightconf.admin.dal.dao;

import com.lightconf.admin.model.dataobj.ConfLog;
import com.lightconf.admin.model.dataobj.ConfLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConfLogMapper {
    long countByExample(ConfLogExample example);

    int deleteByExample(ConfLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ConfLog record);

    int insertSelective(ConfLog record);

    List<ConfLog> selectByExample(ConfLogExample example);

    ConfLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ConfLog record, @Param("example") ConfLogExample example);

    int updateByExample(@Param("record") ConfLog record, @Param("example") ConfLogExample example);

    int updateByPrimaryKeySelective(ConfLog record);

    int updateByPrimaryKey(ConfLog record);

    ConfLog selectForUpdate(Integer id);
}