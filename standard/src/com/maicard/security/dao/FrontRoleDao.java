package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;

public interface FrontRoleDao {

	int insert(Role frontRole) throws DataAccessException;

	int update(Role frontRole) throws DataAccessException;

	int delete(int frontRoleId) throws DataAccessException;

	Role select(int frontRoleId) throws DataAccessException;

	List<Role> list(RoleCriteria frontRoleCriteria) throws DataAccessException;
	
	List<Role> listOnPage(RoleCriteria frontRoleCriteria) throws DataAccessException;
	
	int count(RoleCriteria frontRoleCriteria) throws DataAccessException;

}
