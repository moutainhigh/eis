package com.maicard.wpt.service.cartoon;

import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ProductValidator;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;

public class SubscribeStatusValidator extends BaseService implements ProductValidator{
	
	@Resource
	private UserRelationService userRelationService;

	@Override
	public int getLabelMoneyFromCard(Item item) {
		return 0;
	}

	@Override
	public int validate(String action, Item item, Object params) {
		UserRelationCriteria userRelationCriteria = new UserRelationCriteria(item.getOwnerId());
		userRelationCriteria.setUuid(item.getChargeFromAccount());
		userRelationCriteria.setRelationType(UserRelationCriteria.RELATION_TYPE_SUBSCRIBE);
		userRelationCriteria.setObjectType(item.getObjectType());
		userRelationCriteria.setObjectId(item.getProductId());
		
		int count = userRelationService.count(userRelationCriteria);
		logger.info("针对交易:{}，用户:{}的类型为:{}/{}的订阅关系数量是:{}", item.getTransactionId(), userRelationCriteria.getUuid(), userRelationCriteria.getObjectType(), userRelationCriteria.getObjectId(), count );
		
		if(count <= 0) {
			return OperateResult.success.getId();
		} else {
			return EisError.COUNT_LIMIT_EXCEED.id;
		}
		
	}

}
