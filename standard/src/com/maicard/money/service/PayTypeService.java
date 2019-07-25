package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;

public interface PayTypeService {

	int insert(PayType payType);

	int update(PayType payType);

	int delete(int payTypeId);
	
	PayType select(int payTypeId);

	List<PayType> list(PayTypeCriteria payTypeCriteria);

	List<PayType> listOnPage(PayTypeCriteria payTypeCriteria);

	int count(PayTypeCriteria payTypeCriteria);

}
