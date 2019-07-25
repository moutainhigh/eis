package com.maicard.product.service;


import java.util.List;

import com.maicard.product.criteria.OutProductCriteria;
import com.maicard.product.domain.OutProduct;
import com.maicard.product.domain.Product;

public interface OutProductService {


	OutProduct select(OutProductCriteria outProductCriteria);

	List<OutProduct> list(OutProductCriteria outProductCriteria);

	Product selectInternal(OutProductCriteria outProductCriteria);

	
}
