package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.ProductGroupCriteria;
import com.maicard.product.domain.ProductGroup;

public interface ProductGroupService {

	int insert(ProductGroup cardMatch);

	int update(ProductGroup cardMatch);

	int delete(long productGroupId);
	
	ProductGroup select(long productGroupId);

	List<ProductGroup> list(ProductGroupCriteria productGroupCriteria);

	List<ProductGroup> listOnPage(ProductGroupCriteria productGroupCriteria);

	int count(ProductGroupCriteria productGroupCriteria);

	List<ProductGroup> listNextGroup(ProductGroupCriteria productGroupCriteria);

	List<ProductGroup> generateTree(String objectType, long objectId);

	List<ProductGroup> generateTree(List<ProductGroup> plateList);

	long createMaxObjectId(long ownerId);

	

}
