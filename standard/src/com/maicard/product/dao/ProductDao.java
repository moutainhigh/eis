package com.maicard.product.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.product.criteria.ProductCriteria;
import com.maicard.product.domain.Product;

public interface ProductDao {

	int insert(Product product) throws DataAccessException;

	int update(Product product) throws DataAccessException;

	int delete(long productId) throws DataAccessException;

	Product select(long productId) throws DataAccessException;

	List<Product> list(ProductCriteria productCriteria) throws DataAccessException;
	
	List<Product> listOnPage(ProductCriteria productCriteria) throws DataAccessException;
	
	int count(ProductCriteria productCriteria) throws DataAccessException;

	List<Long> listPkByNodeIds(ProductCriteria productCriteria);

	List<Long> listPk(ProductCriteria productCriteria);

	List<Long> listPkOnPage(ProductCriteria productCriteria);

	/**
	 * 得到数据库中最大自增KEY，条件暂时无用
	 * @param productCriteria
	 * @return
	 * @throws DataAccessException
	 */
	long getMaxId(ProductCriteria productCriteria) throws DataAccessException;

	void updateAmount(Product product);

	int forceDeleteAllAndRelation(long productId);

	int updateNoNull(Product product);

}
