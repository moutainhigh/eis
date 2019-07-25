package com.maicard.product.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSON;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.OpEisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CenterDataService;
import com.maicard.method.Operatable;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.domain.ProductData;
import com.maicard.product.service.ProductService;
import com.maicard.product.service.StockService;
import com.maicard.product.utils.DataUtils;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.UserRelationService;
import com.maicard.site.dao.DocumentDao;
import com.maicard.site.dao.NodeDao;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;

@Service
public class StockServiceImpl extends BaseService implements StockService{

	@Resource
	private CenterDataService centerDataService;
	
	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private NodeDao nodeDao;

	@Resource
	private NodeService nodeService;

	@Resource
	private DocumentDao documentDao;
	
	@Resource
	private ProductService productService;

	@Resource
	private DocumentService documentService;

	@Resource
	private UserRelationService userRelationService;

	@Resource
	private PriceService priceService;

	@Override
	public int changeStock(String objectType, long objectId, int offset) {
		int count = this.getAvaiableCount(objectType, objectId);
		if(count < 0){
			logger.debug("库存:{}/{}的数值为:{}，不更新");
			return 1;
		}
		int newCount = count + offset;
		if(newCount < 0) {
			newCount = 0;
		}

		Operatable targetObject = this.getTargetObject(objectType, objectId);
		long soldCount = targetObject.getLongExtraValue(DataName.soldCount.name());
		//如果是卖出，则offset应当是负数，这里的卖出总数就应该是 --得正
		soldCount -= offset;
		if(soldCount < 0) {
			soldCount = 0;
		}
		targetObject.setExtraValue(DataName.soldCount.name(), String.valueOf(soldCount));
		//XXX 写入数据库是直接写入最新数值，比如剩余数量1000个，，写入缓存是写入要修改的数值，比如减少5个为-5
		if(newCount % 5 == 0) {
			//写入数据库
			this._writeToDb(objectType, objectId, newCount, offset);
		}
		return this._writeOffsetToCache(objectType, objectId, offset);

	}

	/**
	 * 把新的库存数量写入数据库，同时把已售出数量+offset
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-21
	 */
	private int _writeToDb(String objectType, long objectId, int amount, int offset){
		if(objectType.equals(ObjectType.node.name())){
			//强制从数据库获取
			Node node = nodeDao.selectNoCache((int)objectId);
			if(node == null) {
				logger.error("找不到:{}/{}的对象", objectType, objectId);
				return 0;
			} 
			node.setExtraValue(DataName.availableCount.name(), String.valueOf(amount));
			/*long soldCount = node.getLongExtraValue(DataName.soldCount.name());
			//如果是卖出，则offset应当是负数，这里的卖出总数就应该是 --得正
			soldCount -= offset;
			if(soldCount < 0) {
				soldCount = 0;
			}
			node.setExtraValue(DataName.soldCount.name(), String.valueOf(soldCount));*/
			nodeDao.update(node);
			return amount;

		} else {
			//强制从数据库获取
			Document document = documentDao.selectNoCache((int)objectId);
			if(document == null) {
				logger.error("找不到:{}/{}的对象", objectType, objectId);
				return 0;
			} 
			document.setExtraValue(DataName.availableCount.name(), String.valueOf(amount));

			/*long soldCount = document.getLongExtraValue(DataName.soldCount.name());
			//如果是卖出，则offset应当是负数，这里的卖出总数就应该是 --得正
			soldCount -= offset;
			if(soldCount < 0) {
				soldCount = 0;
			}
			document.setExtraValue(DataName.soldCount.name(), String.valueOf(soldCount));*/
			return amount;

		}

	}


	@Override
	public int getAvaiableCount(String objectType, long objectId) {
		int distributedCount = _getAmountFromCache(objectType, objectId);
		if(distributedCount == -999){
			//-999表示没有取到数值
			distributedCount =  _getAmountFromDb(objectType, objectId);
			this._writeCountToCache(objectType, objectId, distributedCount);
		} 
		return distributedCount;

	}



