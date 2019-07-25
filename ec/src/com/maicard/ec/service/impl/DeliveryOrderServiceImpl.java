package com.maicard.ec.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.ec.criteria.DeliveryOrderCriteria;
import com.maicard.ec.criteria.FeeAdjustCriteria;
import com.maicard.ec.dao.DeliveryOrderDao;
import com.maicard.ec.domain.AddressBook;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.domain.FeeAdjust;
import com.maicard.ec.service.AddressBookService;
import com.maicard.ec.service.DeliveryCompanyService;
import com.maicard.ec.service.DeliveryOrderService;
import com.maicard.ec.service.DeliveryPriceService;
import com.maicard.ec.service.FeeAdjustService;
import com.maicard.ec.utils.AreaTrimUtils;
import com.maicard.money.domain.Price;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EOEisObject;
import com.maicard.common.domain.EVEisObject;
import com.maicard.common.domain.Uuid;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.UuidService;

@Service
public class DeliveryOrderServiceImpl  extends BaseService implements DeliveryOrderService{


	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private ConfigService configService;

	@Resource
	private DeliveryCompanyService deliveryCompanyService;
	@Resource
	private DeliveryOrderDao deliveryOrderDao;

	@Resource
	private DeliveryPriceService deliveryPriceService;

	@Resource
	private FeeAdjustService feeAdjustService;

	@Resource
	private AddressBookService addressBookService;

	@Resource
	private ProductService productService;

	@Resource
	private UuidService uuidService;

	final String globalDefaultFromArea = "北京#北京";
	

	@Override
	public int insert(DeliveryOrder deliveryOrder) {
		if(deliveryOrder.getDeliveryOrderId() <= 0){
			long uuid = uuidService.insert(new Uuid());
			if(uuid <= 0){
				logger.error("无法创建本地UUID，因此无法建立DeliveryOrder主键");
				return -1;
			}
			long newUuid = Long.parseLong(configService.getServerId() + "" + uuid);
			deliveryOrder.setDeliveryOrderId(newUuid);

		}
		return deliveryOrderDao.insert(deliveryOrder);
	}
	@Override
	public int update(DeliveryOrder deliveryOrder) {
		int actualRowsAffected = 0;
		DeliveryOrder _oldDeliveryOrder = deliveryOrderDao.select(deliveryOrder.getDeliveryOrderId());
		if (_oldDeliveryOrder !=null){
			deliveryCompanyService.checkInfo(deliveryOrder);
			actualRowsAffected=deliveryOrderDao.update(deliveryOrder);
		}
		return actualRowsAffected;
	}
	@Override
	public List<DeliveryOrder> list(DeliveryOrderCriteria deliveryOrderCriteria) {
		return deliveryOrderDao.list(deliveryOrderCriteria);
	}
	@Override
	public int count(DeliveryOrderCriteria productCriteria) {
		// TODO Auto-generated method stub
		try{
			return deliveryOrderDao.count(productCriteria);
		}
		catch(Exception e){}
		return 0;
	}
	@Override
	public List<DeliveryOrder> listOnPage(DeliveryOrderCriteria deliveryOrderCriteria) {
		return deliveryOrderDao.listOnPage(deliveryOrderCriteria);
	}

	@Override
	public DeliveryOrder select(long deliveryOrderId) {
		return deliveryOrderDao.select(deliveryOrderId);
	}
	@Override
	public int delete(long deliveryOrderId) {
		return deliveryOrderDao.delete(deliveryOrderId);
	}
	


