package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.SiteDomainRelationCriteria;
import com.maicard.common.domain.SiteDomainRelation;

public interface SiteDomainRelationService {

	int insert(SiteDomainRelation siteDomainRelation);

	int update(SiteDomainRelation siteDomainRelation);

	int delete(int siteDomainRelationId);
	
	SiteDomainRelation select(int siteDomainRelationId);

	List<SiteDomainRelation> list(SiteDomainRelationCriteria siteDomainRelationCriteria);

	List<SiteDomainRelation> listOnPage(SiteDomainRelationCriteria siteDomainRelationCriteria);

	
	int count(SiteDomainRelationCriteria siteDomainRelationCriteria);

	boolean isValidOwnerAccess(String serverName, long ownerId);

	SiteDomainRelation getByHostName(String hostName);


}
