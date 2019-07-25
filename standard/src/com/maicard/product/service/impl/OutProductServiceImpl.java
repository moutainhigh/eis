package com.maicard.product.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.criteria.OutProductCriteria;
import com.maicard.product.dao.OutProductDao;
import com.maicard.product.domain.OutProduct;
import com.maicard.product.domain.Product;
import com.maicard.product.service.OutProductService;
import com.maicard.product.service.ProductService;

@Service
public class OutProductServiceImpl extends BaseService implements OutProductService {

	@Resource
	private OutProductDao outProductDao;
	
	@Resource
	private ProductService productService;

	@Override
	public Product selectInternal(OutProductCriteria outProductCriteria){
		OutProduct outProduct = select(outProductCriteria);
		if(outProduct == null){
			logger.error("根据条件找不到指定的外部产品配置:{}", outProductCriteria.getOutProductCode());
			return null;
		}
		
		return productService.select(outProduct.getInternalProductId());
	}
	
	@Override
	public OutProduct select(OutProductCriteria outProductCriteria) {
		 List<OutProduct> list = list(outProductCriteria);
		 if(list != null && list.size() == 1){
			 return list.get(0);
		 }
		return null;
	}

	@Override
	public List<OutProduct> list(OutProductCriteria outProductCriteria) {
		return outProductDao.list(outProductCriteria);
	}
}
