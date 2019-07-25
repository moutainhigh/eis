package com.maicard.billing.service;

import java.util.List;

import com.maicard.billing.criteria.SettlementCriteria;
import com.maicard.billing.domain.Settlement;


public interface SettlementService {

	int insert(SettlementCriteria settlementCriteria);

	int update(SettlementCriteria settlementCriteria);

	int delete(int billingId);
	
	Settlement select(int billingId);

	List<Settlement> list(SettlementCriteria settlementCriteria);

	List<Settlement> listOnPage(SettlementCriteria settlementCriteria);
	List<Settlement> listRecentBilling(SettlementCriteria settlementCriteria);

	int count(SettlementCriteria settlementCriteria);
	int countrecentbilling(SettlementCriteria settlementCriteria);

}
