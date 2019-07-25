package com.maicard.money.service;

import java.util.HashMap;
import java.util.List;

import com.maicard.money.criteria.GiftCardCriteria;
import com.maicard.money.domain.GiftCard;

public interface GiftCardService {


	int update(GiftCard giftCard);
	
	GiftCard get(GiftCardCriteria giftCardCriteria);

	List<GiftCard> list(GiftCardCriteria giftCardCriteria);

	List<GiftCard> listOnPage(GiftCardCriteria giftCardCriteria);
		
	GiftCard fetchWithLock(GiftCardCriteria giftCardCriteria);

	HashMap<String, String> generate(GiftCardCriteria giftCardCriteria);

	int insert(GiftCard giftCard);
	
	GiftCard select(int giftCardId);

	GiftCard check(GiftCardCriteria giftCardCriteria);

    float charge(Long uuid,String cardNumber);
}
