package com.maicard.wpt.service.me;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.StringTools;
import com.maicard.ec.iface.DeliveryProcessor;
import com.maicard.product.domain.Item;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;


/**
 * 
 * 订购VIP后的发货流程
 * 
 * @author NetSnake
 * @date 2018-04-27
 *
 */
public class VipPurchaseDeliveryProcessor extends BaseService implements DeliveryProcessor{
	
	private final String objectType = "VIP";
	
	@Resource
	private ProductService productService;

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
		List<UserRelation> userRelationList = userRelationService.list(userRelationCriteria);
		if(userRelationList.size() > 0){
			UserRelation userRelation = userRelationList.get(0);
			logger.info("用户#{}已经订阅了VIP产品:{}，延长其有效期",subscribeUuid, item.getProductId());
			userRelation.setCreateTime(new Date());

			userRelationService.update(userRelation);
			return new EisMessage(OperateResult.success.id);
		}
		Product product = productService.select(item.getProductId());
		if(product==null){
			logger.error("交易订单对应的产品为空，无法创建VIP订购关联");
			return new EisMessage(OperateResult.failed.id);
		}
		String vipType = product.getProductCode();
		UserRelation userRelation = new UserRelation(item.getOwnerId());
		userRelation.setUuid(subscribeUuid);
		userRelation.setObjectType(objectType);
		userRelation.setObjectId(item.getProductId());
		userRelation.setCreateTime(new Date());
		userRelation.setExtraValue("initSubscribeTime", StringTools.getFormattedTime(userRelation.getCreateTime()));
		userRelation.setRelationType(vipType);
		userRelation.setCurrentStatus(2);
		int rs = userRelationService.insert(userRelation);
		logger.info("为用户#{}新增订购VIP:{}，VIP类型:{},结果:{}",subscribeUuid, item.getProductId(), vipType,rs);

		return new EisMessage(OperateResult.success.id);
	}

}
