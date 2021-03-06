package com.maicard.product.dao;

import java.util.List;
import java.util.Map;

import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.ProductData;

public interface ItemDataDao {

	int insert(ProductData productData) throws Exception;

	int update(ProductData productData) throws Exception;

	int delete(int itemDataId) throws Exception;
	int delete(ProductDataCriteria itemDataCriteria) throws Exception;

	ProductData select(int productDataId) throws Exception;

	List<ProductData> list(ProductDataCriteria itemDataCriteria) throws Exception;
	
	List<ProductData> listOnPage(ProductDataCriteria itemDataCriteria) throws Exception;

	
	List<Long> listBy(ProductDataCriteria itemDataCriteria) throws Exception;
	
	int count(ProductDataCriteria itemDataCriteria) throws Exception;

	void deleteByProductId(int productId);

	int copyToHistory(ProductDataCriteria itemDataCriteria);

	Map<String, ProductData> map(ProductDataCriteria productDataCriteria) throws Exception;

}
