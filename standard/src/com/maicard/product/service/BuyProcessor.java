package com.maicard.product.service;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Item;
import com.maicard.standard.IpPolicy;


public interface BuyProcessor {
	public EisMessage onBuy(Item item);
	public EisMessage onQuery(Item item);
	public Item onResult(String queryString);
	public IpPolicy getIpPolicy(Item item);

}
