package com.maicard.billing.service;

import java.util.List;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.common.domain.EisMessage;
import com.maicard.product.domain.Item;

public interface ShareConfigService {

	int insert(ShareConfig shareConfig);

	int update(ShareConfig shareConfig);
	
	int updateNoNull(ShareConfig shareConfig);

	int delete(int shareConfigId);
	
	ShareConfig select(int shareConfigId);

	List<ShareConfig> list(ShareConfigCriteria shareConfigCriteria);

	List<ShareConfig> listOnPage(ShareConfigCriteria shareConfigCriteria);

	int count(ShareConfigCriteria shareConfigCriteria);

	ShareConfig calculateShare(
			ShareConfigCriteria shareConfigCriteria);

	
	ShareConfig calculateShare(Object obj, ShareConfigCriteria shareConfigCriteria);

	float calculateFrozenMoney(Item item);

	EisMessage clone(ShareConfigCriteria shareConfigCriteria);

}
