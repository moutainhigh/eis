package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.util.JsonUtils;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.dao.PartnerPrivilegeDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerPrivilegeService;


@Service
public class PartnerPrivilegeServiceImpl extends BaseService implements PartnerPrivilegeService {

	@Resource
	private PartnerPrivilegeDao partnerPrivilegeDao;

	
	public int insert(Privilege partnerPrivilege) {
		try{
			return partnerPrivilegeDao.insert(partnerPrivilege);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Privilege partnerPrivilege) {
		try{
			return  partnerPrivilegeDao.update(partnerPrivilege);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	
	}

	public int delete(int privilegeId) {
		try{
			return  partnerPrivilegeDao.delete(privilegeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public Privilege select(int privilegeId) {
		return partnerPrivilegeDao.select(privilegeId);
	}

	public List<Privilege> list(PrivilegeCriteria partnerPrivilegeCriteria) {
		return partnerPrivilegeDao.list(partnerPrivilegeCriteria);
	}
	
	public List<Privilege> listOnPage(PrivilegeCriteria partnerPrivilegeCriteria) {
		return partnerPrivilegeDao.listOnPage(partnerPrivilegeCriteria);
	}
	
	@Override
	public List<Privilege> listByRole(PrivilegeCriteria partnerPrivilegeCriteria) {
		//根据角色列出权限，那么角色ID就不能是空
		Assert.isTrue(partnerPrivilegeCriteria.getRoleIds().length > 0, "根据角色列出权限的角色ID不能是空");
		Assert.isTrue(partnerPrivilegeCriteria.getRoleIds()[0] > 0, "根据角色列出权限的角色，第一个角色ID不能是0:" + JsonUtils.toStringFull(partnerPrivilegeCriteria.getRoleIds()));

		return partnerPrivilegeDao.listByRole(partnerPrivilegeCriteria);
	}
	
	public int count(PrivilegeCriteria partnerPrivilegeCriteria) {
		return partnerPrivilegeDao.count(partnerPrivilegeCriteria);
	}

	@Override
	public List<User> getUserByPrivilege(
			PrivilegeCriteria privilegeCriteria) {
		return partnerPrivilegeDao.getUserByPrivilege(privilegeCriteria);
	}


}
