package com.maicard.product.dao.ibatis;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.product.dao.ValidateCacheDao;
import com.maicard.product.domain.ValidateCache;

@Repository
public class ValidateCacheDaoImpl extends BaseDao implements ValidateCacheDao {

	@Override
	public List<ValidateCache> initValidateCacheData()
			throws DataAccessException {
		return getSqlSessionTemplate().selectList("ValidateCache.initCacheFromItem");
	}

	

}
