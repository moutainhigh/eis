package com.maicard.security.service;

import java.util.List;

import com.maicard.common.domain.Region;
import com.maicard.security.criteria.UserRegionRelationCriteria;
import com.maicard.security.domain.UserRegionRelation;

public interface UserRegionRelationService {

	Region getRegionByUuid(long uuid);

	List<UserRegionRelation> list(
			UserRegionRelationCriteria userRegionRelationCriteria);
}
