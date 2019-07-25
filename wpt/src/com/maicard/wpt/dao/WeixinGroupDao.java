package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.WeixinGroupCriteria;
import com.maicard.wpt.domain.WeixinGroup;


public interface WeixinGroupDao {
	int insert(WeixinGroup weixinGroup) throws DataAccessException;

	int update(WeixinGroup weixinGroup) throws DataAccessException;

	int delete(long weixinGroupId) throws DataAccessException;

	WeixinGroup select(long weixinGroupId) throws DataAccessException;
	
	List<WeixinGroup> list(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException;
	
	List<WeixinGroup> listOnPage(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException;
	
	int count(WeixinGroupCriteria weixinGroupCriteria) throws DataAccessException;
}
