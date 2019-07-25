package com.maicard.wpt.service.me;

import java.util.Date;

import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.StringTools;
import com.maicard.ec.iface.DeliveryProcessor;
import com.maicard.product.domain.Item;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;


/**
 * 
 * 购买虚拟商品后的发货流程
 * 对于音乐平台而言，只需要建立一个用户与对应商品的关联关系
 * 
 * @author NetSnake
 * @date 2018-04-27
 *
 */
public class VirtualItemDeliveryProcessor extends BaseService implements DeliveryProcessor{
	
	private final String objectType = "SUBSCRIBE";
	
	@Resource
	private UserRelationService userRelationService;

	@Override
	public EisMessage delivery(Item item) {
		item.setCurrentStatus(TransactionStatus.waitingComment.id);
		
		long subscribeUuid = item.getChargeFromAccount();
		
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(item.getOwnerId());
		userRelationCriteria.setObjectType(objectType);
		userRelationCriteria.setObjectId(item.getProductId());
		userRelationCriteria.setUuid(subscribeUuid);
		int count = userRelationService.count(userRelationCriteria);
		if(count > 0){
			logger.info("用户#{}已经订阅了产品:{}",subscribeUuid, item.getProductId());
			return new EisMessage(OperateResult.success.id);
		}
		UserRelation userRelation = new UserRelation(item.getOwnerId());
		userRelation.setUuid(subscribeUuid);
		userRelation.setObjectType(objectType);
		userRelation.setObjectId(item.getProductId());
		userRelation.setCreateTime(new Date());
		userRelation.setExtraValue("initSubscribeTime", StringTools.getFormattedTime(userRelation.getCreateTime()));
		int rs = userRelationService.insert(userRelation);
		logger.info("为用户#{}新增订阅产品:{}，结果:{}",subscribeUuid, item.getProductId(), rs);
		
		return new EisMessage(OperateResult.success.id);
	}

}
