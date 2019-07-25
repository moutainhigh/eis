package com.maicard.product.service;



import com.maicard.product.criteria.ValidateCacheCriteria;
import com.maicard.product.domain.ValidateCache;

public interface ValidateCacheService {

	void insert(ValidateCache validateCache);

	void delete(ValidateCache validateCache);
	
	ValidateCache select(ValidateCacheCriteria validateCacheCriteria);

	int initValidateCacheData();


}
