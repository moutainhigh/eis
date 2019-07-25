package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;

public interface PartnerRoleService {

	int insert(Role partnerRole);

	int update(Role partnerRole);

	int delete(int roleId);
	
	Role select(int roleId);

	List<Role> list(RoleCriteria partnerRoleCriteria);

	List<Role> listOnPage(RoleCriteria partnerRoleCriteria);

	int count(RoleCriteria roleCriteria);



}
