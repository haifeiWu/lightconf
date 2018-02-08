package com.lightconf.admin.dao;


import com.lightconf.admin.core.model.XxlConfNode;

import java.util.List;


/**
 * 配置
 * @author xuxueli
 */
public interface IXxlConfNodeDao {

	public List<XxlConfNode> pageList(int offset, int pagesize, String nodeGroup, String nodeKey);
	public int pageListCount(int offset, int pagesize, String nodeGroup, String nodeKey);

	public int deleteByKey(String nodeGroup, String nodeKey);

	public void insert(XxlConfNode xxlConfNode);

	public XxlConfNode selectByKey(String nodeGroup, String nodeKey);

	public int update(XxlConfNode xxlConfNode);
	
}
