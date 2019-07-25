package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.IpLocationCriteria;
import com.maicard.common.domain.IpLocation;

public interface IpLocationService {

	int insert(IpLocation ipLocation);
	
	int update(IpLocation ipLocation);

	int delete(int ipLocationId);
	
	IpLocation select(int ipLocationId);

	List<IpLocation> list(IpLocationCriteria ipLocationCriteria);

	List<IpLocation> listOnPage(IpLocationCriteria ipLocationCriteria);
	
	int count(IpLocationCriteria ipLocationCriteria);

	IpLocation query(String ip);

	
}
