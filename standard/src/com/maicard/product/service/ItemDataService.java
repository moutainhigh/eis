package com.maicard.product.service;

import java.util.List;
import java.util.Map;

import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.ProductData;

public interface ItemDataService {

	int insert(ProductData productData) throws Exception;

	int update(ProductData productData) throws Exception;

	int delete(int productDataId) throws Exception;
	
	ProductData select(int productDataId) throws Exception;

	List<ProductData> list(ProductDataCriteria itemDataCriteria) throws Exception;

	Map<String, ProductData> map(ProductDataCriteria itemDataCriteria) throws Exception;

	List<Long> listBy(ProductDataCriteria itemDataCriteria) throws Exception;
	
	List<ProductData> listOnPage(ProductDataCriteria itemDataCriteria) throws Exception;


	int copyToHistory(ProductDataCriteria itemDataCriteria) throws Exception;

	int delete(ProductDataCriteria itemDataCriteria) throws Exception;


}
