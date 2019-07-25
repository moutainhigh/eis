package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ProductPartnerRelationCriteria;
import com.maicard.product.domain.ProductPartnerRelation;

public interface ProductPartnerRelationDao {
	int insert(ProductPartnerRelation productPartnerRelation) throws DataAccessException;

	int update(ProductPartnerRelation productPartnerRelation) throws DataAccessException;

	int delete(int productPartnerRelationId) throws DataAccessException;

	ProductPartnerRelation select(int productPartnerRelationId) throws DataAccessException;

	List<ProductPartnerRelation> list(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException;
	
	List<ProductPartnerRelation> listOnPage(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException;
	
	int count(ProductPartnerRelationCriteria productPartnerRelationCriteria) throws DataAccessException;
}
