package com.maicard.money.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.money.criteria.GiftCardCriteria;
import com.maicard.money.domain.GiftCard;


public interface GiftCardDao {

	int insert(GiftCard giftCard) throws DataAccessException;

	int update(GiftCard giftCard) throws DataAccessException;
	
	int delete(String cardNumber) throws DataAccessException;

	List<GiftCard> list(GiftCardCriteria giftCardCriteria) throws DataAccessException;
		
	List<GiftCard> listOnPage(GiftCardCriteria giftCardCriteria) throws DataAccessException;
	
	int count(GiftCardCriteria giftCardCriteria) throws DataAccessException;

	GiftCard fetchWithLock(GiftCardCriteria giftCardCriteria);

	GiftCard select(int giftCardId);

}
