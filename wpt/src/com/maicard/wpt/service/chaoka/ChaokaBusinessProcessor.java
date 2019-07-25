package com.maicard.wpt.service.chaoka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.GlobalOrderIdService;
import com.maicard.common.util.RandStrUtils;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.product.service.BusinessProcessor;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.StockService;
import com.maicard.standard.DataName;
import com.maicard.standard.IpPolicy;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.TransactionStandard.TransactionType;

@Service
public class ChaokaBusinessProcessor extends BaseService implements BusinessProcessor {

	@Resource
	private StockService stockService;
	
	@Resource
	private ItemService itemService;
	
	@Resource
	private GlobalOrderIdService globalOrderIdService;
	
	
	@Override
	public EisMessage startTx(Item item) {
		logger.info("开始处理点卡购买后业务:{}", item.getTransactionId());
		if(item.getTransactionTypeId() == TransactionType.buy.id) {
			return _buy(item);
		} else if(item.getTransactionTypeId() == TransactionType.sale.id) {
			return _sale(item);
		}
		return null;
	}

	/**
	 * 玩家请求寄售一张卡
	 * @param item
	 * @return
	 */
	private EisMessage _sale(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 玩家请求购买一张卡
	 * @param item
	 * @return
	 */
	private EisMessage _buy(Item item) {
		
		//OpEisObject targetObject = stockService.getTargetObject( item.getObjectType(), item.getProductId());
		//向本地库存查询有没有对应的卡密，比如有玩家正在出售的
		ItemCriteria itemCriteria = new ItemCriteria(item.getOwnerId());
		itemCriteria.setTransactionTypeId(TransactionType.sale.id);
		itemCriteria.setCurrentStatus(TransactionStatus.newOrder.id);
		itemCriteria.setObjectType(item.getObjectType());
		itemCriteria.setProductIds(item.getProductId());
		int count = itemService.count(itemCriteria);
		int buyCount = item.getCount() ;
		
		List<Item> lockedItemList = new ArrayList<Item>();
		logger.info("购买订单:{}请求购买数量是:{},查找其对应产品:{}#{}的销售订单数量是:{}", item.getTransactionId(), item.getCount(), item.getObjectType(), item.getProductId(), count);
		if(count < item.getCount()) {
			//数量不足，向官方请求购买
			if(count > 0) {
				//先锁定系统内有的卡密
				lockedItemList = itemService.lock(itemCriteria);
				buyCount = item.getCount() - lockedItemList.size();
			} 
			logger.info("购买订单:{}请求购买数量是{}，系统可支持的数量是{}，还需要购买:{}", item.getTransactionId(), item.getCount(),lockedItemList.size(), buyCount);

			if(buyCount > 0) {
				//向官方购买
				List<Item> buyItemList = _buyFromOut(itemCriteria, buyCount);
				if(buyItemList.size() > 0) {
					lockedItemList.addAll(buyItemList);
				}
				}
			
		}
		EisMessage msg = new EisMessage(OperateResult.success.id,"购买成功");
		msg.setAttachmentData("itemList", lockedItemList);
		return msg;
	}

	private List<Item> _buyFromOut(ItemCriteria itemCriteria, int buyCount) {
		
		List<Item> itemList = new ArrayList<Item>();
		//生成一批假卡密
		for(int i = 0; i < buyCount; i++) {
			Item item = new Item();
			item.setObjectType(itemCriteria.getObjectType());
			item.setProductId(itemCriteria.getProductIds()[0]);
			item.setChargeFromAccount(itemCriteria.getChargeFromAccount());
			item.setTransactionTypeId(TransactionType.stock.id);
			item.setTransactionId(globalOrderIdService.generate(item.getTransactionTypeId()));
			String[] card = RandStrUtils.generate(8, 8, true);
			item.setExtraValue(DataName.productSerialNumber.name(), card[0]);
			item.setExtraValue(DataName.productPassword.name(), card[1]);
			item.setContent(card[0] + "," + card[1]);
			logger.debug("生成一张卡密:{}={}", item.getTransactionId(), Arrays.toString(card));
			itemList.add(item);
		}
		
		return itemList;
	}

	@Override
	public EisMessage onQuery(Item item) {
		return null;
	}

	@Override
	public Item onResult(String queryString) {
		return null;
	}

	@Override
	public IpPolicy getIpPolicy(Item item) {
		return null;
	}

}
