package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;

public interface SysRoleDao {

	int insert(Role sysRole) throws DataAccessException;

	int update(Role sysRole) throws DataAccessException;

	int delete(int roleId) throws DataAccessException;

	Role select(int roleId) throws DataAccessException;

	List<Role> list(RoleCriteria roleCriteria) throws DataAccessException;
	
	List<Role> listOnPage(RoleCriteria roleCriteria) throws DataAccessException;
	
	int count(RoleCriteria roleCriteria) throws DataAccessException;
		
	int maxGroupLevel() throws DataAccessException;
	
	int maxId()  throws DataAccessException;

}
