package com.maicard.security.dao;

import java.util.List;

import com.maicard.security.criteria.UserRegionRelationCriteria;
import com.maicard.security.domain.UserRegionRelation;

public interface UserRegionRelationDao {

	List<UserRegionRelation> list(
			UserRegionRelationCriteria userRegionRelationCriteria);

}
