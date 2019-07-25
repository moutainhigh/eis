package com.maicard.security.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.SysUserRoleRelationDao;
import com.maicard.security.domain.UserRoleRelation;

@Repository
public class SysUserRoleRelationDaoImpl extends BaseDao implements SysUserRoleRelationDao {

	public int insert(UserRoleRelation sysGroupUserRelation) throws DataAccessException {
		return ((Integer)getSqlSessionTemplate().insert("SysUserRoleRelation.insert", sysGroupUserRelation)).intValue();
	}

	public int update(UserRoleRelation sysGroupUserRelation) throws DataAccessException {


		return getSqlSessionTemplate().update("SysUserRoleRelation.update", sysGroupUserRelation);


	}

	public int delete(int id) throws DataAccessException {


		return getSqlSessionTemplate().delete("SysUserRoleRelation.delete", (id));


	}
	
	public 	void deleteByUuid(long uuid){
		UserRoleRelationCriteria userRoleRelationCriteria = new UserRoleRelationCriteria();
		userRoleRelationCriteria.setUuid(uuid);
		int effected = count(userRoleRelationCriteria);
		logger.info("应当删除:" + effected + "条关联");
		getSqlSessionTemplate().delete("SysUserRoleRelation.deleteByUuid", uuid);	
	}


	public UserRoleRelation select(int id) throws DataAccessException {
		return (UserRoleRelation) getSqlSessionTemplate().selectOne("SysUserRoleRelation.select", id);
	}


	public List<UserRoleRelation> list(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(userRoleRelationCriteria, "sysUserRoleRelationCriteria must not be null");
		
		return getSqlSessionTemplate().selectList("SysUserRoleRelation.list", userRoleRelationCriteria);
	}


	public List<UserRoleRelation> listOnPage(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(userRoleRelationCriteria, "sysUserRoleRelationCriteria must not be null");
		Assert.notNull(userRoleRelationCriteria.getPaging(), "paging must not be null");
		
		int totalResults = count(userRoleRelationCriteria);
		Paging paging = userRoleRelationCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		
		return getSqlSessionTemplate().selectList("SysUserRoleRelation.list", userRoleRelationCriteria, rowBounds);
	}

	public int count(UserRoleRelationCriteria userRoleRelationCriteria) throws DataAccessException {
		Assert.notNull(userRoleRelationCriteria, "sysUserRoleRelationCriteria must not be null");
		
		return ((Integer) getSqlSessionTemplate().selectOne("SysUserRoleRelation.count", userRoleRelationCriteria)).intValue();
	}

}
