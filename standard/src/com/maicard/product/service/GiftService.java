package com.maicard.product.service;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.domain.GiftCard;
import com.maicard.product.domain.Item;

/**
 * 通过现金giftMoney或礼品卡，向用户赠送物品
 * 
 *
 * @author NetSnake
 * @date 2013-6-27 
 */
public interface GiftService {
	
	EisMessage gift(Item item);
	
	EisMessage gift(Item item, GiftCard giftCard);

}
