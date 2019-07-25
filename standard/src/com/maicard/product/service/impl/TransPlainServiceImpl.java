package com.maicard.product.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.product.domain.TransPlan;
import com.maicard.product.service.TransPlanService;
import com.maicard.product.service.TransactionExecutor;
import com.maicard.standard.DataName;

public class TransPlainServiceImpl implements TransPlanService {
	
	@Resource
	private ConfigService configService;
	
	@Resource
	private ApplicationContextService applicationContextService;
	
	
	String defaultTransactionExecutor = null;
	
	@PostConstruct
	public void init(){
		defaultTransactionExecutor = configService.getValue(DataName.defaultTransactionExecutor.toString(),0);
	}

	@Override
	public TransPlan select(int transPlanId) {
		TransPlan defaultTransPlan = new TransPlan();
		defaultTransPlan.setProcessClass(defaultTransactionExecutor);
		return defaultTransPlan;
	}

	@Override
	public int getTransactionTypeIdFromTransactionid(String resultString) {
		if(resultString == null || resultString.length() < 5){
			return 0;
		}
		return Integer.parseInt(resultString.substring(2,4));
	}

	@Override
	public TransPlan selectByProductId(int productId) {
		TransPlan defaultTransPlan = new TransPlan();
		defaultTransPlan.setProcessClass(defaultTransactionExecutor);
		return defaultTransPlan;
	}

	@Override
	public TransactionExecutor getTransactionExecutor(String objectType, long objectId) {
		return applicationContextService.getBeanGeneric(defaultTransactionExecutor);
	}


}
