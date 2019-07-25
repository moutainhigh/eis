package com.maicard.money.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maicard.money.domain.Price;

public class PriceUtils {

	protected static final Logger logger = LoggerFactory.getLogger(PriceUtils.class);

	

	/**
	 * 把两个价格相加并返回一个新的资金对象
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-30
	 */
	public static Price plus(Price price1, Price price2) {
		if(price1 == null) {
			return price2.clone();
		}
		if(price2 == null) {
			return price1.clone();
		}
		Price price = new Price(price1.getOwnerId());
		price.setPriceType(price1.getPriceType());
		price.setMoney(price1.getMoney() + price2.getMoney());
		price.setCoin(price1.getCoin() + price2.getCoin());
		price.setPoint(price1.getPoint() + price2.getPoint());
		price.setScore(price1.getScore() + price2.getScore());	
		
		return price;
	}
	
	/**
	 * 把两个价格相减并返回一个新的资金对象
	 * 
	 * 
	 * @author GHOST
	 * @date 2018-11-30
	 */
	public static Price minus(Price price1, Price price2) {
		if(price1 == null) {
			return price2.clone();
		}
		if(price2 == null) {
			return price1.clone();
		}
		Price price = new Price(price1.getOwnerId());
		price.setPriceType(price1.getPriceType());
		price.setMoney(price1.getMoney() + price2.getMoney());
		price.setCoin(price1.getCoin() + price2.getCoin());
		price.setPoint(price1.getPoint() + price2.getPoint());
		price.setScore(price1.getScore() + price2.getScore());	
		
		return price;
	}
	
	


}
