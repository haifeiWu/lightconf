package com.lightconf.admin.dao;

import com.lightconf.admin.core.model.XxlConfGroup;

import java.util.List;

/**
 * Created by xuxueli on 16/10/8.
 */
public interface IXxlConfGroupDao {
    public List<XxlConfGroup> findAll();

    public int save(XxlConfGroup xxlJobGroup);

    public int update(XxlConfGroup xxlJobGroup);

    public int remove(String groupName);

    public XxlConfGroup load(String groupName);
}
