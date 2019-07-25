package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;

public interface PartnerPrivilegeService {

	int insert(Privilege partnerPrivilege);

	int update(Privilege partnerPrivilege);

	int delete(int privilegeId);
	
	Privilege select(int privilegeId);

	List<Privilege> list(PrivilegeCriteria partnerPrivilegeCriteria);

	List<Privilege> listOnPage(PrivilegeCriteria partnerPrivilegeCriteria);

	int count(PrivilegeCriteria partnerPrivilegeCriteria);

	List<User> getUserByPrivilege(PrivilegeCriteria privilegeCriteria);

	List<Privilege> listByRole(PrivilegeCriteria privilegeCriteria);

}
