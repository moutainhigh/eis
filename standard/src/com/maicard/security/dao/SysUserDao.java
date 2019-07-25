package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserDynamicData;

public interface SysUserDao {

	int insert(User sysUser) throws DataAccessException;

	int update(User sysUser) throws DataAccessException;

	int delete(long uuid) throws DataAccessException;

	User select(long uuid) throws DataAccessException;

	List<User> list(UserCriteria userCriteria) throws DataAccessException;
	
	List<User> listOnPage(UserCriteria userCriteria) throws DataAccessException;
	
	int count(UserCriteria userCriteria) throws DataAccessException;

	int updateDynamicData(UserDynamicData userDynamicData);

}
