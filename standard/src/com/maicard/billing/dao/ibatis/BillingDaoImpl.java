package com.maicard.billing.dao.ibatis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.billing.criteria.BillingCriteria;
import com.maicard.billing.dao.BillingDao;
import com.maicard.billing.domain.Billing;
import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class BillingDaoImpl extends BaseDao implements BillingDao {
	
	@Override
	public int insert(Billing billing){
		return getSqlSessionTemplate().insert("com.maicard.billing.sql.Billing.insert", billing);
	}

	public int create(BillingCriteria billingCriteria) throws DataAccessException {
		return 0;
/*


		billingCriteria.setBillingBeginTime(DateUtils.truncate(billingCriteria.getBillingBeginTime(),Calendar.DAY_OF_MONTH));
		billingCriteria.setBillingEndTime(DateUtils.addSeconds(DateUtils.ceiling(billingCriteria.getBillingEndTime(),Calendar.DAY_OF_MONTH), -1));
		return (Integer)getSqlSessionTemplate().insert("Billing.insert", billingCriteria);*/
	}

	public int update(Billing billing) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.billing.sql.Billing.update", billing);
	}

	public int delete(int billingId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.billing.sql.Billing.delete", new Integer(billingId));
	}

	public Billing select(int billingId) throws DataAccessException {
		return (Billing) getSqlSessionTemplate().selectOne("com.maicard.billing.sql.Billing.select", new Integer(billingId));
	}


	public List<Billing> list(BillingCriteria billingCriteria) throws DataAccessException {
		Assert.notNull(billingCriteria, "billingCriteria must not be null");		

		if(billingCriteria.getBillingBeginTimeBegin() == null){
			billingCriteria.setBillingBeginTimeBegin(DateUtils.truncate(DateUtils.addWeeks(new Date(),-1),Calendar.DAY_OF_MONTH));
		}
		//billingCriteria.setBillingBeginTimeBegin(DateUtils.truncate(billingCriteria.getBillingBeginTimeBegin(),Calendar.DAY_OF_MONTH));
		
		if(billingCriteria.getBillingEndTimeEnd() == null){
			billingCriteria.setBillingEndTimeEnd(new Date());
		}
/*
			billingCriteria.setBillingEndTime(DateUtils.addSeconds(DateUtils.ceiling(billingCriteria.getBillingEndTime(),Calendar.DAY_OF_MONTH), -1));
*/
		
		return getSqlSessionTemplate().selectList("com.maicard.billing.sql.Billing.list", billingCriteria);
	}

	public List<Billing> listOnPage(BillingCriteria billingCriteria) throws DataAccessException {
		Assert.notNull(billingCriteria, "billingCriteria must not be null");
		Assert.notNull(billingCriteria.getPaging(), "paging must not be null");
		
		if(billingCriteria.getBillingBeginTimeBegin() == null){
			billingCriteria.setBillingBeginTimeBegin(DateUtils.truncate(DateUtils.addWeeks(new Date(),-1),Calendar.DAY_OF_MONTH));
		}
		//billingCriteria.setBillingBeginTimeBegin(DateUtils.truncate(billingCriteria.getBillingBeginTimeBegin(),Calendar.DAY_OF_MONTH));
		
		if(billingCriteria.getBillingEndTimeEnd() == null){
			billingCriteria.setBillingEndTimeEnd(new Date());
		}

		int totalResults = count(billingCriteria);
		Paging paging = billingCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.billing.sql.Billing.list", billingCriteria, rowBounds);
	}

	public int count(BillingCriteria billingCriteria) throws DataAccessException {
		Assert.notNull(billingCriteria, "billingCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.billing.sql.Billing.count", billingCriteria)).intValue();
	}
	public int countrecentbilling(BillingCriteria billingCriteria) throws DataAccessException {
		Assert.notNull(billingCriteria, "billingCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.billing.sql.Billing.countrecentbilling", billingCriteria)).intValue();
	}

	@Override
	public List<Billing> listRecentBilling(BillingCriteria billingCriteria)
			throws DataAccessException {

		Assert.notNull(billingCriteria, "billingCriteria must not be null");
		Assert.notNull(billingCriteria.getPaging(), "paging must not be null");





		//	if(billingCriteria.getBillingBeginTime() == null || billingCriteria.getBillingBeginTime().equals("")){
		//		billingCriteria.setBillingBeginTime(DateUtils.addWeeks(new Date(),-1));
		//	}else{

		//		billingCriteria.setBillingBeginTime(DateUtils.truncate(billingCriteria.getBillingBeginTime(),Calendar.DAY_OF_MONTH));
		//	}
		//	if(billingCriteria.getBillingEndTime() == null || billingCriteria.getBillingEndTime().equals("")){
		//		billingCriteria.setBillingEndTime(new Date());
		//	}else{

		//		billingCriteria.setBillingEndTime(DateUtils.addSeconds(DateUtils.ceiling(billingCriteria.getBillingEndTime(),Calendar.DAY_OF_MONTH), -1));

		//	}



		int totalResults = countrecentbilling(billingCriteria);
		Paging paging = billingCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.billing.sql.Billing.listRecentBilling", billingCriteria, rowBounds);

	}

	@Override
	public Billing billing(Billing billing) {
		int rs = getSqlSessionTemplate().update("com.maicard.billing.sql.Billing.billing", billing);
		if(rs > 0){
			return billing;
		}
		return null;
	}




}
