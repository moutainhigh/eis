package com.maicard.product.service;

import java.util.List;

import com.maicard.product.criteria.ProductPartnerRelationCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductPartnerRelation;
import com.maicard.security.domain.User;

public interface ProductPartnerRelationService {

	int insert(ProductPartnerRelation productPartnerRelation);

	int update(ProductPartnerRelation productPartnerRelation);

	int delete(int productPartnerRelationId);
	
	ProductPartnerRelation select(int productPartnerRelationId);

	List<ProductPartnerRelation> list(ProductPartnerRelationCriteria productPartnerRelationCriteria);

	List<ProductPartnerRelation> listOnPage(ProductPartnerRelationCriteria productPartnerRelationCriteria);

	List<User> listPartner(ProductPartnerRelationCriteria productPartnerRelationCriteria);

	List<Product> listProduct(
			ProductPartnerRelationCriteria productPartnerRelationCriteria);

	void applyPartnerSetting(Item accountItem);

	int count(ProductPartnerRelationCriteria productPartnerRelationCriteria);

}
