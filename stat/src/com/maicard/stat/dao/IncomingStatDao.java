package com.maicard.stat.dao;

import java.util.List;

import com.maicard.stat.criteria.PayStatCriteria;
import com.maicard.stat.domain.IncomingStat;

public interface IncomingStatDao {
	
	List<IncomingStat> incomingStat(PayStatCriteria payStatCriteria);
}
