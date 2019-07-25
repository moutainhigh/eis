package com.maicard.product.service;

import com.maicard.product.criteria.PayTypeProductRelationCriteria;
import com.maicard.product.domain.Product;

//支付卡密与内部点卡产品的对应关系

public interface PayTypeProductRelationService {
	
	Product getProductByPayType(
			PayTypeProductRelationCriteria payTypeProductRelationCriteria);

}
