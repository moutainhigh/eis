package com.maicard.wpt.dao.mybatis;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.wpt.dao.WeixinMsgDao;
import com.maicard.wpt.domain.WeixinMsg;

@Repository
public class WeixinMsgDaoImpl extends BaseDao implements WeixinMsgDao{
	
	public int insert(WeixinMsg message) throws DataAccessException {
		return getSqlSessionTemplate().insert("com.maicard.wpt.sql.WeixinMsg.insert", message);
	}

}
