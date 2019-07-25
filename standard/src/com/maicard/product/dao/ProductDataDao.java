package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.ProductData;

public interface ProductDataDao {

	int insert(ProductData productData) throws DataAccessException;

	int update(ProductData productData) throws DataAccessException;

	int delete(int productDataId) throws DataAccessException;

	ProductData select(int productDataId) throws DataAccessException;

	List<Integer> listPk(ProductDataCriteria productDataCriteria) throws DataAccessException;
	
	List<Integer> listPkOnPage(ProductDataCriteria productDataCriteria) throws DataAccessException;
	
	int count(ProductDataCriteria productDataCriteria) throws DataAccessException;

	List<Integer> listProductIdByCriteria(ProductDataCriteria productDataCriteria);

	List<ProductData> list(ProductDataCriteria productDataCriteria)
			throws DataAccessException;

	List<ProductData> listOnPage(ProductDataCriteria productDataCriteria)
			throws DataAccessException;

}
