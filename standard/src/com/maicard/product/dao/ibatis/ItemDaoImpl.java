package com.maicard.product.dao.ibatis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.Paging;
import com.maicard.common.util.TablePartitionUtils;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.dao.ItemDao;
import com.maicard.product.domain.Item;
import com.maicard.standard.DataName;
import com.maicard.standard.TablePartitionPolicy;

@Repository
public class ItemDaoImpl extends BaseDao implements ItemDao {

	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private ConfigService configService;

	private final String  historyTableName = "item_history";

	
	public int insert(Item item) throws DataAccessException {
		return (Integer) getSqlSessionTemplate().insert("com.maicard.product.sql.Item.insert", item);
	}

	public int update(Item item) throws DataAccessException {
		item.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + TablePartitionUtils.getTableMonth(item.getTransactionId()));
		return getSqlSessionTemplate().update("com.maicard.product.sql.Item.update", item);
	}

	@Override
	public int delete(ItemCriteria itemCriteria) throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		if (itemCriteria.getTransactionId() == null
				&& itemCriteria.getTransactionIds() == null
				&& itemCriteria.getItemIds() == null) {
			logger.error("删除条件中没有定义任何交易ID或itemId");
			return 0;
		}
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Item.deleteByCriteria", itemCriteria);
	}

	/*
	 * public Item select(int itemId) throws DataAccessException { return (Item)
	 * getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.select", new Integer(itemId)); }
	 */

	public List<Item> list(ItemCriteria itemCriteria)
			throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		// 只允许最多查询一个整月的时间

		if (itemCriteria.getQueryProcessingItem().equals(
				DataName.only.toString())) {
			// 只查询正在处理的数据
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX);
		} else {
			getTableName(itemCriteria);
		}

			return getSqlSessionTemplate()
					.selectList("com.maicard.product.sql.Item.list", itemCriteria);
		
	}



	public List<Item> listAll(ItemCriteria itemCriteria)
			throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		// 只允许最多查询一个整月的时间
		if (itemCriteria.getEnterTimeBegin() == null) {
			// 未定义开始时间，则为当前时间往前1个月
			itemCriteria.setEnterTimeBegin(DateUtils.addMonths(new Date(), -1));
		}
		if (itemCriteria.getQueryProcessingItem().equals(
				DataName.only.toString())) {
			// 只查询正在处理的数据
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX);
		} else {
			getTableName(itemCriteria);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("xxxxxxxxxxxxxxxxxxxxxx表被设置为"	+ itemCriteria.getTableName() + "xxxxxxxxxxxxxxxxxxxx");
		}
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.list", itemCriteria);

	}

	/*public static String getLastDay(String beginTime)// 最beginTime所在月最后一天
	{
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Integer.parseInt(beginTime.substring(0, 4)));
		calendar.set(Calendar.MONTH,
				Integer.parseInt(beginTime.substring(4, 6)) - 1);
		int endday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		String result = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
				+ String.valueOf((calendar.get(Calendar.MONTH)) + 1) + "-"
				+ String.valueOf(endday);
		return result;
	}*/

	public List<Item> listOnPage(ItemCriteria itemCriteria)
			throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		Assert.notNull(itemCriteria.getPaging(), "paging must not be null");
		getTableName(itemCriteria);
		int totalResults = count(itemCriteria);
		Paging paging = itemCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(),paging.getMaxResults());
		// 只查询正在处理的数据
		if (itemCriteria.getQueryProcessingItem().equals(DataName.only.toString())) {
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX);
		} else {
			getTableName(itemCriteria);
		}
		logger.debug("当前查询的item table=" + itemCriteria.getTableName() + ",记录起止:" + rowBounds.getOffset() + "=>" + rowBounds.getLimit());
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.list", itemCriteria, rowBounds);
		
	}

	public int count(ItemCriteria itemCriteria) throws DataAccessException {
		Assert.notNull(itemCriteria, "itemCriteria must not be null");
		//Assert.notNull(itemCriteria.getPaging(), "paging must not be null");
		getTableName(itemCriteria);
		logger.debug("查询的表：" + itemCriteria.getTableName() + "  是否联查：" + itemCriteria.getQueryProcessingItem());
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.count", itemCriteria)).intValue();
	}

	@Override
	public Item fetchWithLock(ItemCriteria itemCriteria) {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.fetchWithLock",itemCriteria);
	}
	
	@Override
	public int lock(Item item) {
		Assert.notNull(item,"尝试锁定的Item不能为空");
		Assert.isTrue(item.getBeforeLockStatus() > 0,"尝试锁定的Item的锁定前状态不能为0");
		Assert.isTrue(item.getAfterLockStatus() > 0,"尝试锁定的Item的锁定后状态不能为0");
		return getSqlSessionTemplate().update("com.maicard.product.sql.Item.lock",item);
	}


