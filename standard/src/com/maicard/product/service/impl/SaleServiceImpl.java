package com.maicard.product.service.impl;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.MessageService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.BuyProcessor;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.SaleService;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.UserDataService;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.TransactionStandard;
import com.maicard.standard.SecurityStandard.UserExtraType;
import com.maicard.standard.SecurityStandard.UserTypes;
import com.maicard.standard.TransactionStandard.TransactionStatus;
@Service
public class SaleServiceImpl extends BaseService implements SaleService {

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private ItemService itemService;
	@Resource
	private MessageService messageService;
	@Resource
	private UserDataService userDataService;
	
	private String messageBusName;
	
	@PostConstruct
	public void init(){
		messageBusName = configService.getValue(DataName.messageBusSystem.toString(),0);
	}

	@Override
	public EisMessage sale(Item item) {
		if(item == null){
			logger.warn("尝试出售的商品为空");
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"请先选择要出售的商品");
		}
		if(item.getCurrentStatus() != TransactionStatus.newOrder.getId()){
			logger.debug("尝试购买的商品不是处于未付款状态");
			return new EisMessage(EisError.dataError.getId(),"尝试购买的商品不是处于未付款状态");
		}
		item.setSuccessMoney(0f);
		item.setTransactionTypeId(TransactionStandard.TransactionType.sale.getId());
		return itemService.insert(item);

	}

	@Override
	public EisMessage end(long uuid, String resultString) {
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUserTypeId(UserTypes.partner.getId());
		userDataCriteria.setUserExtraTypeId(UserExtraType.payChannel.getId());
		userDataCriteria.setUuid(uuid);
		userDataCriteria.setDataCode(DataName.supplierProcessClass.toString());
		UserData processClassConfig = null;
		try {
			processClassConfig = userDataService.select(userDataCriteria);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(processClassConfig == null){
			logger.error("找不到合作方[" + uuid + "]的处理器");
			return null;
		}
		BuyProcessor businessProcessor = null;
		try{
			businessProcessor = (BuyProcessor)applicationContextService.getBean(processClassConfig.getDataValue());
		}catch(Exception e){}
		if(businessProcessor == null){
			logger.error("找不到指定的业务处理器[" + processClassConfig.getDataValue() + "]");
			return new EisMessage(TransactionStatus.failed.getId(), "");
		}
		Item item =  businessProcessor.onResult(resultString);
		if(item == null){
			logger.error("业务处理器[" + processClassConfig.getDataValue() + "]返回的Item为空");
			return new EisMessage(TransactionStatus.failed.getId(), "");
		}
		item.setSupplyPartnerId(uuid);
		//查找该item的当前状态
		
		/*Item _oldItem = null;
		try {
			_oldItem = itemService.select(item.getTransactionId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(_oldItem == null || _oldItem.getCurrentStatus() != TransactionStatus.waitingNotify.getId()){
			//不是处于等待异步结果，不进行处理
			return new EisMessage((_oldItem == null ? TransactionStatus.failed.getId() : _oldItem.getCurrentStatus()), item.getContent());			
		}
		item.setLockGlobalUniqueId(_oldItem.getLockGlobalUniqueId());
		/*if(item.getCurrentStatus() == TransactionStatus.failed.getId()){
			item.setCurrentStatus(Error.cardPasswordError.getId());
		}*/
		//将item发送到消息总线
		if(item.getTransactionId() != null){
			EisMessage m = new EisMessage();
			m.setOperateCode(Operate.close.getId());
			m.setAttachment(new HashMap<String,Object>());
			m.getAttachment().put("item", item);	
			m.setObjectType(ObjectType.item.toString());
			messageService.send(messageBusName, m);
			logger.info("把结束交易请求发送至总线[" + item.getTransactionId() + ",supplyPartnerId=" + item.getSupplyPartnerId() + ",chargeFromAccount=" + item.getChargeFromAccount() + ",productId=" + item.getProductId() + ",labelMoney=" + item.getLabelMoney() + ",successMoney=" + item.getSuccessMoney() + ",frozenMoney=" + item.getFrozenMoney() + ",requestMoney=" + item.getRequestMoney() + "],状态" + item.getCurrentStatus() + "/" + item.getExtraStatus() + "]");
			m = null;
		}
		
		return new EisMessage(item.getCurrentStatus(), item.getContent());			
	}





}
