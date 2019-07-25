package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.domain.ProductGroup;

public interface ProductGroupDao {

	int insert(ProductGroup productGroup) throws DataAccessException;

	int update(ProductGroup productGroup) throws DataAccessException;

	int delete(long id) throws DataAccessException;

	ProductGroup select(long id) throws DataAccessException;

	List<ProductGroup> list(ProductGroupCriteria productGroupCriteria) throws DataAccessException;
	
	List<ProductGroup> listOnPage(ProductGroupCriteria productGroupCriteria) throws DataAccessException;
	
	int count(ProductGroupCriteria productGroupCriteria) throws DataAccessException;

	List<ProductGroup> listNextGroup(ProductGroupCriteria productGroupCriteria) throws DataAccessException;

	long readMaxObjectId(long ownerId);



}
