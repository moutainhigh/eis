package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.domain.ValidateCache;

public interface ValidateCacheDao {

	List<ValidateCache> initValidateCacheData() throws DataAccessException;
		

}
