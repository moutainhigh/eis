package com.maicard.money.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.maicard.common.domain.EOEisObject;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.product.domain.Item;

public interface PriceService {

	int insert(Price price);

	int update(Price price);

	int delete(long priceId);
	
	Price select(long priceId);

	List<Price> list(PriceCriteria priceCriteria);

	List<Price> listOnPage(PriceCriteria priceCriteria);

	int count(PriceCriteria priceCriteria);

	int applyPrice(Item item, Price price);
	
	int applyPrice(Item item, String priceType);

	Price getPrice(PriceCriteria priceCriteria);

	boolean generatePriceExtraData(EOEisObject document, String priceType);
	
	boolean generatePriceExtraData(EOEisObject document, Price price);


	//根据价格类型，创建一个购买token，供后续流程参考
	String generateTransactionToken(Price price, long uuid);

	Price getPriceByToken(EOEisObject object, long uuid, String transactionToken);

	List<Price> bindPrice(HttpServletRequest request, EOEisObject targetObject);

	Price getPrice(EOEisObject object, String priceType);







}
