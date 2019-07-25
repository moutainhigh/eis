package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.domain.Privilege;

public interface FrontPrivilegeService {

	int insert(Privilege frontPrivilege);

	int update(Privilege frontPrivilege);

	int delete(int privilegeId);
	
	Privilege select(int privilegeId);

	List<Privilege> list(PrivilegeCriteria frontPrivilegeCriteria);

	List<Privilege> listOnPage(PrivilegeCriteria frontPrivilegeCriteria);

	int count(PrivilegeCriteria frontPrivilegeCriteria);

}
