package com.maicard.billing.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.billing.criteria.BillingCriteria;
import com.maicard.billing.domain.Billing;

public interface BillingDao {

	

	int update(Billing billing) throws DataAccessException;

	int delete(int billingId) throws DataAccessException;

	Billing select(int billingId) throws DataAccessException;
	


	List<Billing> list(BillingCriteria billingCriteria) throws DataAccessException;
	
	List<Billing> listRecentBilling(BillingCriteria billingCriteria) throws DataAccessException;
	
	List<Billing> listOnPage(BillingCriteria billingCriteria) throws DataAccessException;
	
	int count(BillingCriteria billingCriteria) throws DataAccessException;
	int countrecentbilling(BillingCriteria billingCriteria) throws DataAccessException;

	int create(BillingCriteria billingCriteria);

	int insert(Billing billing);

	Billing billing(Billing billing);

}