	public int _getAmountFromDb(String objectType, long objectId){
		if(objectType.equals(ObjectType.node.name())){
			//强制从数据库获取
			Node node = nodeDao.selectNoCache((int)objectId);
			if(node == null) {
				logger.error("找不到:{}/{}的对象", objectType, objectId);
				return 0;
			} else {
				int amount = (int)node.getLongExtraValue(DataName.availableCount.name());
				logger.debug("从数据库读取:{}/{}对象的剩余数量是:{}", objectType, objectId, amount);
				return amount;
			}
		} else {
			//强制从数据库获取
			Document document = documentService.selectNoCache((int)objectId);
			if(document == null) {
				logger.error("找不到:{}/{}的对象", objectType, objectId);
				return 0;
			} else {
				int amount = (int)document.getLongExtraValue(DataName.availableCount.name());
				logger.debug("从数据库读取:{}/{}对象的剩余数量是:{}", objectType, objectId, amount);
				return amount;		
			}
		}

	}

	private int _writeOffsetToCache(String objectType, long objectId, int offset){

		String key = KeyConstants.STOCK_PREFIX + "#" + objectType + "#" + objectId;
		long newNumber = centerDataService.increaseBy(key, offset, 0, CommonStandard.CACHE_MAX_TTL);
		logger.debug("向中心缓存修改产品[" + key + "]数量:" + offset + ",修改后为:" + newNumber);

		return (int)newNumber;

	}

	private int _writeCountToCache(String objectType, long objectId, int count){

		String key = KeyConstants.STOCK_PREFIX + "#" + objectType + "#" + objectId;
		centerDataService.setForce(key, String.valueOf(count), CommonStandard.CACHE_MAX_TTL);
		logger.debug("向中心缓存写入产品[" + key + "]数量:" + count);

		return count;

	}




	public int _getAmountFromCache(String objectType, long objectId){

		String key = KeyConstants.STOCK_PREFIX + "#" + objectType + "#" + objectId;
		String newNumber = centerDataService.get(key);
		logger.debug("获取中心缓存的产品[" + key + "]数量是:" + newNumber);
		if(StringUtils.isBlank(newNumber)){
			return -999;
		}
		return Integer.parseInt(newNumber);

	}

