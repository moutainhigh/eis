package com.maicard.product.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.product.criteria.ProductTypeCriteria;
import com.maicard.product.domain.ProductType;

public interface ProductTypeService {

	int insert(ProductType productType);

	int update(ProductType productType);

	int delete(int productTypeId);
	
	ProductType select(int productTypeId);

	List<ProductType> list(ProductTypeCriteria productTypeCriteria);

	List<ProductType> listOnPage(ProductTypeCriteria productTypeCriteria);

	HashMap<Integer, String> map(ProductTypeCriteria productTypeCriteria);

	int count(ProductTypeCriteria productTypeCriteria);

}
