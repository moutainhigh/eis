package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserLevelProjectCriteria;
import com.maicard.security.dao.UserLevelProjectDao;
import com.maicard.security.domain.UserLevelProject;

@Repository
public class UserLevelProjectDaoImpl extends BaseDao implements UserLevelProjectDao {

	public int insert(UserLevelProject userLevelProject) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("UserLevelProject.insert", userLevelProject);
	}

	public int update(UserLevelProject userLevelProject) throws DataAccessException {


		return getSqlSessionTemplate().update("UserLevelProject.update", userLevelProject);


	}

	public int delete(int userLevelProjectId) throws DataAccessException {


		return getSqlSessionTemplate().delete("UserLevelProject.delete", new Integer(userLevelProjectId));


	}

	public UserLevelProject select(int userLevelProjectId) throws DataAccessException {
		return (UserLevelProject) getSqlSessionTemplate().selectOne("UserLevelProject.select", new Integer(userLevelProjectId));
	}


	public List<UserLevelProject> list(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException {
		Assert.notNull(userLevelProjectCriteria, "userLevelProjectCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserLevelProject.list", userLevelProjectCriteria);
	}


	public List<UserLevelProject> listOnPage(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException {
		Assert.notNull(userLevelProjectCriteria, "userLevelProjectCriteria must not be null");
		Assert.notNull(userLevelProjectCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userLevelProjectCriteria);
		Paging paging = userLevelProjectCriteria.getPaging();
		paging.setTotalResults(totalResults);RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("UserLevelProject.list", userLevelProjectCriteria, rowBounds);
	}

	public int count(UserLevelProjectCriteria userLevelProjectCriteria) throws DataAccessException {
		Assert.notNull(userLevelProjectCriteria, "userLevelProjectCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("UserLevelProject.count", userLevelProjectCriteria)).intValue();
	}


}
