package com.maicard.product.service;

import com.maicard.common.base.Criteria;
import com.maicard.common.domain.EisMessage;

public interface TransactionExecutor {

	//开始交易，处理资金冻结、事物分发等工作
	EisMessage begin(Object object) throws Exception;

	//查询交易结果尝试结束交易，并根据交易结果进行后续处理，如扣除或返还冻结资金
	EisMessage end(Object object) throws Exception;	
	
	EisMessage lock(Criteria criteria);
	
	void unLock(Object... object);

	void onMessage(EisMessage eisMessage);




}
