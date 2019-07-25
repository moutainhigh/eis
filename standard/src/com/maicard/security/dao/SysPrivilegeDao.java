package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

public interface SysPrivilegeDao {

	int insert(Privilege sysPrivilege) throws DataAccessException;

	int update(Privilege sysPrivilege) throws DataAccessException;

	int delete(int privilegeId) throws DataAccessException;

	Privilege select(int privilegeId) throws DataAccessException;

	List<Privilege> list(PrivilegeCriteria privilegeCriteria) throws DataAccessException;
	
	List<Privilege> listOnPage(PrivilegeCriteria privilegeCriteria) throws DataAccessException;
	
	int count(PrivilegeCriteria privilegeCriteria) throws DataAccessException;
	
	int maxId() throws DataAccessException;
	
	List<User> getSysUserByPrivilege(PrivilegeCriteria privilegeCriteria) throws DataAccessException;
	

}
