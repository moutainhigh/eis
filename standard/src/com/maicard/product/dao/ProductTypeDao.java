package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.domain.ProductType;

public interface ProductTypeDao {

	int insert(ProductType productType) throws DataAccessException;

	int update(ProductType productType) throws DataAccessException;

	int delete(int productTypeId) throws DataAccessException;

	ProductType select(int productTypeId) throws DataAccessException;

	List<ProductType> list(ProductTypeCriteria productTypeCriteria) throws DataAccessException;
	
	List<ProductType> listOnPage(ProductTypeCriteria productTypeCriteria) throws DataAccessException;
	
	int count(ProductTypeCriteria productTypeCriteria) throws DataAccessException;

}
