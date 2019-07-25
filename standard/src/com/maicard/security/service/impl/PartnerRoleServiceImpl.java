package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.dao.PartnerRoleDao;
import com.maicard.security.domain.Role;
import com.maicard.security.service.PartnerRoleService;
import com.maicard.standard.BasicStatus;

@Service
public class PartnerRoleServiceImpl extends BaseService implements PartnerRoleService {

	@Resource
	private PartnerRoleDao partnerRoleDao;

	public int insert(Role partnerRole) {
		try{
			return partnerRoleDao.insert(partnerRole);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(Role partnerRole) {
		try{
			return  partnerRoleDao.update(partnerRole);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int roleId) {
		try{
			return partnerRoleDao.delete(roleId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public Role select(int roleId) {
		Role role = partnerRoleDao.select(roleId);
		if(role == null){
			return null;
		}
		/*logger.info("roleId=" + roleId + "的合作伙伴角色，对应权限数量是" + (role.getRelatedPrivilegeList() == null ? "空" : role.getRelatedPrivilegeList().size()));
		if(role.getRelatedPrivilegeList() != null){
			for(Privilege privilege : role.getRelatedPrivilegeList()){
				logger.info("roleId=" + roleId + "的合作伙伴角色，对应权限:" + privilege.getPrivilegeId() + "/" + privilege.getPrivilegeName());
			}
		}*/
		
		return role;
	}

	public List<Role> list(RoleCriteria partnerRoleCriteria) {
		
		List<Role> partnerRoleList =  partnerRoleDao.list(partnerRoleCriteria);
		if(partnerRoleList == null){
			return null;
		}
		for(int i = 0; i < partnerRoleList.size(); i++){
			partnerRoleList.get(i).setId(partnerRoleList.get(i).getRoleId());
			partnerRoleList.get(i).setIndex(i+1);
			
		}
		return partnerRoleList;
		
	}
	
	public List<Role> listOnPage(RoleCriteria partnerRoleCriteria) {
		return partnerRoleDao.listOnPage(partnerRoleCriteria);
	}
	public int count(RoleCriteria partnerRoleCriteria) {
		return partnerRoleDao.count(partnerRoleCriteria);
	}




}
