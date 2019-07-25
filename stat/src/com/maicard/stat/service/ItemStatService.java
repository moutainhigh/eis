package com.maicard.stat.service;

import java.util.List;

import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.stat.criteria.ItemStatCriteria;
import com.maicard.stat.domain.ItemStat;

public interface ItemStatService {
	
	List<ItemStat> listOnPage(ItemStatCriteria statCriteria) throws Exception;

	int count(ItemStatCriteria statCriteria) throws Exception;
	
	int gather(ItemStatCriteria statCriteria) throws Exception;
	
	void moveToHistory(ItemCriteria itemCriteria) throws Exception;

    List<ItemStat> listByInviter(ItemStatCriteria statCriteria) throws Exception;

	int countByInviter(ItemStatCriteria statCriteria) throws Exception;
   
	int change_Inviter(ItemStatCriteria statCriteria) throws Exception ;
	
	float getJWCharge();
	
	List<Item> getJWShengdaTakeCard(ItemStatCriteria statCriteria);



	
}

