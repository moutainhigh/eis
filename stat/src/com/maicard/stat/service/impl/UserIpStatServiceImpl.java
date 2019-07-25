package com.maicard.stat.service.impl;


import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.service.PartnerService;
import com.maicard.stat.criteria.UserIpStatCriteria;
import com.maicard.stat.dao.UserIpStatDao;
import com.maicard.stat.domain.UserIpStat;
import com.maicard.stat.service.UserIpStatService;

@Service
public class UserIpStatServiceImpl extends BaseService implements UserIpStatService {
	@Resource
	private UserIpStatDao userIpStatDao;

	@Resource
	private PartnerService partnerService;

	public List<UserIpStat> listOnPage(UserIpStatCriteria statCriteria) throws Exception {
		if(statCriteria == null){
			return null;
		}
		List<UserIpStat> itemStatList = userIpStatDao.listOnPage(statCriteria);	
		if(itemStatList != null && itemStatList.size() >0 ){
			for(int i = 0; i < itemStatList.size(); i++){
				itemStatList.get(i).setIndex(i+1);
			}
		}
		return itemStatList;

	}

	
	@Override
	public int count(UserIpStatCriteria statCriteria) throws Exception {
		
		return userIpStatDao.count(statCriteria);
	}


	

}