	@Override
	public DeliveryOrder generateDeliveryOrder(long addressBookId, Item[] items, String refOrderId, String transactionType, String identify) throws Exception {
		Assert.isTrue(addressBookId > 0, "尝试计算邮费必须有配送地址");
		Assert.isTrue(items != null && items.length > 0, "尝试计算邮费至少要有一个订单");

		AddressBook addressBook = addressBookService.select(String.valueOf(addressBookId));

		Assert.notNull(addressBook, "找不到计算邮费的地址:" + addressBookId);
		Assert.notNull(addressBook.getProvince(),"计算邮费的地址[" + addressBookId + "]没有省份信息");
		
		DeliveryOrder totalDeliveryOrder = null;
		//计算每个商品的邮费
		for(Item item : items){
			DeliveryOrder subOrder = generateDeliveryOrder(item, addressBook);
			if(subOrder != null && subOrder.getFee() != null){
				if(totalDeliveryOrder == null){
					totalDeliveryOrder = subOrder.clone();
					totalDeliveryOrder.setFee(new Price());
					totalDeliveryOrder.setDeliveryOrderId(0);
				}
				totalDeliveryOrder.setFee(Price.add(totalDeliveryOrder.getFee(), subOrder.getFee()));
			}
		}
		if(totalDeliveryOrder == null){
			logger.warn("为订单[" + refOrderId + "]计算的总配送单是空");
		} else {
			logger.info("为订单[" + refOrderId + "]计算的总配送单费用是:" + totalDeliveryOrder.getFee());

		}
		return totalDeliveryOrder;
		
		
		

		/*ActivityCriteria activityCriteria = new ActivityCriteria();
		activityCriteria.setActivityType(ActivityCriteria.ACTIVITY_TYPE_BUY);
		List<Activity> activityList = activityService.list(activityCriteria);
		if(activityList == null || activityList.size() < 1){
			logger.debug("当前没有任何类型为[" + ActivityCriteria.ACTIVITY_TYPE_BUY + "]的活动");
		} else {
			for(Activity activity : activityList){
				if(activity.getProcessor() == null){
					logger.warn("购买活动[" + activity.getActivityId() + "]没有定义活动处理器");
					continue;
				}
				ActivityProcessor p = (ActivityProcessor)applicationContextService.getBean(activity.getProcessor());
				p.execute(null, activity, deliveryOrder, feeMap);
			}
		}

		Price finalPrice = new Price();

		for(Price p : feeMap.values()){
			Price.add(finalPrice, p);			
		}
		logger.debug("为配送单[" + deliveryOrder + "]生成的最终价格是:" + finalPrice);
		deliveryOrder.setFee(finalPrice);

		return deliveryOrder;*/
	}



