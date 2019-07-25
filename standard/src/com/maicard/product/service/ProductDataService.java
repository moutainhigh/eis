package com.maicard.product.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;

public interface ProductDataService {

	int insert(ProductData productData);

	int update(ProductData productData);

	int delete(int productDataId);
	
	ProductData select(int productDataId);

	List<ProductData> list(ProductDataCriteria productDataCriteria);

	HashMap<String, ProductData> map(ProductDataCriteria productDataCriteria);

	List<ProductData> listOnPage(ProductDataCriteria productDataCriteria);

	int delete(ProductDataCriteria productDataCriteria);

	int put(ProductData productData);

	List<Integer> listProductIdByCriteria(
			ProductDataCriteria productDataCriteria);

	void sync(Product product);

}