	@Override
	public OpEisObject getTargetObject(String objectType, long objectId) {
		if(objectType.equalsIgnoreCase(ObjectType.node.name())) {
			Node node = nodeService.select((int)objectId);
			if(node == null) {
				logger.error("找不到{}/{}的对象",objectType, objectId);
				return null;
			}
			return node;
		} else if(objectType.equalsIgnoreCase(ObjectType.document.name())) {

			Document document = documentService.select((int)objectId);
			if(document == null) {
				logger.error("找不到{}/{}的对象",objectType, objectId);
				return null;
			}
			return document;
		} else if(objectType.equalsIgnoreCase(ObjectType.product.name())) {
			Product product = productService.select(objectId);
			if(product == null) {
				logger.error("找不到{}/{}的对象",objectType, objectId);
				return null;
			}
			return product;
		} else {
			//使用反射获取对象
			String serviceName = StringUtils.uncapitalize(objectType) + "Service";
			Object service = applicationContextService.getBean(serviceName);
			if(service == null) {
				logger.error("找不到服务:{}", serviceName);
				return null;
			}
			Method method = ClassUtils.getMethodIfAvailable(service.getClass(),"select", long.class);
			if(method == null) {
				logger.error("服务:{}没有select(long)方法", serviceName);
				return null;
			}
			try {
				Object result = method.invoke(service, objectId);
				if(result == null) {
					logger.error("执行{}的select方法返回的类型为空", serviceName);
				} else {
					logger.info("执行{}的select方法返回的类型为:{}", serviceName, result.getClass().getName());
				}
				if(result instanceof Operatable) {
					return (OpEisObject)result;
				} else {
					logger.error("执行{}的select方法返回的类型为:{}，不是需要的OpEisObject", serviceName, result.getClass().getName());
					return null;

				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			return null;
		}

	}



	@Override
	public OpEisObject writeItemData(Item item, String objectType, long objectId) {

		OpEisObject targetObject = null;
		if(objectType.equalsIgnoreCase(ObjectType.node.name())) {
			Node node = nodeDao.select((int)objectId);
			if(node == null) {
				logger.error("找不到{}/{}的对象",objectType, objectId);
				return null;
			}
			item.setName(node.getName());
			item.setExtraValue(DataName.refUrl.toString(), node.getViewUrl());
			item.setExtraValue(DataName.refImage.toString(), node.getPic());	
			item.setObjectTypeId(node.getNodeTypeId());
			targetObject = node;
		} else {
			Document document = documentService.select((int)objectId);
			if(document == null) {
				logger.error("找不到{}/{}的对象",objectType, objectId);
				return null;
			}
			item.setName(document.getTitle());
			item.setExtraValue(DataName.refImage.toString(), document.getExtraValue(DataName.thumbnail.name()));
			item.setObjectType(objectType);
			item.setObjectTypeId(document.getDocumentTypeId());
			if(document.getDocumentDataMap() != null) {
				for(DocumentData dd : document.getDocumentDataMap().values()) {
					logger.debug("检查目标文档的附加数据:" + JSON.toJSONString(dd));
					if(dd != null && dd.getDataCode() != null && dd.getDataCode().toLowerCase().startsWith("product")) {
						//必须使用最原生的Map进行赋值
						//因为要把很多属性带入
						if(item.getItemDataMap() == null) {
							item.setItemDataMap(new HashMap<String,ProductData>());
						}
						item.getItemDataMap().put(dd.getDataCode(), DataUtils.documentData2ProductData(dd));
					}
				}
			} else {
				logger.info("目标文档:{}没有任何附加数据", document.getUdid());
			}
			targetObject = document;
		}

		int ttl = (int)targetObject.getLongExtraValue(DataName.orderTtl.name());
		if(ttl > 0) {
			item.setTtl(ttl);
		}
		item.setExtraValue(DataName.refTitle.toString(), item.getName());

		return targetObject;



	}



	@Override
	public int checkPrivilege(Document document, User frontUser) {
		//检查用户是否有权限浏览本文章
		PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
		priceCriteria.setObjectType(ObjectType.document.name());
		priceCriteria.setObjectId(document.getUdid());
		priceCriteria.setPriceType(PriceType.PRICE_STANDARD.name());
		priceCriteria.setCurrentStatus(BasicStatus.normal.id);
		Price price = priceService.getPrice(priceCriteria);
		if(price == null || price.isZero()) {
			//没有价格或者价格全部为0，不需要购买也可以看
			if(document.getViewLevel() > 0) {
				if(frontUser == null) {
					logger.debug("文档:{}阅读级别是:{}，非登录用户无权浏览", document.getUdid(), document.getViewLevel());
					return EisError.ACCESS_DENY.id;
				} else if(frontUser.getLevel() <= document.getViewLevel()) {
					logger.debug("文档:{}阅读级别是:{}，用户:{}级别是:{},无权浏览", document.getUdid(), document.getViewLevel(), frontUser.getUuid(), frontUser.getLevel());
					return EisError.REQUIRE_HIGH_LEVEL.id;
				}
			}

		}
		if(frontUser == null) {
			logger.debug("文档:{}的价格是:{},非登录用户无权浏览", document.getUdid(), price);
			return EisError.ACCESS_DENY.id;			
		}
		if(document.getViewLevel() > 0) {
			if(frontUser.getLevel() <= document.getViewLevel()) {
				logger.debug("文档:{}阅读级别是:{}，用户:{}级别是:{},无权浏览", document.getUdid(), document.getViewLevel(), frontUser.getUuid(), frontUser.getLevel());
				return EisError.REQUIRE_HIGH_LEVEL.id;
			}
		}
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_SUBSCRIBE);
		userRelationCriteria.setObjectType(ObjectType.document.name());
		userRelationCriteria.setObjectId(document.getUdid());
		int count = userRelationService.count(userRelationCriteria);
		logger.debug("用户:{}对文档:{}的订阅数量是:{}", frontUser.getUuid(), document.getUdid(), count);

		if(count > 0) {
			return OperateResult.success.id;
		}
		//检查用户对整个栏目是否有订阅
		userRelationCriteria = new UserRelationCriteria();
		userRelationCriteria.setUuid(frontUser.getUuid());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_SUBSCRIBE);
		userRelationCriteria.setObjectType(ObjectType.node.name());
		userRelationCriteria.setObjectId(document.getDefaultNode().getNodeId());
		count = userRelationService.count(userRelationCriteria);
		logger.debug("用户:{}对栏目:{}的订阅数量是:{}", frontUser.getUuid(), document.getDefaultNode().getNodeId(), count);
		if(count > 0) {
			return OperateResult.success.id;
		}
		return EisError.subscribeCountError.id;
	}
}