	/**
	 * 根据交易，得到它的发货地区
	 * @param item
	 * @return
	 */
	@Override
	public String getFromArea(Item item) {
		if(item.getObjectType().equalsIgnoreCase(ObjectType.node.name())) {
			return null;
		}
		if(item.getObjectType().equalsIgnoreCase(ObjectType.document.name())) {
			return null;
		}
		Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error("找不到产品:" + item.getProductId() + ",无法获取发货地");
			return null;
		}
		//FIXME 
		return null;//getFromArea(product);
	}
	
	@Override
	public String getFromArea(EOEisObject object) {
		Assert.notNull(object,"尝试查找发货地的对象不能为空");
		
		String defaultFromArea = object.getExtraValue(DataName.defaultFromArea.toString());
		if(defaultFromArea != null){
			logger.info("找到了对象[" + object.getObjectType() + "/" + object.getId() + "]的默认发货地:" + defaultFromArea);
			return defaultFromArea;
		}

		logger.info("找不到对象[" + object.getObjectType() + "/" + object.getId() + "]的默认发货地，尝试查找系统配置");
		defaultFromArea = configService.getValue(DataName.defaultFromArea.toString(), object.getOwnerId());
		if(defaultFromArea == null){
			defaultFromArea = globalDefaultFromArea;
			logger.info("系统未配置默认发货地，使用全局默认配置:" + defaultFromArea);
		}
		return defaultFromArea;	
	}
	
	@Override
	public DeliveryOrder generateDeliveryOrder(Item item, AddressBook addressBook){
		Product product = productService.select(item.getProductId());
		if(product == null){
			logger.error("找不到订单对应的产品:" + item.getProductId());
		}
		
		/*Price itemPrice = items[0].getPrice();
		if(itemPrice == null){
			logger.warn("交易[" + items[0].getTransactionId() + "]没有价格实例");
		}*/

		//获取发货地
		String fromProvince = null;
		String fromArea = null;
		
		//FIXME
		String from = "";// getFromArea(product);
		String[] data = from.split("#");
		if(data.length != 2){
			logger.error("无法解析发货地配置:" + from);
			return null;
		}
		fromProvince = data[0];
		fromArea = data[1];
		
		
		
		DeliveryOrder deliveryOrder = new DeliveryOrder(addressBook);
		deliveryOrder.setAddressBookId(addressBook.getAddressBookId());
		deliveryOrder.setRefOrderId(item.getTransactionId());
		//deliveryOrder.setObjectType(transactionType);
		//deliveryOrder.setRefOrderId(refOrderId);
		int weight = (int)item.getLongExtraValue(DataName.goodsWeight.toString());
		if(weight <= 0){
			logger.warn("订单[" + item.getTransactionId() + "]没有设置商品重量");
		} else {
			int count = item.getCount();
			if(count < 1){
				count = 1;
			}
			deliveryOrder.setGoodsWeight(weight * count);
			logger.debug("订单[" + item.getTransactionId() + "]的商品重量是:" + weight + ",乘以数量" + count + "后的总重量是:" + deliveryOrder.getGoodsWeight());
		}

		deliveryOrder.setIdentify("product#" + item.getProductId());
		deliveryOrder.setFromProvince(fromProvince);
		deliveryOrder.setFromArea(fromArea);
		
		if(addressBook.getProvince() == null){
			logger.error("地址[" + addressBook + "]没有收货地省份信息，无法生成配送单");
			return null;
		}
		//处理省份		
		deliveryOrder.setToProvince(AreaTrimUtils.trimArea(addressBook.getProvince()));
		

		if(addressBook.getCity() != null){
			deliveryOrder.setToArea(AreaTrimUtils.trimArea(addressBook.getCity()));
		} else if(addressBook.getProvince() != null){
			deliveryOrder.setToArea("*");
		} 
		
		//得到最优快递公司
		long deliveryPartnerId = product.getLongExtraValue(DataName.deliveryCompanyId.toString());
		if(deliveryPartnerId > 0){
			deliveryOrder.setDeliveryCompanyId(deliveryPartnerId);
		} else {
			deliveryOrder.setDeliveryCompanyId(deliveryCompanyService.getBestDeliveryCompanyId(deliveryOrder));
		}
		
		//计算标准价格
		Price standardPrice = deliveryPriceService.calculatePrice(deliveryOrder);
		logger.debug("为订单[" + deliveryOrder + "]计算标准价格是:" + standardPrice);
		
		
		
		//根据价格Price查找是否有对应的价格减免
		
		
		
		
		FeeAdjustCriteria feeAdjustCriteria = new FeeAdjustCriteria(deliveryOrder.getOwnerId());
		feeAdjustCriteria.setFromProvince(deliveryOrder.getFromProvince());
		feeAdjustCriteria.setFromArea(deliveryOrder.getFromArea());
		feeAdjustCriteria.setToProvince(deliveryOrder.getToProvince());
		feeAdjustCriteria.setToArea(deliveryOrder.getToArea());
		//feeAdjustCriteria.applyPriceAttributes(itemPrice);
		feeAdjustCriteria.setDeliveryCompanyId(deliveryOrder.getDeliveryCompanyId());
		feeAdjustCriteria.setBeginTime(new Date());
		feeAdjustCriteria.setEndTime(new Date());
		feeAdjustCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<FeeAdjust> feeAdjustList = feeAdjustService.list(feeAdjustCriteria);
		boolean clearDeliveryFee = false;
		/*if(feeAdjustList == null || feeAdjustList.size() < 1){
			logger.debug("当前没有针对订单[" + deliveryOrder + "]的价格调整配置");
		} else {
			for(FeeAdjust feeAdjust : feeAdjustList){
				if(feeAdjust.getFee() == null){
					logger.error("价格调整配置[" + feeAdjust.getFeeAdjustId() + "]的调整价格是空");
					continue;
				}
				if(feeAdjust.isClearDeliveryFee()){
					logger.info("当前价格调整[" + feeAdjust.getFeeAdjustId() + "]要求清空快递费");
					clearDeliveryFee = true;
					break;
					
				}
				finalPrice = Price.add(finalPrice, feeAdjust.getFee());		
				logger.debug("为配送单[" + deliveryOrder + "]应用价格调整配置[" + feeAdjust.getFeeAdjustId() + "]的调整价格:" + feeAdjust.getFee());
			}
		}
		if(clearDeliveryFee){
			finalPrice = new Price();
		}
		logger.debug("为配送单[" + deliveryOrder + "]生成的最终价格是:" + finalPrice);*/
		deliveryOrder.setFee(standardPrice);
		return deliveryOrder;
	}
	
}
