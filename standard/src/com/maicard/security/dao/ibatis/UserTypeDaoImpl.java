package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserTypeCriteria;
import com.maicard.security.dao.UserTypeDao;
import com.maicard.security.domain.UserType;

@Repository
public class UserTypeDaoImpl extends BaseDao implements UserTypeDao {

	public int insert(UserType userType) throws DataAccessException {
		return (Integer)getSqlSessionTemplate().insert("UserType.insert", userType);
	}

	public int update(UserType userType) throws DataAccessException {


		return getSqlSessionTemplate().update("UserType.update", userType);


	}

	public int delete(int id) throws DataAccessException {


		return getSqlSessionTemplate().delete("UserType.delete", new Integer(id));


	}

	public UserType select(int id) throws DataAccessException {
		return (UserType) getSqlSessionTemplate().selectOne("UserType.select", new Integer(id));
	}


	public List<UserType> list(UserTypeCriteria userTypeCriteria) throws DataAccessException {
		Assert.notNull(userTypeCriteria, "userTypeCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("UserType.list", userTypeCriteria);
	}


	public List<UserType> listOnPage(UserTypeCriteria userTypeCriteria) throws DataAccessException {
		Assert.notNull(userTypeCriteria, "userTypeCriteria must not be null");
		Assert.notNull(userTypeCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userTypeCriteria);
		Paging paging = userTypeCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("UserType.list", userTypeCriteria, rowBounds);
	}

	public int count(UserTypeCriteria userTypeCriteria) throws DataAccessException {
		Assert.notNull(userTypeCriteria, "userTypeCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("UserType.count", userTypeCriteria)).intValue();
	}

	@Override
	public void deleteByCriteria(UserTypeCriteria userTypeCriteria) {
		// TODO Auto-generated method stub
		
	}

}
