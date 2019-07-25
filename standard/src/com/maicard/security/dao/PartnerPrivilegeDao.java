package com.maicard.security.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

public interface PartnerPrivilegeDao {

	int insert(Privilege partnerPrivilege) throws DataAccessException;

	int update(Privilege partnerPrivilege) throws DataAccessException;

	int delete(int privilegeId) throws DataAccessException;

	Privilege select(int privilegeId) throws DataAccessException;

	List<Privilege> list(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException;
	
	List<Privilege> listOnPage(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException;
	
	int count(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException;

	List<User> getUserByPrivilege(PrivilegeCriteria partnerPrivilegeCriteria) throws DataAccessException;

	List<Privilege> listByRole(PrivilegeCriteria partnerPrivilegeCriteria);


}
