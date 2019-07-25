package com.maicard.product.service;

import com.maicard.product.domain.TransPlan;


public interface TransPlanService {
	
	public TransPlan select(int transPlanId);
	
	public TransPlan selectByProductId(int productId);

	public int getTransactionTypeIdFromTransactionid(String resultString);

	public TransactionExecutor getTransactionExecutor(String objectType, long objectId);

}
