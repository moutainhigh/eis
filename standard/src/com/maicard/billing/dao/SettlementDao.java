package com.maicard.billing.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.billing.criteria.SettlementCriteria;
import com.maicard.billing.domain.Settlement;

public interface SettlementDao {

	

	int update(Settlement settlement) throws DataAccessException;

	int delete(int billingId) throws DataAccessException;

	Settlement select(int billingId) throws DataAccessException;
	


	List<Settlement> list(SettlementCriteria settlementCriteria) throws DataAccessException;
	
	
	
	List<Settlement> listRecentBilling(SettlementCriteria settlementCriteria) throws DataAccessException;
	
	List<Settlement> listOnPage(SettlementCriteria settlementCriteria) throws DataAccessException;
	
	int count(SettlementCriteria settlementCriteria) throws DataAccessException;
	int countrecentbilling(SettlementCriteria settlementCriteria) throws DataAccessException;

	int insert(SettlementCriteria settlementCriteria);

}
