package com.maicard.common.dao.ibatis;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.GlobalOrderIdCriteria;
import com.maicard.common.dao.GlobalOrderIdDao;

@Repository
public class GlobalOrderIdDaoImpl extends BaseDao implements GlobalOrderIdDao {

	@Override
	public void insert(String globalOrderId) throws DataAccessException {
		getSqlSessionTemplate().insert("GlobalOrderId.insert", globalOrderId);
	}
	


	@Override
	public boolean exist(GlobalOrderIdCriteria globalOrderIdCriteria) throws DataAccessException {
		Assert.notNull(globalOrderIdCriteria, "globalOrderIdCriteria must not be null");
		
		if(((Integer) getSqlSessionTemplate().selectOne("GlobalOrderId.count", globalOrderIdCriteria)).intValue() > 0){
			return true;
		}
		return false;
	}

}
