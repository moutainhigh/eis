package com.maicard.security.dao.ibatis;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.security.criteria.UserRegionRelationCriteria;
import com.maicard.security.dao.UserRegionRelationDao;
import com.maicard.security.domain.UserRegionRelation;

@Repository
public class UserRegionRelationDaoImpl extends BaseDao implements UserRegionRelationDao {

	@Override
	public List<UserRegionRelation> list(
			UserRegionRelationCriteria userRegionRelationCriteria) {
		return getSqlSessionTemplate().selectList("UserRegionRelation.list", userRegionRelationCriteria);
		
	}





}
