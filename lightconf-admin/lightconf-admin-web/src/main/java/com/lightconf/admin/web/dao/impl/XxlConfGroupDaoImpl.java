package com.lightconf.admin.web.dao.impl;

import com.lightconf.admin.web.core.model.XxlConfGroup;
import com.lightconf.admin.web.dao.IXxlConfGroupDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuxueli on 16/10/8.
 */
//@Repository
public class XxlConfGroupDaoImpl implements IXxlConfGroupDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<XxlConfGroup> findAll() {
        return sqlSessionTemplate.selectList("XxlConfGroupMapper.findAll");
    }

    @Override
    public int save(XxlConfGroup xxlJobGroup) {
        return sqlSessionTemplate.insert("XxlConfGroupMapper.save", xxlJobGroup);
    }

    @Override
    public int update(XxlConfGroup xxlJobGroup) {
        return sqlSessionTemplate.update("XxlConfGroupMapper.update", xxlJobGroup);
    }

    @Override
    public int remove(String groupName) {
        return sqlSessionTemplate.delete("XxlConfGroupMapper.remove", groupName);
    }

    @Override
    public XxlConfGroup load(String groupName) {
        return sqlSessionTemplate.selectOne("XxlConfGroupMapper.load", groupName);
    }
}
