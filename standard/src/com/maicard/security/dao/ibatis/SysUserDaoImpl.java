package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.dao.SysUserDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;

@Repository
public class SysUserDaoImpl extends BaseDao implements SysUserDao {

	public int insert(User sysUser) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysUser.insert", sysUser)).intValue();
	}

	public int update(User sysUser) throws DataAccessException {


		return getSqlSessionTemplate().update("SysUser.update", sysUser);


	}

	public int delete(long uuid) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysUser.delete", uuid);


	}

	public User select(long uuid) throws DataAccessException {
		return (User) getSqlSessionTemplate().selectOne("SysUser.select", uuid);
	}


	public List<User> list(UserCriteria userCriteria) throws DataAccessException {
		Assert.notNull(userCriteria, "sysUserCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysUser.list", userCriteria);
	}


	public List<User> listOnPage(UserCriteria userCriteria) throws DataAccessException {
		Assert.notNull(userCriteria, "sysUserCriteria must not be null");
		Assert.notNull(userCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userCriteria);
		Paging paging = userCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysUser.list", userCriteria, rowBounds);
	}

	public int count(UserCriteria userCriteria) throws DataAccessException {
		Assert.notNull(userCriteria, "sysUserCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysUser.count", userCriteria)).intValue();
	}
	
	@Override
	public int updateDynamicData(UserDynamicData userDynamicData){
		return getSqlSessionTemplate().update("SysUser.updateDynamicData", userDynamicData);
	}


}
