package com.maicard.billing.service;

import java.util.List;

import com.maicard.billing.criteria.BillingCriteria;
import com.maicard.billing.domain.Billing;


public interface BillingService {


	int update(Billing billing);

	int delete(int billingId);
	
	Billing select(int billingId);

	List<Billing> list(BillingCriteria billingCriteria);

	List<Billing> listOnPage(BillingCriteria billingCriteria);

	int count(BillingCriteria billingCriteria);

	/**
	 * 根据指定的条件，处理支付订单并生成结算单<br>
	 * 把指定范围内的支付订单更新为对应的结算ID
	 */
	Billing billing(BillingCriteria billingCriteria);

	int insert(Billing billing);

}
