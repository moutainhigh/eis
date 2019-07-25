package com.maicard.product.dao;

import com.maicard.product.criteria.PayTypeProductRelationCriteria;

public interface PayTypeProductRelationDao {
	
	Integer getProductIdByPayTypeId(
			PayTypeProductRelationCriteria payTypeProductRelationCriteria);


}
