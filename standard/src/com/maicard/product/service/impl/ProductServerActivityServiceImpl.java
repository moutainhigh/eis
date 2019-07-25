package com.maicard.product.service.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.CacheService;
import com.maicard.product.domain.ProductServer;
import com.maicard.product.service.ProductServerActivityService;
import com.maicard.standard.CommonStandard;

@Service
public class ProductServerActivityServiceImpl extends BaseService implements
		ProductServerActivityService {
	
	@Resource
	private CacheService cacheService;
	
	private String cacheName = CommonStandard.cacheNameProduct;

	@Override
	@Async
	public void updateActivity(int productServerId) {
		String cacheKey = "ProductServer#" + productServerId;
		ProductServer productServer = cacheService.get(cacheName, cacheKey);
		
		if(productServer == null){
			logger.warn("在缓存中找不到要更新活跃度的产品服务器[" + productServerId + "]");
			return;
		}
		long oldCount = productServer.getActivity();
		productServer.setActivity(productServer.getActivity()+1);
		cacheService.put(cacheName, cacheKey, productServer);
		if(logger.isDebugEnabled()){
			logger.debug("将产品服务器的活跃度从" + oldCount + "更新到" + productServer.getActivity());
		}

		
		

	}

}
