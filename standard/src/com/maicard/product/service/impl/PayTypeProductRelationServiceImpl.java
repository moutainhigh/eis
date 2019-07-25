package com.maicard.product.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.criteria.PayTypeProductRelationCriteria;
import com.maicard.product.dao.PayTypeProductRelationDao;
import com.maicard.product.domain.Product;
import com.maicard.product.service.PayTypeProductRelationService;
import com.maicard.product.service.ProductService;

@Service
public class PayTypeProductRelationServiceImpl extends BaseService implements PayTypeProductRelationService{
	
	@Resource
	private PayTypeProductRelationDao payTypeProductRelationDao;
	
	@Resource
	private ProductService productService;

	@Override
	public Product getProductByPayType(PayTypeProductRelationCriteria payTypeProductRelationCriteria) {
		Integer productId = payTypeProductRelationDao.getProductIdByPayTypeId(payTypeProductRelationCriteria);
		if(productId == null || productId == 0){
			return null;
		}
		return productService.select(productId);
	}

}
