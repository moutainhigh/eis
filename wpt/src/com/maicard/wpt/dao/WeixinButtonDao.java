package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.WeixinButtonCriteria;
import com.maicard.wpt.domain.WeixinButton;


public interface WeixinButtonDao {
	int insert(WeixinButton weixinButton) throws DataAccessException;

	int update(WeixinButton weixinButton) throws DataAccessException;

	int delete(long weixinButtonId) throws DataAccessException;

	WeixinButton select(long weixinButtonId) throws DataAccessException;
	
	List<WeixinButton> list(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException;
	
	List<WeixinButton> listOnPage(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException;
	
	int count(WeixinButtonCriteria weixinButtonCriteria) throws DataAccessException;

	void deleteByUuid(long uuid);
}
