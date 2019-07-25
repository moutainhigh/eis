package com.maicard.product.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.exception.DataInvalidException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.money.domain.GiftCard;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.GiftService;
import com.maicard.product.service.ItemService;
import com.maicard.security.service.FrontUserService;
import com.maicard.standard.DataName;
import com.maicard.standard.MoneyType;
import com.maicard.standard.TransactionStandard.TransactionType;

@Service
public class GiftServiceImpl extends BaseService implements GiftService {

	@Resource
	private ConfigService configService;
	@Resource
	private FrontUserService frontUserService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private ItemService itemService;
	
	private float maxGiftMoneyAmount;
	
	@PostConstruct
	public void init(){
		maxGiftMoneyAmount = configService.getFloatValue(DataName.maxGiftMoneyAmount.toString(),0);
		if(maxGiftMoneyAmount == 0){
			maxGiftMoneyAmount = 1000f;
		}
	}
	
	@Override
	public EisMessage gift(Item item) {
		if(item == null){
			logger.error("尝试赠送操作的物品是空");
			throw new RequiredObjectIsNullException("尝试赠送操作的物品是空");
		}
		if(item.getRequestMoney() > maxGiftMoneyAmount || item.getSuccessMoney() > maxGiftMoneyAmount){
			throw new DataInvalidException("充值赠送金额[" + item.getRequestMoney() + "]超出了系统最大限制[" + maxGiftMoneyAmount + "]");
		}
		if(item.getChargeFromAccount() == 0){
			throw new DataInvalidException("充值赠送来源账户为空");
		}
		item.setTransactionTypeId(TransactionType.buy.getId());
		item.addConfig(DataName.moneyType.toString(), MoneyType.giftMoney.getCode());
		Money money = new Money();
		money.setUuid(item.getChargeFromAccount());
		money.setGiftMoney(item.getRequestMoney());
		moneyService.plus(money);
		itemService.insert(item);
		return null;
	}

	@Override
	public EisMessage gift(Item item, GiftCard giftCard) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
