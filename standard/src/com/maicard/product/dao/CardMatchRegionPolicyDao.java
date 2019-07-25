package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.CardMatchRegionPolicyCriteria;
import com.maicard.product.domain.CardMatchRegionPolicy;

public interface CardMatchRegionPolicyDao {

	void insert(CardMatchRegionPolicy cardMatchRegionPolicy) throws DataAccessException;

	int update(CardMatchRegionPolicy cardMatchRegionPolicy) throws DataAccessException;

	int delete(int cardMatchRegionPolicyId) throws DataAccessException;

	CardMatchRegionPolicy select(int cardMatchRegionPolicyId) throws DataAccessException;

	List<CardMatchRegionPolicy> list(CardMatchRegionPolicyCriteria cardMatchRegionPolicyCriteria) throws DataAccessException;
	
	List<CardMatchRegionPolicy> listOnPage(CardMatchRegionPolicyCriteria cardMatchRegionPolicyCriteria) throws DataAccessException;
	
	int count(CardMatchRegionPolicyCriteria cardMatchRegionPolicyCriteria) throws DataAccessException;

}
