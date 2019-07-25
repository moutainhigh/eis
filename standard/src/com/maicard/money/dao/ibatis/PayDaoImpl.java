package com.maicard.money.dao.ibatis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.exception.DataInvalidException;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.dao.PayDao;
import com.maicard.money.domain.Pay;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.TablePartitionPolicy;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Repository
public class PayDaoImpl extends BaseDao implements PayDao {
	
	@Resource
	private GlobalOrderIdService globalOrderIdService;

	@Resource
	private ConfigService configService;
	
	private static final String defaultTableName = "pay";
	
	//每天几点会执行把数据移动到历史表
	//前第三天的数据理论上应该去历史表，但如果小于这个时间，说明job还没执行，则还需要在pay表中查询
	private static int moveHistoryHour = 1;
	@PostConstruct
	public void init() {
		moveHistoryHour = configService.getIntValue("PAY_HISTORY_MOVE_JOB_HOUR", 0);
		if(moveHistoryHour == 0) {
			moveHistoryHour = 1;
		}
		
	}

	private static ThreadLocal<SimpleDateFormat> monthFormatterHolder
	= new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat(TablePartitionPolicy.pay.getName());
		}
	};
	
	public int insert(Pay pay) throws DataAccessException {
		pay.setTableName(getTableName(pay.getTransactionId()));
		return getSqlSessionTemplate().insert("com.maicard.money.sql.Pay.insert", pay);
	}

	public int update(Pay pay) throws Exception {
		if(pay.getTransactionId() == null || pay.getTransactionId().equals("")){
			throw new RequiredAttributeIsNullException("要更新的支付订单没有订单号");
		}
		pay.setTableName(getTableName(pay.getTransactionId()));
		//pay.setTableName(defaultTableName);
		return getSqlSessionTemplate().update("com.maicard.money.sql.Pay.update", pay);
	}

	public int delete(String transactionId) throws DataAccessException {
		Pay pay = new Pay();
		pay.setTransactionId(transactionId);
		pay.setTableName(defaultTableName);
		return getSqlSessionTemplate().delete("com.maicard.money.sql.Pay.delete", pay);
	}

	public Pay select(String transactionId) throws DataAccessException {
		Pay pay = new Pay();
		pay.setTransactionId(transactionId);
		pay.setTableName(getTableName(transactionId));
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Pay.select", pay);
	}

	public List<Pay> list(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		getTableName(payCriteria);
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Pay.list", payCriteria);
	}

	public List<Pay> listOnPage(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		Assert.notNull(payCriteria.getPaging(), "paging must not be null");
		getTableName(payCriteria);
		int totalResults = count(payCriteria);
		Paging paging = payCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Pay.list", payCriteria, rowBounds);
	}

	@Override
	public List<Pay> listOnPageByPartner(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		Assert.notNull(payCriteria.getPaging(), "paging must not be null");

		getTableName(payCriteria);
		int totalResults = countByPartner(payCriteria);
		Paging paging = payCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Pay.listByPartner", payCriteria, rowBounds);
	}
	@Override
	public List<Pay> listOnPageByday(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		Assert.notNull(payCriteria.getPaging(), "paging must not be null");

		getTableName(payCriteria);
		int totalResults = countByPartner(payCriteria);
		Paging paging = payCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Pay.listByDay", payCriteria, rowBounds);
	}
	public int countByPartner(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		getTableName(payCriteria);
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.Pay.countByPartner", payCriteria)).intValue();
	}

	public int count(PayCriteria payCriteria) throws DataAccessException {
		Assert.notNull(payCriteria, "payCriteria must not be null");
		getTableName(payCriteria);
		return getSqlSessionTemplate().selectOne("com.maicard.money.sql.Pay.count", payCriteria);
	}

	private void getTableName(PayCriteria payCriteria){
		if(TablePartitionPolicy.pay == null || TablePartitionPolicy.pay.getName().equals("")){	
			payCriteria.setTableName(defaultTableName);
			return;
		}
		if(payCriteria.getStartTime() == null && payCriteria.getEndTime() == null 
				&& payCriteria.getStartTimeBegin() == null && payCriteria.getStartTimeEnd() == null
				&& payCriteria.getEndTimeBegin() == null && payCriteria.getEndTimeBegin() == null){
			payCriteria.setTableName(defaultTableName + "_" + monthFormatterHolder.get().format(new Date()));
			return;
		}
		
		if(payCriteria.getEndTimeBegin() != null){
			payCriteria.setTableName(defaultTableName + "_" +  monthFormatterHolder.get().format(payCriteria.getEndTimeBegin()));
			String lastMonth2 = defaultTableName + "_" + monthFormatterHolder.get().format(DateUtils.addMonths(payCriteria.getEndTimeBegin(), -1));
			payCriteria.setTableName2(lastMonth2);
			return;
		}
		if(payCriteria.getEndTimeEnd() != null){
			payCriteria.setTableName(defaultTableName + "_" + monthFormatterHolder.get().format(payCriteria.getEndTimeEnd()));
			return;
		}
		if(payCriteria.getStartTimeEnd() != null){
			payCriteria.setTableName(defaultTableName + "_" + monthFormatterHolder.get().format(payCriteria.getStartTimeEnd()));
			return;
		}
		
		if(payCriteria.getStartTimeBegin() != null){
			payCriteria.setTableName(defaultTableName + "_" + monthFormatterHolder.get().format(payCriteria.getStartTimeBegin()));
			return;
		}
		if(payCriteria.getStartTimeEnd() != null){
			payCriteria.setTableName(defaultTableName + "_" + monthFormatterHolder.get().format(payCriteria.getStartTimeEnd()));
			return;
		}
		if(payCriteria.getStartTime() != null){
			payCriteria.setTableName(defaultTableName + "_" +  payCriteria.getStartTime().substring(5,7));
			return;
		}
		if(payCriteria.getEndTime() != null){
			payCriteria.setTableName(defaultTableName + "_" +  payCriteria.getStartTime().substring(5,7));
			return;
		}
	}
	
	private String getTableName(String transactionId){
		if(transactionId == null || transactionId.equals("")){
			throw new DataInvalidException("支付订单没有订单号");
		}
		if(TablePartitionPolicy.pay == null || TablePartitionPolicy.pay.getName().equals("")){	
			return defaultTableName;
		}
		Date transactionIdDate = globalOrderIdService.getDateByTransactionId(transactionId);
		if(CommonStandard.payMoveToHistoryDay > 0){
			if(transactionIdDate == null){
				logger.error("无法判断订单号的生成日期[" + transactionId + "]");
				return defaultTableName;
			}
			Date historySplitDate = DateUtils.truncate(DateUtils.addDays(new Date(), -(CommonStandard.payMoveToHistoryDay)+1),Calendar.DAY_OF_MONTH );
			
			if(transactionIdDate.after( historySplitDate)){
				logger.debug("订单号[" + transactionId + "]的日期[" + new SimpleDateFormat("yyyyMMddHHmmss").format(transactionIdDate) + "]在分表时间点[" + new SimpleDateFormat("yyyyMMddHHmmss").format(historySplitDate) + "]之后，使用默认表:" + defaultTableName);
				return defaultTableName;
			} else if(DateUtils.isSameDay(DateUtils.addDays(historySplitDate, -1), transactionIdDate) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < moveHistoryHour){
				logger.debug("订单号[" + transactionId + "]的日期[" + new SimpleDateFormat("yyyyMMddHHmmss").format(transactionIdDate) + "]在分表时间点[" + new SimpleDateFormat("yyyyMMddHHmmss").format(historySplitDate) + "]前一天，但当日移动Job还没执行，使用默认表:" + defaultTableName);
				return defaultTableName;
				
			}
			logger.debug("订单号[" + transactionId + "]的日期[" + new SimpleDateFormat("yyyyMMddHHmmss").format(transactionIdDate) + "]在分表时间点[" + new SimpleDateFormat("yyyyMMddHHmmss").format(historySplitDate) + "]之前，使用历史表");
		}
		return defaultTableName + "_" + monthFormatterHolder.get().format(transactionIdDate);
	}



}