/*
	@Override
	public Item fetchWithPartMoneyLock1(ItemCriteria itemCriteria) {
		itemCriteria.setLockGlobalUniqueId(java.util.UUID.randomUUID()
				.toString());
		logger.debug("模式1锁定交易品, 请求锁定资金[" + itemCriteria.getNeedRequestMoney()
		+ "].");
		Item item = getSqlSessionTemplate().selectOne(
				"com.maicard.product.sql.Item.fetchWithPartMoneyLock1", itemCriteria);
		if (item == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("未能锁定部分资金模式的交易品");
			}
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("成功锁定的不分资金模式交易品:" + item.getItemId()
			+ ",currentStatus=" + item.getCurrentStatus() + ",lockid="
			+ item.getLockGlobalUniqueId());
		}
		return item;
	}
	@Override
	public Item fetchWithPartMoneyLock2(ItemCriteria itemCriteria) {
		itemCriteria.setLockGlobalUniqueId(java.util.UUID.randomUUID()
				.toString());
		logger.debug("模式2锁定交易品, 请求锁定资金[" + itemCriteria.getNeedRequestMoney()
		+ "],最小锁定资金[" + itemCriteria.getNeedMinRequestMoney() + "]");
		Item item = getSqlSessionTemplate().selectOne(
				"com.maicard.product.sql.Item.fetchWithPartMoneyLock2", itemCriteria);
		if (item == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("未能锁定部分资金模式的交易品");
			}
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("成功锁定的不分资金模式交易品:" + item.getItemId()
			+ ",currentStatus=" + item.getCurrentStatus() + ",lockid="
			+ item.getLockGlobalUniqueId());
		}
		return item;
	}

	@Override
	public Item fetchWithPartMoneyLock3(ItemCriteria itemCriteria) {
		itemCriteria.setLockGlobalUniqueId(java.util.UUID.randomUUID()
				.toString());
		logger.debug("模式3锁定交易品,最大锁定资金[" + itemCriteria.getNeedRequestMoney()
		+ "].");
		Item item = getSqlSessionTemplate().selectOne(
				"com.maicard.product.sql.Item.fetchWithPartMoneyLock3", itemCriteria);
		if (item == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("未能锁定部分资金模式的交易品");
			}
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("模式3成功锁定的部分资金模式交易品:" + item.getTransactionId()
			+ ",currentStatus=" + item.getCurrentStatus() + ",lockid="
			+ item.getLockGlobalUniqueId());
		}
		//查询当前数据在数据库中的状态
		ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		List<Item> oldItemList = list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("查不到以模式3成功锁定后交易的数据[" + item.getTransactionId() + "]");
		} else {
			logger.info("以模式3成功锁定后交易的数据[" + item.getTransactionId() + "],requestMoney=" + oldItemList.get(0).getRequestMoney() + ",successMoney=" + oldItemList.get(0).getSuccessMoney() + ",frozenMoney=" + oldItemList.get(0).getFrozenMoney() + ",currentStatus=" + oldItemList.get(0).getCurrentStatus() + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
		}
		return item;
	}

	@Override
	public Item fetchWithPartMoneyLock4(ItemCriteria itemCriteria) {
		itemCriteria.setLockGlobalUniqueId(java.util.UUID.randomUUID()
				.toString());
		logger.debug("模式4锁定交易品, 请求锁定资金" + itemCriteria.getNeedRequestMoney()
		+ ",最小锁定资金" + itemCriteria.getNeedMinRequestMoney() + "，交易类型:"
		+ itemCriteria.getTransactionTypeId() + ",当前状态为:"
		+ itemCriteria.getLockStatus() + ",处理超时交易规则:"
		+ itemCriteria.getTimeoutPolicy() + ",锁定产品范围:" + (itemCriteria.getProductIds() ==  null ? "空" : + itemCriteria.getProductIds().length) + "]");
		Item item = getSqlSessionTemplate().selectOne(
				"com.maicard.product.sql.Item.fetchWithPartMoneyLock4", itemCriteria);
		if (item == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("未能锁定部分资金模式的交易品");
			}
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("成功锁定的部分资金模式交易品:" + item.getTransactionId()
			+ ",requestMoney=" + item.getRequestMoney() + ",successMoney=" + item.getSuccessMoney() + ",frozenMoney=" + item.getFrozenMoney() + ",currentStatus=" + item.getCurrentStatus() + ",lockid="
			+ item.getLockGlobalUniqueId());
		}
		//查找老订单状态
		ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		List<Item> oldItemList = list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询刚刚锁定的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("刚刚锁定的交易[" + item.getTransactionId() + "],requestMoney=" + oldItemList.get(0).getRequestMoney() + ",successMoney=" + oldItemList.get(0).getSuccessMoney() + ",frozenMoney=" + oldItemList.get(0).getFrozenMoney() + ",currentStatus=" + oldItemList.get(0).getCurrentStatus() + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
		}
		return item;
	}

	@Override
	public int copyToHistory(ItemCriteria itemCriteria) {
		if (itemCriteria == null) {
			return -1;
		}
		if (itemCriteria.getEnterTimeEnd() == null) {
			// 如果未设置结束时间，默认为昨天
			itemCriteria.setEnterTimeEnd(DateUtils.addDays(new Date(), -1));
		}
		if (itemCriteria.getEnterTimeBegin() == null) {
			// 如果未设置开始时间，默认为昨天
			itemCriteria.setEnterTimeBegin(DateUtils.addDays(new Date(), -1));
		}
		getTableName(itemCriteria);
		if (itemCriteria.getTableName() == null
				|| itemCriteria.getTableName().equals(ItemCriteria.ITEM_TABLE_PREFIX)) {
			logger.error("清理交易时错误的表名[" + itemCriteria.getTableName() + "]");
			return -1;
		}
		return getSqlSessionTemplate().update("com.maicard.product.sql.Item.copyToHistory",
				itemCriteria);

	}*/

	private void getTableName(ItemCriteria itemCriteria) {
		if (itemCriteria != null && itemCriteria.getTableName() != null && itemCriteria.getTableName().equals(historyTableName)) {
			return;
		}
		if (!TablePartitionPolicy.itemHistory.toString().equals("MM")) {
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX);
			return;

		}
		
		if(StringUtils.isNotBlank(itemCriteria.getTransactionId())){
			logger.debug("查询条件中包含了transcationId=" + itemCriteria.getTransactionId() + "，使用该ID判断表名");
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + TablePartitionUtils.getTableMonth(itemCriteria.getTransactionId()));
			return;
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");

		if (itemCriteria.getEnterTimeBegin() != null){
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + "_" + sdf2.format(itemCriteria.getEnterTimeBegin()));
		} else if (itemCriteria.getEnterTimeEnd() != null) {
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + "_" + sdf2.format(itemCriteria.getEnterTimeEnd()));
		} else {
			itemCriteria.setTableName(ItemCriteria.ITEM_TABLE_PREFIX);
		}
		
		if(itemCriteria.getEnterTimeBegin() != null && itemCriteria.getEnterTimeEnd() != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(itemCriteria.getEnterTimeBegin());
			int m1 = c1.get(Calendar.MONTH);
			
			Calendar c2 = Calendar.getInstance();
			c2.setTime(itemCriteria.getEnterTimeEnd());
			int m2 = c2.get(Calendar.MONTH);
			
			if(m1 != m2) {
				//查询的开始和结束时间不是一个月，需要多设置一个月
				itemCriteria.setTableName2(ItemCriteria.ITEM_TABLE_PREFIX + "_" + sdf2.format(itemCriteria.getEnterTimeEnd()));
			}
		}
		
		

	}

	@Override
	public String downloadCsv(ItemCriteria itemCriteria) {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.downloadCsv",
				itemCriteria);
	}

	@Override
	public int totalFailItem(int productID) {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.totalFailItem",
				productID);
	}

	@Override
	public int releaseItemWithFrozenMoney(Item item) {
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.releaseItemWithFrozenMoney", item);
	}

	@Override
	public int releaseItemWithFrozenMoney2(Item item) {
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.releaseItemWithFrozenMoney2", item);
	}

	

	@Override
	public int plusItemMoneyWithFrozenMoney(Item item) {
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.plusItemMoneyWithFrozenMoney", item);// minusItemWithFrozenMoney
	}

	@Override
	public int plusItemMoneyWithoutFrozenMoney(Item item) {
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.plusItemMoneyWithoutFrozenMoney", item); // minusItemWithoutFrozenMoney
	}

	@Override
	public List<Item> listProcessTimeout(ItemCriteria itemCriteria) {
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.listProcessTimeout",
				itemCriteria);
	}

