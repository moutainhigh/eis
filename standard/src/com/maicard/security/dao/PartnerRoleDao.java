package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;

public interface PartnerRoleDao {

	int insert(Role partnerRole) throws DataAccessException;

	int update(Role partnerRole) throws DataAccessException;

	int delete(int roleId) throws DataAccessException;

	Role select(int roleId) throws DataAccessException;

	List<Role> list(RoleCriteria partnerRoleCriteria) throws DataAccessException;
	
	List<Role> listOnPage(RoleCriteria partnerRoleCriteria) throws DataAccessException;
	
	int count(RoleCriteria partnerRoleCriteria) throws DataAccessException;

}
