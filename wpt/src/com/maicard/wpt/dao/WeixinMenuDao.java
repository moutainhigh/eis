package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.WeixinMenuCriteria;
import com.maicard.wpt.domain.WeixinMenu;


public interface WeixinMenuDao {
	int insert(WeixinMenu weixinMenu) throws DataAccessException;

	int update(WeixinMenu weixinMenu) throws DataAccessException;

	int delete(long weixinMenuId) throws DataAccessException;

	WeixinMenu select(long weixinMenuId) throws DataAccessException;
	
	List<WeixinMenu> list(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException;
	
	List<WeixinMenu> listOnPage(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException;
	
	int count(WeixinMenuCriteria weixinMenuCriteria) throws DataAccessException;
}
