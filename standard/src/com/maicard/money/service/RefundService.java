package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.RefundCriteria;
import com.maicard.money.domain.Refund;

public interface RefundService {

	int insert(Refund refund);

	int update(Refund refund);

	int delete(int refundId);
	
	Refund select(int refundId);

	List<Refund> list(RefundCriteria refundCriteria);

	List<Refund> listOnPage(RefundCriteria refundCriteria);

	int count(RefundCriteria refundCriteria);

}
