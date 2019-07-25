package com.maicard.wpt.service;

import java.util.List;

import com.maicard.wpt.criteria.WeixinGroupCriteria;
import com.maicard.wpt.domain.WeixinGroup;

public interface WeixinGroupService {
	int insert(WeixinGroup weixinGroup);

	int update(WeixinGroup weixinGroup);

	int delete(long weixinGroupId);
	
	WeixinGroup select(long weixinGroupId);
	
	List<WeixinGroup> list(WeixinGroupCriteria weixinGroupCriteria);

	List<WeixinGroup> listOnPage(WeixinGroupCriteria weixinGroupCriteria);

	int count(WeixinGroupCriteria weixinGroupCriteria);

	WeixinGroup findGroupByIdentify(String identify);

}
