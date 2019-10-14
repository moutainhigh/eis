package com.maicard.product.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.service.UuidService;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.DataWriteErrorException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.criteria.ProductDataCriteria;
import com.maicard.product.dao.ItemDao;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ItemDataService;
import com.maicard.product.service.ItemLogService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

@Service
public class ItemServiceImpl extends BaseService implements ItemService {

	@Resource
	private ItemDao itemDao;

	@Resource
	private ConfigService configService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private MessageService messageService;
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	@Resource
	private ItemLogService itemLogService;
	@Resource
	private ItemDataService itemDataService;
	@Resource
	private ProductService productService;

	@Resource
	private UuidService uuidService;

	private String messageBusName;
	private final String  historyTableName = "item_history";

	SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);



	private int remoteLockWaitingRetry = 0;

	//在新增Item时，是否先在本地插入数据，再传递给后端节点
	private boolean itemNoLocalInsert = false;
	private boolean handlerItem = false;

	@PostConstruct
	public void init(){
		messageBusName = configService.getValue(DataName.messageBusTransaction.toString(),1);
		remoteLockWaitingRetry = configService.getIntValue(DataName.remoteLockWaitingRetry.toString(),1);
		if(remoteLockWaitingRetry == 0){
			remoteLockWaitingRetry = 5;
		}
		itemNoLocalInsert = configService.getBooleanValue(DataName.itemNoLocalInsert.toString(), 0);
		handlerItem = configService.getBooleanValue(DataName.handlerItem.toString(),0);



	}


	@Override
	@IgnoreJmsDataSync
	public EisMessage insert(Item item){
		if(item == null){
			throw new RequiredObjectIsNullException("尝试新增的Item是空");
		}
		if(item.getTransactionTypeId() == 0){
			item.setTransactionTypeId(TransactionType.buy.id);
		}
		if(StringUtils.isBlank(item.getTransactionId())){
			item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
		}
		logger.info("item[" + item.getTransactionId() + "]的ownerId是:" + item.getOwnerId() + ",当前节点是否处理Item:"+handlerItem + ",当前系统是否先插入本地数据库:" + !itemNoLocalInsert);
		if(handlerItem || !itemNoLocalInsert){
			try{
				if(insertLocal(item) == 1){
					messageService.sendJmsDataSyncMessage(messageBusName, "itemService", "insertLocal", item);
					logger.debug("交易[" + item + "]已插入数据库，并发送同步请求[itemService.insertLocal(" + item + ")");
					return new EisMessage(OperateResult.success.getId(),"交易已插入");
				}
			}catch(Exception e){
				logger.debug("交易[" + item + "]无法插入数据库:" + e.getMessage());
				e.printStackTrace();
				return  new EisMessage(OperateResult.failed.getId(),"交易插入失败");
			}

		}
		logger.info("当前节点不负责处理Item，系统也不是先插入本地数据，把交易[" + item.getTransactionId() + "]发送到消息总线");
		return insertRemote(item);
	}

	//插入到缓存系统中，并发送到消息总线
	@IgnoreJmsDataSync
	private EisMessage insertRemote(Item item) {
		if(item.getItemId() < 1){
			long uuid = uuidService.insert(new Uuid());
			if(uuid < 1){
				logger.error("无法生成本地UUID");
				return null;
			}
			logger.debug("生成了本地UUID:" + uuid);
			long itemId = 0;
			try{
				itemId = Long.parseLong(configService.getServerId() + "" + uuid);
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.debug("生成了本地itemId:" + itemId);
			item.setItemId(itemId);
		}

		logger.info("把交易[" + item.getTransactionId() + "]发送到消息总线");
		EisMessage m = new EisMessage();
		//m.setNeedReply(true);
		m.setOperateCode(Operate.create.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("item", item);	
		m.setObjectType(ObjectType.item.toString());
		messageService.send(messageBusName, m);
		logger.info("交易[" + item + "]已发送到消息总线.");
		EisMessage result = new EisMessage(OperateResult.accept.getId(),"订单提交成功");
		result.setContent(item.getTransactionId());
		//m=null;
		return result;
	}


	@IgnoreJmsDataSync
	@Override
	@Transactional
	public int  insertLocal(Item item) throws Exception{

		if(item == null){
			logger.error("尝试插入的item为空");
			return -1;
		}
		if(item.getItemId() < 1){
			long uuid = uuidService.insert(new Uuid());
			if(uuid < 1){
				logger.error("无法生成本地UUID");
				return EisError.DATA_UPDATE_FAIL.id;
			}
			int itemId = Integer.parseInt(configService.getServerId() + "" + uuid);
			item.setItemId(itemId);
		}
		if(item.getEnterTime() == null){
			item.setEnterTime(new Date());
		}
		if(itemDao.exist(item.getTransactionId())){
			logger.warn("本地数据库已存在相同订单号:#{}的订单", item.getTransactionId());
			return 0;
		}
		int rs = itemDao.insert(item);
		if(rs != 1){
			throw new DataWriteErrorException("写入item到数据库异常,返回值是:" + rs);
		}

		if(item.getItemDataMap() != null){
			for(ProductData pd : item.getItemDataMap().values()){
				if(pd == null || pd.getDataValue() == null){
					continue;
				}
				pd.setProductId(item.getItemId());				
				pd.setCurrentStatus(item.getCurrentStatus());
				pd.setProductDataId(0);
				pd.setOwnerId(item.getOwnerId());
				pd.setObjectType(item.getObjectType());
				pd.setObjectId(item.getObjectTypeId());
				int dataRs = itemDataService.insert(pd);
				if(logger.isDebugEnabled()){
					logger.debug("为交易[" + item.getTransactionId() + "]插入新的itemData[" + pd.getDataCode() + "=>" + pd.getDataValue() + "]结果:" + dataRs);
				}
			}
		} else {
			logger.info("交易[" + item.getTransactionId() + "]没有任何扩展数据定义itemData");
		}
		logger.info("已向本地数据库插入交易品[" + item + "]");
		return rs;
	}

	@IgnoreJmsDataSync
	@Override
	public int update(Item item) {

		int actualRowsAffected = itemDao.update(item);
		if(logger.isDebugEnabled()){
			logger.debug("Item[" + item.getItemId() + "/" + item.getTransactionId() + "]更新结果:" + actualRowsAffected );
		}
		if(actualRowsAffected == 1){
			logger.debug("更新的Item[" + item.getItemId() + "/" + item.getTransactionId() + "]扩展数据:" + ( item.getItemDataMap() == null ? "空" : item.getItemDataMap().size()));
			if(item.getItemDataMap() != null){
				try{
					for(ProductData itemData : item.getItemDataMap().values()){
						if(itemData.getProductId() < 1){
							itemData.setProductId(item.getItemId());
						}
						itemDataService.update(itemData);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(logger.isDebugEnabled()){
				logger.debug("已更新本地数据库交易[" + item.getTransactionId() + "]");
			}
		}
		return actualRowsAffected;
	}

	/*public int delete(int itemId) {
		int actualRowsAffected = 0;

		Item _oldItem = itemDao.select(itemId);

		if (_oldItem != null) {
			actualRowsAffected = itemDao.delete(itemId);
		}
		if(actualRowsAffected == 1){
			//删除关联数据item_data
			itemDataService.deleteByProductId(itemId);
		}
		return actualRowsAffected;
	}*/
	/*
	public Item select(int itemId) {
		Item item = itemDao.select(itemId);
		if(item == null){
			return null;
		}
		afterFetch(item);
		return item;
	}*/

	public List<Item> list(ItemCriteria itemCriteria){
		
		fixBeginAndEndTime(itemCriteria);
		List<Item> itemList = itemDao.list(itemCriteria);
		logger.debug("根据查询条件[" + itemCriteria + "]查到的item有" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			return Collections.emptyList();
		}
		if(itemCriteria.getExtraDataMode() != null && itemCriteria.getExtraDataMode().equals(ItemCriteria.EXTRA_DATA_MODE_NONE)){
			return itemList;
		}
		for(int i = 0; i < itemList.size(); i++){
			itemList.get(i).setIndex(i+1);
			afterFetch(itemList.get(i));
		}
		return itemList;
	}

	public List<Item> listOnPage(ItemCriteria itemCriteria) {
		
		fixBeginAndEndTime(itemCriteria);
		List<Item> itemList = itemDao.listOnPage(itemCriteria);
		if(itemList == null || itemList.size() < 1){
			return Collections.emptyList();
		}
		if(itemCriteria.getExtraDataMode() != null && itemCriteria.getExtraDataMode().equals(ItemCriteria.EXTRA_DATA_MODE_NONE)){
			return itemList;
		}
		for(int i = 0; i < itemList.size(); i++){
			itemList.get(i).setIndex(i+1);
			afterFetch(itemList.get(i));
		}
		return itemList;
	}




	@Override
	public Item select(String transactionId) {
		Item item = selectSimple(transactionId);
		afterFetch(item);
		return item;
	}

	@Override
	public Item selectSimple(String transactionId) {
		if(StringUtils.isBlank(transactionId) || transactionId.length() < 10){
			logger.warn("订单号错误[" + transactionId + "]");
			return null;
		}
		ItemCriteria itemCriteria = new ItemCriteria();
		itemCriteria.setTransactionId(transactionId);
		List<Item> itemList = itemDao.list(itemCriteria);
		Item item = null;
		if(itemList  == null || itemList.size() < 1){

			/*if(!handlerItem){
				logger.debug("本节点不是交易处理节点，同时数据库中没有交易[" + transactionId + "],尝试从缓存中读取");
				item = _selectFromCache(transactionId);
				if(item != null){
					try{
						itemCache.remove(transactionId);
					}catch(Exception e){}
					afterFetch(item);
				}
				return item;

			} else {
				return null;
			}*/
			return null;
		}
		
		item = itemList.get(0);
		return item;
	}


	/*private Item _selectFromCache(String transactionId){
		if(itemCache == null){
			return null;
		}
		if(itemCache.get(transactionId) == null){
			return null;
		}
		try{
			Item item = itemCache.get(transactionId).clone();
			logger.debug("从缓存系统中读到交易[" + item.getTransactionId() + "].");			
			return item;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}*/





	@Override
	public Item fetchWithLock(ItemCriteria itemCriteria)  {
		Item item = itemDao.fetchWithLock(itemCriteria);
		if(item == null){
			return null;
		}
		afterFetch(item);
		return item;
	}
	
	/**
	 * 将一个Item的currentStatus设置为afterLockStatus，保证在设置前它的currentStatus必须是beforeLockStatus
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public int lock(Item item){
		return itemDao.lock(item);
	}


	/*@Override
	public Item fetchWithPartMoneyLock(ItemCriteria itemCriteria) {
		Item item = null;
		if(itemCriteria.getMoneyLockMode() == ItemCriteria.LOCK_EQ){
			item = itemDao.fetchWithPartMoneyLock1(itemCriteria);
			if(item != null){
				item.setLockStatus(ItemCriteria.LOCK_EQ);
			}
		}
		if(itemCriteria.getMoneyLockMode() == ItemCriteria.LOCK_SOURCE_PART){
			item = itemDao.fetchWithPartMoneyLock2(itemCriteria);
			if(item != null){
				item.setLockStatus(ItemCriteria.LOCK_SOURCE_PART);
			}
		}
		if(itemCriteria.getMoneyLockMode() == ItemCriteria.LOCK_DEST_PART){
			item = itemDao.fetchWithPartMoneyLock3(itemCriteria);
			if(item != null){
				item.setLockStatus(ItemCriteria.LOCK_DEST_PART);
			}
		}
		if(itemCriteria.getMoneyLockMode() == ItemCriteria.LOCK_BOTH_PART){
			item = itemDao.fetchWithPartMoneyLock4(itemCriteria);
			if(item != null){
				item.setLockStatus(ItemCriteria.LOCK_BOTH_PART);
			}
		}
		if(item == null){
			return null;
		}
		afterFetch(item);
		return item;
	}*/

	private void afterFetch(Item item){
		if(item == null){
			return;
		}
		
		ProductDataCriteria productDataCriteria = new  ProductDataCriteria();
		productDataCriteria.setProductId(item.getItemId());
		productDataCriteria.setTransactionId(item.getTransactionId());
		Map<String, ProductData> itemDataMap = null;
		try {
			itemDataMap = itemDataService.map(productDataCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		item.setItemDataMap(itemDataMap);
		//logger.info("chargeFromAccout:" + item.getChargeFromAccount());

	}

	/*@Override
	public void moveToHistory(ItemCriteria itemCriteria) throws Exception{

		if(itemCriteria == null){
			return;
		}
		if(itemCriteria.getEndTime() == null){
			//如果未设置结束时间，默认为昨天
			itemCriteria.setEndTime(new SimpleDateFormat(CommonStandard.statHourFormat.toString()).format( DateUtils.addDays(new Date(),-1)));
		}
		if(itemCriteria.getBeginTime() == null){
			//如果未设置开始时间，默认为昨天
			itemCriteria.setBeginTime(new SimpleDateFormat(CommonStandard.statDayFormat.toString()).format( DateUtils.addDays(new Date(),-1)));
		}
		itemCriteria.setQueryProcessingItem(DataName.only.toString());
		//itemCriteria.setCurrentStatus(new int[]{TransactionStatus.failed.getId(), TransactionStatus.success.getId()});

		List<Item> itemList = list(itemCriteria);
		logger.debug("得到" + (itemCriteria.getBeginTime() == null ? "最初" : "从[" + itemCriteria.getBeginTime()) + "]到" + (itemCriteria.getEndTime() == null ? "最近" : "[" + itemCriteria.getEndTime()) + "]的交易记录有[" + (itemList == null ? -1 : itemList.size()) + "]条");
		if(itemList == null || itemList.size() < 1){
			return;
		}
		int[] itemIds = new int[itemList.size()];
		for(int i = 0; i < itemList.size(); i++){
			itemIds[i] = itemList.get(i).getItemId();
		}
		itemCriteria.setItemIds(itemIds);
		int copyCount = itemDao.copyToHistory(itemCriteria);
		logger.debug("从当前交易表中复制的数据有[" + copyCount + "]条");
		if(copyCount == 0){
			return;
		}
		//从item_data中选择对应的数据并移动(复制和删除)到历史表
		ProductDataCriteria itemDataCriteria = new ProductDataCriteria();
		itemDataCriteria.setProductIds(itemIds);

		List<ProductData> itemDataList = itemDataService.list(itemDataCriteria);
		if(itemDataList == null || itemDataList.size() < 1){
			logger.warn("清理item时，没有找到对应的itemData");
		} else {
			logger.warn("清理item时，找到对应的itemData有[" + itemDataList.size() + "]条");

			int[] itemDataIds = new int[itemDataList.size()];
			for(int j = 0; j < itemDataList.size(); j++){
				itemDataIds[j] = itemDataList.get(j).getProductDataId();
			}
			itemDataCriteria.setProductDataIds(itemDataIds);
			itemDataCriteria.setBeginTime(itemCriteria.getBeginTime());
			itemDataCriteria.setEndTime(itemCriteria.getEndTime());
			int itemDataCopyRs = itemDataService.copyToHistory(itemDataCriteria);
			logger.debug("根据item复制到历史表的itemData有[" + itemDataCopyRs + "]条数据");
			if(itemDataCopyRs > 0){
				itemDataCriteria.setBeginTime(null);
				itemDataCriteria.setEndTime(null);
				int itemDataDeleteRs = itemDataService.delete(itemDataCriteria);
				logger.debug("根据item从当前表删除的itemData有[" + itemDataDeleteRs + "]条数据");
				if(itemDataCopyRs != itemDataDeleteRs){
					logger.error("复制的记录数和删除的记录数不一致");
					throw new DataWriteErrorException("在尝试复制和删除itemData时，复制的记录数和删除的记录数不一致");
				}
			}		
		}		
		ItemCriteria deleteCriteria = new ItemCriteria();
		deleteCriteria.setItemIds(itemIds);
		deleteCriteria.setQueryProcessingItem(DataName.only.toString());
		deleteCriteria.setCurrentStatus(new int[]{TransactionStatus.failed.getId(), TransactionStatus.success.getId()});
		int deleteCount =  itemDao.delete(itemCriteria);
		logger.debug("从当前交易表中删除的数据有[" + deleteCount + "]条");
	}
	 */
	@Override
	public int count(ItemCriteria itemCriteria) {
		//logger.debug("chargeFromAccount=" + itemCriteria.getChargeFromAccount() + ",productId=" + itemCriteria.getProductId());
		fixBeginAndEndTime(itemCriteria);
		return itemDao.count(itemCriteria);
	}


	//释放锁定金额为请求金额
	@Override
	public int releaseItemWithFrozenMoney(Item item) {
		if(item == null){
			if(logger.isDebugEnabled()){
				logger.debug("尝试释放锁定金额的item为空");
			}
			return -1;
		}
		if(item.getFrozenMoney() <= 0f){
			if(logger.isDebugEnabled()){
				logger.debug("尝试释放锁定金额的item，释放金额是0，不需要进行释放");
			}
			return 0;
		}
		if(logger.isDebugEnabled()){
			logger.debug("尝试释放锁定金额的item，释放金额是:" 	 + item.getFrozenMoney());
		}	
		ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		List<Item> oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询准备释放锁定金额的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("准备释放锁定金额的交易[" +  oldItemList.get(0) + ",processCount="	+ oldItemList.get(0).getProcessCount());
			if(oldItemList.get(0).getCurrentStatus() == TransactionStatus.waitingNotify.getId()){
				logger.info("准备锁定金额的交易[" + item.getTransactionId() + "]处于等待异步通知中，不释放");
				return 0;
			}
		}
		int rs =  itemDao.releaseItemWithFrozenMoney(item);
		logger.info("交易[" + item.getTransactionId() + "]释放结果:" + rs);
		itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		oldItemList = null;
		oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询刚刚释放锁定金额的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("刚刚释放锁定金额的交易[" + oldItemList.get(0) + ",processCount="
					+ oldItemList.get(0).getProcessCount());
		}
		return rs;
	}

	//释放锁定金额为请求金额
	@Override
	public int releaseItemWithFrozenMoney2(Item item) {
		if(item == null){
			if(logger.isDebugEnabled()){
				logger.debug("尝试释放锁定金额2的item为空");
			}
			return -1;
		}
		if(item.getFrozenMoney() <= 0f){
			if(logger.isDebugEnabled()){
				logger.debug("尝试释放锁定金额2的item，释放金额是0，不需要进行释放");
			}
			return 0;
		}
		if(logger.isDebugEnabled()){
			logger.debug("尝试释放锁定金额2的item，释放金额是:" 	 + item.getFrozenMoney());
		}	
		ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		List<Item> oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询准备释放锁定金额2的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("准备释放锁定金额2的交易[" + oldItemList.get(0) + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
		}
		int rs =  itemDao.releaseItemWithFrozenMoney2(item);
		logger.info("释放锁定金额2交易[" + item.getTransactionId() + "]释放结果:" + rs);
		itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		oldItemList = null;
		oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询刚刚释放锁定金额2的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("刚刚释放锁定金额2的交易[" + oldItemList.get(0) + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
		}
		return rs;
	}


	//增加交易的成功金额为指定的锁定金额，并扣除锁定金额
	@Override
	public int plusItemMoneyWithFrozenMoney(Item item) {
		if(item == null){
			if(logger.isDebugEnabled()){
				logger.debug("尝试扣除锁定金额的item为空");
			}
			return -1;
		}
		if(item.getFrozenMoney() <= 0f){
			logger.warn("尝试扣除锁定金额的item[" + item.getTransactionId() + "]，扣除金额是0，不需要进行扣除");

			return 0;
		}
		logger.debug("尝试扣除锁定金额的item，扣除锁定金额是:" 	 + item.getFrozenMoney());

		ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		List<Item> oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询准备plusItemMoneyWithFrozenMoney的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("准备plusItemMoneyWithFrozenMoney的交易[" + oldItemList.get(0) + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
			/*if(oldItemList.get(0).getFrozenMoney() > 0){
				if(item.getTtl() == 0){
					logger.warn("订单[" + item.getTransactionId() + "]的TTL为0，强制为1分钟");
					item.setTtl(60);
				}
				logger.info("由于刚刚查询到的交易[" + item.getTransactionId() + "]还有锁定金额，因此将订单TTL增加一倍");
				item.setTtl(item.getTtl() * 2);

			}*/
			/*if(item.getCurrentStatus() == TransactionStatus.success.getId()){
				if(oldItemList.get(0).getFrozenMoney() > item.getFrozenMoney()){
					logger.info("由于刚刚查询到的交易[" + item.getTransactionId() + "]还有锁定金额并且比当前锁定金额要大，因此将订单状态有成功改为新订单");
					item.setCurrentStatus(TransactionStatus.newOrder.getId());			
				}

			}*/

		}
		int rs =  itemDao.plusItemMoneyWithFrozenMoney(item);
		itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		oldItemList = null;
		oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询刚刚plusItemMoneyWithFrozenMoney的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("刚刚plusItemMoneyWithFrozenMoney的交易[" + oldItemList.get(0) + ",lockid="
					+ oldItemList.get(0).getLockGlobalUniqueId());
		}
		return rs;
	}

	//修改Item的请求金额-成功金额，成功金额+成功金额
	@Override
	public int plusItemMoneyWithoutFrozenMoney(Item item) {
		if(item == null){
			if(logger.isDebugEnabled()){
				logger.debug("尝试成功金额的item为空");
			}
			return -1;
		}

		if(logger.isDebugEnabled()){
			logger.debug("尝试成功金额的item，成功金额是:" 	 + item.getSuccessMoney());
		}		
		return itemDao.plusItemMoneyWithoutFrozenMoney(item);
	}

	//把item列表转换为CSV纯文本
	@Override
	public String generateCsv(List<Item> itemList) {
		StringBuffer sb = new StringBuffer();
		sb.append("order_id,transaction_id,product_code,product_name,partner,card_serialnumber,card_password,request_money,success_money,enter_time,close_time,current_status\n");
		for(Item item : itemList){
			sb.append(item.getInOrderId());
			sb.append(",");
			sb.append(item.getTransactionId());
			sb.append(",");			
			Product product = productService.select(item.getProductId());
			if(product != null){
				sb.append(product.getProductCode());
			}
			sb.append(",");	
			if(product != null){
				sb.append(product.getProductName());
			}
			sb.append(",");
			sb.append(item.getChargeFromAccount());
			sb.append(",");	
			if(item.getItemDataMap() != null && item.getItemDataMap().get(DataName.productSerialNumber.toString()) != null){
				sb.append(item.getItemDataMap().get(DataName.productSerialNumber.toString()).getDataValue());				
			}
			sb.append(",");	
			if(item.getItemDataMap() != null && item.getItemDataMap().get(DataName.productPassword.toString()) != null){
				sb.append(item.getItemDataMap().get(DataName.productPassword.toString()).getDataValue());				
			}
			sb.append(",");	
			sb.append(item.getLabelMoney());
			sb.append(",");	
			sb.append(item.getSuccessMoney());
			sb.append(",");	
			if(item.getEnterTime() == null){
				sb.append("");
			} else {
				sb.append(sdf.format(item.getEnterTime()));
			}
			sb.append(",");	
			if(item.getCloseTime() == null){
				sb.append("");
			} else {
				sb.append(sdf.format(item.getCloseTime()));
			}
			sb.append(",");	
			sb.append(item.getCurrentStatus());
			sb.append("\r\n");	
		}
		return sb.toString();
	}


	@Override
	public List<Item> listProcessTimeout(ItemCriteria itemCriteria) {
		if(itemCriteria == null){
			return null;
		}
		List<Item> itemList = itemDao.listProcessTimeout(itemCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("处理时间超过TTL" + itemCriteria.getTimeoutSeconds() + "秒或超时规则[" + itemCriteria.getTimeoutPolicy() + "]的交易有" + (itemList == null ? "空" : itemList.size()));
		}
		if(itemList == null || itemList.size() < 1){
			return null;
		}
		try{
			for(Item item : itemList){		
				afterFetch(item);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return itemList;
	}
	public String downloadCsv(ItemCriteria itemCriteria) {
		try {
			return itemDao.downloadCsv(itemCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	


	@Override
	public int changeStatus(Item item) {
		return itemDao.changeStatus(item);
	}


	private void fixBeginAndEndTime(ItemCriteria itemCriteria){
		if (itemCriteria != null && itemCriteria.getTableName() != null && itemCriteria.getTableName().equals(historyTableName) || itemCriteria.getQueryProcessingItem() == null || itemCriteria.getQueryProcessingItem().equals(DataName.only.toString())) {
			itemCriteria.setEnterTimeBegin(null); 
			itemCriteria.setEnterTimeEnd(null); 
			return;
		}
		if(StringUtils.isNotBlank(itemCriteria.getTransactionId())){
			Date orderDate = globalOrderIdService.getDateByTransactionId(itemCriteria.getTransactionId());
			if(orderDate == null){
				logger.warn("无法根据订单号[" + itemCriteria.getTransactionId() + "]判断其日期");
			} else {
				itemCriteria.setEnterTimeBegin(DateUtils.truncate(orderDate, Calendar.DAY_OF_MONTH));
				itemCriteria.setEnterTimeEnd(DateUtils.ceiling(orderDate, Calendar.DAY_OF_MONTH));
				logger.debug("根据订单号[" + itemCriteria.getTransactionId() + "]将查询时间设置为:" + sdf.format(itemCriteria.getEnterTimeBegin()) + "=>" + sdf.format(itemCriteria.getEnterTimeEnd()));
			}
			return;
		} 

		if(itemCriteria.getEnterTimeBegin() == null){ 
			/* 从本月第一天改为7天前，并去掉了Item.xml.list中在指定条件下才查询item表的限制（但是这个修改应与chaoka系统无关），即无条件查询item表
			 * 以支撑跨月查询
			 */				
			itemCriteria.setEnterTimeBegin(DateUtils.truncate(DateUtils.addDays(new Date(), -CommonStandard.DEFAULT_QUERY_DAY), Calendar.DAY_OF_MONTH) ); 
			logger.debug("查询条件未定义开始时间，设置为"+ CommonStandard.DEFAULT_QUERY_DAY + "天前:" +  sdf.format(itemCriteria.getEnterTimeBegin()));

		} else {
			logger.debug("查询条件中定义的开始时间是:" +  sdf.format(itemCriteria.getEnterTimeBegin()));

		}
		
		if(itemCriteria.getEnterTimeEnd() == null){ 
					
			itemCriteria.setEnterTimeEnd( DateUtils.addSeconds(DateUtils.ceiling(new Date(), Calendar.MONTH),-1) ); 
			logger.debug("查询条件未定义结束时间，设置为本月最后一天:" +  sdf.format(itemCriteria.getEnterTimeEnd()));
			

		} else {
			logger.debug("查询条件中定义的结束时间是:" +  sdf.format(itemCriteria.getEnterTimeEnd()));

		}
		
		/*if(StringUtils.isBlank(itemCriteria.getEndTime())){ //未定义结束时间，为本月最后一天
			itemCriteria.setEndTime(new	SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addSeconds(DateUtils.ceiling(new Date(), Calendar.MONTH),-1)) + "24");
			logger.debug("查询条件未定义结束时间，设置为本月最后一天:" + itemCriteria.getEndTime());
		} else {
			try{
				itemCriteria.setEndTime( new SimpleDateFormat(CommonStandard.statDayFormat).format(fmt.parse(itemCriteria.getEndTime()))+ "24");
			}catch(Exception e){}
			if(!itemCriteria.getEndTime().substring(0,6).equals(itemCriteria.getBeginTime().substring(0,6))){
				try{
					itemCriteria.setEndTime(new	SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addSeconds(DateUtils.ceiling(new SimpleDateFormat(CommonStandard.statDayFormat).parse(itemCriteria.getBeginTime()), Calendar.MONTH),-1)) + "24");
				}catch(Exception e){}
				logger.debug("查询条件的结束时间与开始时间不一致，设置为开始时间月份的最后一天:" + itemCriteria.getEndTime());
			} else {
				logger.debug("查询条件的结束时间是:" + itemCriteria.getEndTime());

			}
		}*/

	}


/*

	private Item _fetchWithLockAsyncLocal(ItemCriteria itemCriteria){		

		Item item =  itemDao.fetchWithLock(itemCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("根据锁定ID[" + itemCriteria.getLockGlobalUniqueId() + "]锁定的交易是[" + item + "]");
		}
		if(item != null){
			messageService.sendJmsDataSyncMessage(messageBusName, "itemService", "update", item);
		}
		return item;
	}*/
	

	/*
	 * 按照条件仅返回一条数据
	 */
	@Override
	public Item selectOneForMatch(ItemCriteria itemCriteria) {
		itemCriteria.setQueryProcessingItem(DataName.only.toString());
		List<Item> itemList = itemDao.listForMatch(itemCriteria);
		Item item = null;
		logger.debug("为匹配所返回的交易数据是" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			return null;
		} else if(itemList.size() == 1 || itemCriteria.getMaxRandomCount() <= 1){
			item = itemList.get(0);
		} else {
			int rand = RandomUtils.nextInt(itemList.size());
			logger.info("尝试随机返回一条记录[" + rand + "/" + itemCriteria.getMaxRandomCount() + "]");
			item = itemList.get(rand);	
		}
		if(item != null){
			afterFetch(item);
		}
		return item;
	}

	/*
	 * 按照条件返回指定条数的数据
	 */
	@Override
	public List<Item> listForMatch(ItemCriteria itemCriteria) {
		itemCriteria.setQueryProcessingItem(DataName.only.toString());		
		List<Item> itemList = itemDao.listForMatch(itemCriteria);
		logger.debug("为匹配所返回的交易数据是" + (itemList == null ? "空" : itemList.size()));
		if(itemList == null || itemList.size() < 1){
			return null;
		}
		for(Item item : itemList){
			afterFetch(item);
		}
		return itemList;
	}

	

	/*
	 * 更新指定的item，并使其request_money=request_money-frozenMoney，frozen_money=frozen_money+frozenMoney
	 * 且在当前requestMoney>frozenMoney的前提下才能更新
	 */
	@Override
	public int lockUpdateAndRelaseAdditinalFrozenMoney(Item item) {
		/*ItemCriteria itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		itemCriteria2.setItemAccordingFrontUser(itemAccordingFrontUser);
		List<Item> oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询准备进行锁定的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("准备进行锁定的交易，锁定前指定状态:" + item.getBeforeLockStatus() + "，锁定后状态:" + item.getAfterLockStatus() + "，[" + item.getTransactionId() + "],requestMoney=" + oldItemList.get(0).getRequestMoney() + ",successMoney=" + oldItemList.get(0).getSuccessMoney() + ",frozenMoney=" + oldItemList.get(0).getFrozenMoney() + ",currentStatus=" + oldItemList.get(0).getCurrentStatus() + ",processCount="
					+ oldItemList.get(0).getProcessCount());

		}*/
		int rs =  itemDao.lockUpdateAndRelaseAdditinalFrozenMoney(item);

		/*itemCriteria2 = new ItemCriteria();
		itemCriteria2.setTransactionId(item.getTransactionId());
		itemCriteria2.setQueryProcessingItem(DataName.only.toString());
		itemCriteria2.setItemAccordingFrontUser(itemAccordingFrontUser);
		oldItemList = itemDao.list(itemCriteria2);
		if(oldItemList == null || oldItemList.size() < 1){
			logger.error("无法查询锁定后的交易[" + item.getTransactionId() + "]");
		} else {
			logger.info("锁定后的交易[" + item.getTransactionId() + "],requestMoney=" + oldItemList.get(0).getRequestMoney() + ",successMoney=" + oldItemList.get(0).getSuccessMoney() + ",frozenMoney=" + oldItemList.get(0).getFrozenMoney() + ",currentStatus=" + oldItemList.get(0).getCurrentStatus() + ",processCount="
					+ oldItemList.get(0).getProcessCount());			
		}*/
		return rs;
	}


	@Override
	public void fixOutStatus(Item item) {
		if(item.getCurrentStatus() == TransactionStatus.success.id || item.getCurrentStatus() == TransactionStatus.newOrder.id){
			return;
		}
		//解析卡密的失败规则
		int errorCode = item.getIntConfig(DataName.outErrorCode.toString());

		if(errorCode == 0){
			logger.info("卡密[" + item.getTransactionId() + "]未设置统一错误代码，使用内部状态:" + item.getCurrentStatus());
			errorCode = item.getCurrentStatus();
		}
		logger.info("卡密[" + item.getTransactionId() + "]外部状态设置为:" + errorCode);
		item.setOutStatus(errorCode);		
	}


	@Override
	public void applyTtl(Item item, Product product) {
		if(product == null || item == null){
			return;
		}
		int productTtl = product.getTransactionTtl();
		if(product.getProductDataMap() == null || product.getProductDataMap().size() < 1){
			item.setTtl(productTtl);
			return;
		}

		String ttlType = null;
		if(item.getTransactionTypeId() == TransactionType.buy.getId()){
			ttlType = "buyTtl";
		} else if(item.getTransactionTypeId() == TransactionType.sale.getId()){
			ttlType = "saleTtl";
		} else {
			return;
		}
		if(product.getProductDataMap().containsKey(ttlType)){
			int ttl = NumericUtils.getNumeric(product.getProductDataMap().get(ttlType).getDataValue());
			if(ttl > 0){
				item.setTtl(ttl);
				return;
			}
		}
		item.setTtl(productTtl);
		return;

	}
	@Override
	public int updateNoNull(Item item) {
		return itemDao.updateNoNull(item);
	}


	@Override
	@Transactional
	public int delete(String transactionId) throws Exception {
		Item item = select(transactionId);
		if(item == null){
			logger.warn("找不到尝试删除的交易[" + transactionId + "]");
		}
		ProductDataCriteria itemDataCriteria = new ProductDataCriteria();
		//设置transactionId便于分表
		itemDataCriteria.setTransactionId(transactionId);
		itemDataCriteria.setProductId(item.getItemId());
		itemDataService.delete(itemDataCriteria);
		return itemDao.delete(transactionId);
	}

	@Override
	public boolean recycle(Item item){
		Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error("找不到准备回收订单[" + item.getTransactionId() + "]对应的产品:" + item.getProductId());
			return false;
		}	
		//productService.writeAmount(product, item.getCount(), false);
		//----XXX 不再修改订单状态，交由存储过程remove_timeout_temp_order进行，这里只回收产品数量
		int rs = changeStatus(item);
		logger.debug("将回收订单[" + item.getTransactionId() + "]状态修改为:" + item.getCurrentStatus() + ",结果:" + rs);
		if(rs == 1){
			productService.writeAmount(product, item.getCount(), false);
			messageService.sendJmsDataSyncMessage(null, "itemService", "changeStatus", item);
		}
		return true;
	}


	@Override
	public List<Item> lock(ItemCriteria itemCriteria) {
		
		//TODO 尚未实现
		return Collections.emptyList();
	}


	

}



