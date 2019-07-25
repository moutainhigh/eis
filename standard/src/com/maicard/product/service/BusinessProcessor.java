package com.maicard.product.service;


import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Item;
import com.maicard.standard.IpPolicy;

public interface BusinessProcessor{
	/**
	 * 开始一笔交易
	 * @param item
	 * @return
	 */
	public EisMessage startTx(Item item);
	public EisMessage onQuery(Item item);
	public Item onResult(String queryString);
	public IpPolicy getIpPolicy(Item item);
}
