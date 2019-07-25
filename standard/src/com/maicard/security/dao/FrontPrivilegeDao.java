package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

public interface FrontPrivilegeDao {

	int insert(Privilege frontPrivilege) throws DataAccessException;

	int update(Privilege frontPrivilege) throws DataAccessException;

	int delete(int privilegeId) throws DataAccessException;

	Privilege select(int privilegeId) throws DataAccessException;

	List<Privilege> list(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException;
	
	List<Privilege> listOnPage(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException;
	
	List<User> getFrontUserByPrivilege(PrivilegeCriteria frontPrivilegeCriteria);
	
	int count(PrivilegeCriteria frontPrivilegeCriteria) throws DataAccessException;

}
