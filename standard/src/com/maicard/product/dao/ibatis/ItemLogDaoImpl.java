package com.maicard.product.dao.ibatis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.dao.ItemLogDao;
import com.maicard.product.domain.Item;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.TablePartitionPolicy;

@Repository
public class ItemLogDaoImpl extends BaseDao implements ItemLogDao {


	private static final String defaultTableName = "item_log";
	private static final String historyTableName = "item_log_history";

	private static final int insertRetry = 3;
	private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);


	public int insert(Item item) throws DataAccessException {
		/*try{
			getSqlSessionTemplate().update("ItemLog.init", null);
		}catch(Exception e){
			logger.debug("无法执行初始化语句，可能已经执行:" + e.getMessage());
		}*/
		getTableName(item);
		for(int i = 0; i < insertRetry; i++){
			try{
				return getSqlSessionTemplate().insert("ItemLog.insert", item);
			}catch(DeadlockLoserDataAccessException deadLock){
				logger.warn("插入ItemLog发生死锁，重试[" + (i+1) + "/" + insertRetry + "]...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				continue;
			}catch(Exception e){
				logger.error("插入ItemLog发生异常:" + e.getMessage());
				return 0;
			}
		}
		return 0;
	}

	@Override
	public List<Item> listBadVps() throws DataAccessException {

		return getSqlSessionTemplate().selectList("ItemLog.listBadVps");
	}

	public Item select(String transactionId) throws DataAccessException {
		Item item = new Item();
		item.setTransactionId(transactionId);
		getTableName(item);
		return (Item) getSqlSessionTemplate().selectOne("ItemLog.select", item);
	}

	public List<Item> list(ItemCriteria itemCriteria) throws DataAccessException {
		/*try{
			getSqlSessionTemplate().update("ItemLog.init", null);
		}catch(Exception e){
			logger.debug("无法执行初始化语句，可能已经执行:" + e.getMessage());
		}*/
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		getTableName(itemCriteria);
		return getSqlSessionTemplate().selectList("ItemLog.list", itemCriteria);
	}

	public List<Item> listOnPage(ItemCriteria itemCriteria) throws DataAccessException {
		/*try{
			getSqlSessionTemplate().update("ItemLog.init", null);
		}catch(Exception e){
			logger.debug("无法执行初始化语句，可能已经执行:" + e.getMessage());
		}*/
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		Assert.notNull(itemCriteria.getPaging(), "paging must not be null");
		getTableName(itemCriteria);		
		int totalResults = count(itemCriteria);
		Paging paging = itemCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());		
		return getSqlSessionTemplate().selectList("ItemLog.list", itemCriteria, rowBounds);
	}

	public int count(ItemCriteria itemCriteria) throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		getTableName(itemCriteria);
		return ((Integer) getSqlSessionTemplate().selectOne("ItemLog.count", itemCriteria)).intValue();
	}


	private void getTableName(Item item){
		if(TablePartitionPolicy.itemLog == null || TablePartitionPolicy.itemLog.getName().equals("")){	
			item.setTableName(defaultTableName);
			return;
		}
		if(item.getEnterTime() == null && item.getCloseTime() == null){
			item.setTableName(defaultTableName + "_" +  new SimpleDateFormat(TablePartitionPolicy.itemLog.getName()).format(new Date()));
			return;
		}
		if(item.getEnterTime() != null){
			item.setTableName(defaultTableName + "_" +  new SimpleDateFormat(TablePartitionPolicy.itemLog.getName()).format(item.getEnterTime()));
			return;
		}
		if(item.getClass() != null){
			item.setTableName(defaultTableName + "_" +  new SimpleDateFormat(TablePartitionPolicy.itemLog.getName()).format(item.getCloseTime()));
			return;
		}
	}

	@Override
	public int delete(ItemCriteria itemCriteria) {
		getTableName(itemCriteria);
		try{
			return getSqlSessionTemplate().insert("ItemLog.deleteByCriteria", itemCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	private String getTableName(String transactionId){
		String tableName = null;
		Date splitDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);
		Date orderDate = getDateByTransactionId(transactionId);
		if(orderDate == null){
			tableName =  defaultTableName;
		} else {
			if (orderDate.after(splitDate)){				
				tableName =  defaultTableName;
			} else { 
				tableName = historyTableName;
			}
		}

		if(logger.isDebugEnabled()){
			logger.debug("订单[" + transactionId + "]时间是[" + sdf.format(orderDate) + "]，分表时间点是[" + sdf.format(splitDate) + "],应用表名" + tableName);
		}
		return tableName;
	}

	private Date getDateByTransactionId(String transactionId) {
		if(transactionId == null || transactionId.equals("") || transactionId.length() < 23){
			return null;
		}
		try{
			return new SimpleDateFormat(CommonStandard.orderIdDateFormat).parse(transactionId.substring(5,19));
		}catch(Exception e){}
		return null;
	}


	private void getTableName(ItemCriteria itemCriteria) {
		Date splitDate = DateUtils.truncate(DateUtils.addDays(new Date(), -1), Calendar.DAY_OF_MONTH);

		if (itemCriteria != null && itemCriteria.getTableName() != null && itemCriteria.getTableName().equals(historyTableName)) {
			itemCriteria.setTableName(defaultTableName);
		}

		if (TablePartitionPolicy.itemHistory.toString().equals("MM")) {
			if(itemCriteria.getTransactionId() != null){
				logger.debug("查询条件中包含了transcationId=" + itemCriteria.getTransactionId() + "，使用该ID判断表名");
				itemCriteria.setTableName(getTableName(itemCriteria.getTransactionId()));
				return;
			}
			if (itemCriteria.getEnterTimeBegin() == null){

				itemCriteria.setTableName(defaultTableName);
			} else {
				if (itemCriteria.getEnterTimeBegin().after(splitDate)){				
					itemCriteria.setTableName(defaultTableName);
				} else { 
					itemCriteria.setTableName(historyTableName);
				}
			}

		} else {
			itemCriteria.setTableName(defaultTableName);
		}
	}


}
