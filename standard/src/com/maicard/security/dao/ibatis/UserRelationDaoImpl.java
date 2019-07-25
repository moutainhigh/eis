package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.UserRelationDao;
import com.maicard.security.domain.UserRelation;

@Repository
public class UserRelationDaoImpl extends BaseDao implements UserRelationDao {

	public int insert(UserRelation userRelation) throws DataAccessException {
		if (userRelation.getObjectType()!=null && userRelation.getObjectType().equals("vip")) {
			userRelation.setUserRelationId(0);
		}
		return (Integer)getSqlSessionTemplate().insert("com.maicard.security.sql.UserRelation.insert", userRelation);
	}

	public int update(UserRelation userRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("com.maicard.security.sql.UserRelation.update", userRelation);


	}

	public int delete(long userRelationId) throws DataAccessException {


		return getSqlSessionTemplate().delete("com.maicard.security.sql.UserRelation.delete", userRelationId);


	}

	public UserRelation select(long userRelationId) throws DataAccessException {
		return (UserRelation) getSqlSessionTemplate().selectOne("com.maicard.security.sql.UserRelation.select", userRelationId);
	}


	public List<UserRelation> list(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "userRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserRelation.list", userRelationCriteria);
	}


	public List<UserRelation> listOnPage(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "userRelationCriteria must not be null");
		Assert.notNull(userRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userRelationCriteria);
		Paging paging = userRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserRelation.list", userRelationCriteria, rowBounds);
	}

	public int count(UserRelationCriteria userRelationCriteria) throws DataAccessException {
		Assert.notNull(userRelationCriteria, "userRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.security.sql.UserRelation.count", userRelationCriteria)).intValue();
	}

	@Override
	public int delete(UserRelationCriteria userRelationCriteria) {
		if(userRelationCriteria == null){
			return -1;
		}
		if(StringUtils.isBlank(userRelationCriteria.getObjectType())){
			logger.error("条件删除关联未提供对象类型");
			return -2;
		}
		if(userRelationCriteria.getUuid() <= 0 && userRelationCriteria.getObjectId() <= 0){
			logger.error("条件删除关联既没有提供用户ID也没有提供对象ID");
			return -3;
		}	
		return getSqlSessionTemplate().delete("com.maicard.security.sql.UserRelation.deleteByCriteria", userRelationCriteria);

		
	}

	@Override
	public long getMaxId() {
		return getSqlSessionTemplate().selectOne("com.maicard.security.sql.UserRelation.getMaxId");
	}

}
