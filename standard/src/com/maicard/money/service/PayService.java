package com.maicard.money.service;

import java.util.List;
import java.util.Map;

import com.maicard.common.domain.EisMessage;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.domain.Pay;
import com.maicard.money.iface.PayProcessor;

public interface PayService {

	int insert(Pay pay);

	int update(Pay pay) throws Exception;

	int delete(String  transactionId);
	
	Pay select(String transactionId);
	
	List<Pay> list(PayCriteria payCriteria);

	List<Pay> listOnPage(PayCriteria payCriteria);
	
	EisMessage end(int payMethodId, String resultString, Object params) throws Exception;
	
	int count(PayCriteria payCriteria);

	List<Pay> listOnPageByPartner(PayCriteria payCriteria);
	
	List<Pay> listOnPageByDay(PayCriteria payCriteria);
	
	int countByPartner(PayCriteria payCriteria);

	EisMessage begin(Pay pay);
		
	EisMessage end(Pay pay) throws Exception;

	//EisMessage createOrder(Pay pay);

	int endFront(Pay pay) throws Exception;

	int refund(Pay pay);

	Map<String, String> generateClientResponseMap(Pay pay);

	PayProcessor getProcessor(Pay pay);


	//PayMethod getPayMethod(Pay pay, User partner);


	

}
