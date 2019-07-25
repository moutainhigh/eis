package com.maicard.money.service;

import java.util.List;

import com.maicard.money.criteria.AwardCriteria;
import com.maicard.money.domain.Award;
import com.maicard.money.domain.Money;

public interface AwardService {


	int update(Award award);
	
	List<Award> list(AwardCriteria awardCriteria);

	List<Award> listOnPage(AwardCriteria awardCriteria);

	int insert(Award award);
	
	Award select(long awardId);

	int delete(long awardId);
	
	int count(AwardCriteria awardCriteria);

	Award convert(Money awardMoney);


}