/*	@Override
	public int updateWithMoneyCondition(Item item) {
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.updateWithMoneyCondition", item);
	}
*/
	@Override
	public int changeStatus(Item item) {
		item.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + TablePartitionUtils.getTableMonth(item.getTransactionId()));
		return getSqlSessionTemplate().update(
				"com.maicard.product.sql.Item.changeStatus", item);
	}
	@Override
	public int lockUpdateAndRelaseAdditinalFrozenMoney(Item item) {
		int rs = getSqlSessionTemplate().update("com.maicard.product.sql.Item.lockUpdateAndRelaseAdditinalFrozenMoney", item);	
		logger.debug("锁定订单[" + item.getTransactionId() + "]并释放多余锁定金额的操作结果是:" + rs);
		return rs;
	}
	@Override
	public List<Item> listForMatch(ItemCriteria itemCriteria) {
		//return getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.listForMatch", itemCriteria);
		int totalResults = countForMatch(itemCriteria);
		Paging paging = null;
		if(itemCriteria.getMaxRandomCount() > 0){
			paging = new Paging(itemCriteria.getMaxRandomCount());
		} else {
			paging = new Paging();
		}
		paging.setTotalResults(totalResults);
		if(itemCriteria.getMaxRandomPage() <= 1 || totalResults <= itemCriteria.getMaxRandomCount() ){
			paging.setCurrentPage(1);
		} else {
			if(itemCriteria.getMaxRandomCount() > 0){
				int totalPage = totalResults / itemCriteria.getMaxRandomCount();
				if(totalPage <= 1){
					paging.setCurrentPage(1);
				} else {
					int rand = RandomUtils.nextInt(totalPage / 2 + 1);
					logger.debug("当前共[" + totalResults + "]条记录,每组[" + paging.getMaxResults() + "]条记录，共[" + totalPage + "],随机返回第[" + rand + "]组记录");
					paging.setCurrentPage(rand);
				}
			}
		}

		RowBounds rowBounds = new RowBounds(paging.getFirstResult(),
				paging.getMaxResults());
		List<Item> itemList = getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.listForMatch", itemCriteria,rowBounds);
		logger.debug("第" + paging.getCurrentPage() + "组记录[" + paging.getFirstResult() + "," + paging.getMaxResults() + "]返回了[" + (itemList == null ? "空" : itemList.size()) + "]条记录");
		return itemList;
	}

	private int countForMatch(ItemCriteria itemCriteria) {
		return getSqlSessionTemplate().selectOne("com.maicard.product.sql.Item.countForMatch", itemCriteria);	
	}
	@Override
	public List<Item> listFrozenDeadAccount(ItemCriteria itemCriteria) {
		return getSqlSessionTemplate().selectList("com.maicard.product.sql.Item.listFrozenDeadAccount", itemCriteria);	
	}
	@Override
	public int updateNoNull(Item item) {
		Assert.notNull(item, "有限更新的Item不能为空");
		Assert.notNull(item.getItemId(), "有限更新的ItemId不能为空");
		Assert.notNull(item.getTransactionId(), "有限更新的Item，transactionId不能为空");
		
		item.setTableName(ItemCriteria.ITEM_TABLE_PREFIX + TablePartitionUtils.getTableMonth(item.getTransactionId()));
		return getSqlSessionTemplate().update("com.maicard.product.sql.Item.updateNoNull", item);		
	}
	@Override
	public int delete(String transactionId) {
		return getSqlSessionTemplate().delete("com.maicard.product.sql.Item.delete", transactionId);	
	}

	@Override
	public boolean exist(String transactionId) {
		Assert.notNull(transactionId, "尝试查询是否存在，但订单号为空");
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setTransactionId(transactionId);
		if(this.count(itemCriteria) > 0){
			return true;
		}
		return false;
	}


}
