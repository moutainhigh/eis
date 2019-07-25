package com.maicard.security.service;

import java.util.List;

import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;

public interface FrontRoleService {

	int insert(Role frontRole);

	int update(Role frontRole);

	int delete(int frontRoleId);
	
	Role select(int frontRoleId);

	List<Role> list(RoleCriteria frontRoleCriteria);

	List<Role> listOnPage(RoleCriteria frontRoleCriteria);
	
	int count(RoleCriteria frontRoleCriteria);

	List<Role> getUnLoginedUserRoles(); //返回未注册/未登录用户的角色
}
