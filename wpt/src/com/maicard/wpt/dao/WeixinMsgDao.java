package com.maicard.wpt.dao;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.domain.WeixinMsg;



public interface WeixinMsgDao {
	int insert(WeixinMsg message) throws DataAccessException ;
}
