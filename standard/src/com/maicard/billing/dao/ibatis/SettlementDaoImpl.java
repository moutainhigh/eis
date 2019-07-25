package com.maicard.billing.dao.ibatis;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.billing.criteria.SettlementCriteria;
import com.maicard.billing.dao.SettlementDao;
import com.maicard.billing.domain.Settlement;
import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;

@Repository
public class SettlementDaoImpl extends BaseDao implements SettlementDao {

	public int insert(SettlementCriteria settlementCriteria) throws DataAccessException {



		settlementCriteria.setBillingBeginTime(DateUtils.truncate(settlementCriteria.getBillingBeginTime(),Calendar.DAY_OF_MONTH));
		settlementCriteria.setBillingEndTime(DateUtils.addSeconds(DateUtils.ceiling(settlementCriteria.getBillingEndTime(),Calendar.DAY_OF_MONTH), -1));
		return (Integer)getSqlSessionTemplate().insert("Settlement.insert", settlementCriteria);
	}

	public int update(Settlement settlement) throws DataAccessException {
		return getSqlSessionTemplate().update("Settlement.update", settlement);
	}

	public int delete(int billingId) throws DataAccessException {
		return getSqlSessionTemplate().delete("Settlement.delete", new Integer(billingId));
	}

	public Settlement select(int billingId) throws DataAccessException {
		return (Settlement) getSqlSessionTemplate().selectOne("Settlement.select", new Integer(billingId));
	}
	


	public List<Settlement> list(SettlementCriteria settlementCriteria) throws DataAccessException {
		Assert.notNull(settlementCriteria, "billingCriteria must not be null");		
		
	//	if(settlementCriteria.getBillingBeginTime() == null || settlementCriteria.getBillingBeginTime().equals("")){
	//		settlementCriteria.setBillingBeginTime(DateUtils.addWeeks(new Date(),-1));
	//	}else{
//
		//	settlementCriteria.setBillingBeginTime(DateUtils.truncate(settlementCriteria.getBillingBeginTime(),Calendar.DAY_OF_MONTH));
	//	}
	//	if(settlementCriteria.getBillingEndTime() == null || settlementCriteria.getBillingEndTime().equals("")){
	//		settlementCriteria.setBillingEndTime(new Date());
	//	}else{

	//		settlementCriteria.setBillingEndTime(DateUtils.addSeconds(DateUtils.ceiling(settlementCriteria.getBillingEndTime(),Calendar.DAY_OF_MONTH), -1));
//
	//	}
		return getSqlSessionTemplate().selectList("Settlement.list", settlementCriteria);
	}

	public List<Settlement> listOnPage(SettlementCriteria settlementCriteria) throws DataAccessException {
		Assert.notNull(settlementCriteria, "settlementCriteria must not be null");
		Assert.notNull(settlementCriteria.getPaging(), "paging must not be null");

		int totalResults = count(settlementCriteria);
		Paging paging = settlementCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Settlement.list", settlementCriteria, rowBounds);
	}

	public int count(SettlementCriteria settlementCriteria) throws DataAccessException {
		Assert.notNull(settlementCriteria, "settlementCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("Settlement.count", settlementCriteria)).intValue();
	}
	public int countrecentbilling(SettlementCriteria settlementCriteria) throws DataAccessException {
		Assert.notNull(settlementCriteria, "settlementCriteria must not be null");		
		return ((Integer) getSqlSessionTemplate().selectOne("Settlement.countrecentbilling", settlementCriteria)).intValue();
	}

	@Override
	public List<Settlement> listRecentBilling(SettlementCriteria settlementCriteria)
			throws DataAccessException {

		Assert.notNull(settlementCriteria, "settlementCriteria must not be null");
		Assert.notNull(settlementCriteria.getPaging(), "paging must not be null");





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



		int totalResults = countrecentbilling(settlementCriteria);
		Paging paging = settlementCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("Settlement.listRecentBilling", settlementCriteria, rowBounds);

	}




}
