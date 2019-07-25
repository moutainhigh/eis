package com.maicard.common.service.impl;


import java.util.List;


import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.SiteDomainRelationCriteria;
import com.maicard.common.domain.SiteDomainRelation;
import com.maicard.common.service.SiteDomainRelationService;

@Service
public class SiteDomainRelationServiceImpl extends BaseService implements SiteDomainRelationService {

	@Override
	public int insert(SiteDomainRelation siteDomainRelation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(SiteDomainRelation siteDomainRelation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int siteDomainRelationId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SiteDomainRelation select(int siteDomainRelationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SiteDomainRelation> list(SiteDomainRelationCriteria siteDomainRelationCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SiteDomainRelation> listOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(SiteDomainRelationCriteria siteDomainRelationCriteria) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isValidOwnerAccess(String serverName, long ownerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SiteDomainRelation getByHostName(String hostName) {
		// TODO Auto-generated method stub
		return null;
	}

}
