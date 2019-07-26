package com.maicard.money.service;

import java.util.List;
import java.util.Map;

import com.maicard.money.criteria.PayMethodCriteria;
import com.maicard.money.domain.PayMethod;

public interface PayMethodService {

	int insert(PayMethod payMethod);

	int update(PayMethod payMethod);

	int delete(int payMethodId);
	
	PayMethod select(int payMethodId);

	List<PayMethod> list(PayMethodCriteria payMethodCriteria) throws Exception;

	List<PayMethod> listOnPage(PayMethodCriteria payMethodCriteria);
	
	int count(PayMethodCriteria payMethodCriteria);

	Map<Integer, PayMethod> list4IdKeyMap(PayMethodCriteria payMethodCriteria) throws Exception;


}
