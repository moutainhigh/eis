package com.maicard.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.dao.FrontRoleDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.domain.PrivilegeRoleRelation;
import com.maicard.security.domain.Role;
import com.maicard.security.service.FrontPrivilegeRoleRelationService;
import com.maicard.security.service.FrontPrivilegeService;
import com.maicard.security.service.FrontRoleService;
import com.maicard.standard.BasicStatus;

@Service
public class FrontRoleServiceImpl extends BaseService implements FrontRoleService {

	@Resource
	private FrontRoleDao frontRoleDao;
	@Resource
	private FrontPrivilegeRoleRelationService frontPrivilegeRoleRelationService;
	@Resource
	private FrontPrivilegeService frontPrivilegeService;


	public int insert(Role frontRole) {
		int rs = 0;
		try{
			rs = frontRoleDao.insert(frontRole);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			return 0;
		}

		frontPrivilegeRoleRelationService.deleteByRoleId(frontRole.getRoleId());
		if(frontRole.getRelatedPrivilegeList() != null){
			List<Privilege> relatedFrontPrivilegeList = frontRole.getRelatedPrivilegeList(); 
			for(int i = 0; i < relatedFrontPrivilegeList.size(); i++){
				PrivilegeRoleRelation privilegeRoleRelation = new  PrivilegeRoleRelation();
				privilegeRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				privilegeRoleRelation.setRoleId(frontRole.getRoleId());
				privilegeRoleRelation.setPrivilegeId(relatedFrontPrivilegeList.get(i).getPrivilegeId());
				frontPrivilegeRoleRelationService.insert(privilegeRoleRelation);
			}
		} else {
			logger.warn("新增的前台角色未包含对应权限:" + frontRole.getRoleId());
		}

		return frontRole.getRoleId(); 
	}

	public int update(Role frontRole) {

		try{
			if(  frontRoleDao.update(frontRole) != 1){
				return 0;
			}
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
			return -1;
		}

		frontPrivilegeRoleRelationService.deleteByRoleId(frontRole.getRoleId());
		if(frontRole.getRelatedPrivilegeList() != null){
			List<Privilege> relatedFrontPrivilegeList = frontRole.getRelatedPrivilegeList(); 
			for(int i = 0; i < relatedFrontPrivilegeList.size(); i++){
				PrivilegeRoleRelation privilegeRoleRelation = new  PrivilegeRoleRelation();
				privilegeRoleRelation.setCurrentStatus(BasicStatus.normal.getId());
				privilegeRoleRelation.setRoleId(frontRole.getRoleId());
				privilegeRoleRelation.setPrivilegeId(relatedFrontPrivilegeList.get(i).getPrivilegeId());
				frontPrivilegeRoleRelationService.insert(privilegeRoleRelation);
			}
		} else {
			logger.warn("新增的前台角色未包含对应权限:" + frontRole.getRoleId());
		}
		return 1;
	}

	public int delete(int frontRoleId) {
		try{

			frontRoleDao.delete(frontRoleId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
			return -1;
		}
		frontPrivilegeRoleRelationService.deleteByRoleId(frontRoleId);

		return 1;
	}

	public Role select(int frontRoleId) {
		Role frontRole =  frontRoleDao.select(frontRoleId);
		PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
		frontPrivilegeRoleRelationCriteria.setRoleId(frontRoleId);
		List<PrivilegeRoleRelation> frontPrivilegeRoleRelationList = frontPrivilegeRoleRelationService.list(frontPrivilegeRoleRelationCriteria);
		ArrayList<Privilege> relatedPrivilegeList = new ArrayList<Privilege>();
		if(frontPrivilegeRoleRelationList != null){
			for(int i = 0; i < frontPrivilegeRoleRelationList.size(); i++){
				Privilege frontPrivilege = frontPrivilegeService.select(frontPrivilegeRoleRelationList.get(i).getPrivilegeId());
				if(frontPrivilege != null){
					relatedPrivilegeList.add(frontPrivilege);
				}
			}
		}
		frontRole.setRelatedPrivilegeList(relatedPrivilegeList);		
		return frontRole;
	}

	public List<Role> list(RoleCriteria frontRoleCriteria) {
		List<Role> frontRoleList = frontRoleDao.list(frontRoleCriteria);
		if(frontRoleList == null){
			return null;
		}
		for(int i = 0; i < frontRoleList.size(); i++){
			frontRoleList.get(i).setId(frontRoleList.get(i).getRoleId());
			frontRoleList.get(i).setIndex(i+1);
			PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
			frontPrivilegeRoleRelationCriteria.setRoleId(frontRoleList.get(i).getRoleId());
			List<PrivilegeRoleRelation> frontPrivilegeRoleRelationList = frontPrivilegeRoleRelationService.list(frontPrivilegeRoleRelationCriteria);
			ArrayList<Privilege> relatedPrivilegeList = new ArrayList<Privilege>();
			if(frontPrivilegeRoleRelationList != null){
				for(int j = 0; j < frontPrivilegeRoleRelationList.size(); j++){
					Privilege frontPrivilege = frontPrivilegeService.select(frontPrivilegeRoleRelationList.get(j).getPrivilegeId());
					if(frontPrivilege != null){
						relatedPrivilegeList.add(frontPrivilege);
					}
				}
			}
			frontRoleList.get(i).setRelatedPrivilegeList(relatedPrivilegeList);	
		}
		return frontRoleList;
	}

	public List<Role> listOnPage(RoleCriteria frontRoleCriteria) {
		List<Role> frontRoleList = frontRoleDao.listOnPage(frontRoleCriteria);
		if(frontRoleList == null){
			return null;
		}
		for(int i = 0; i < frontRoleList.size(); i++){
			frontRoleList.get(i).setId(frontRoleList.get(i).getRoleId());
			frontRoleList.get(i).setIndex(i+1);
			PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria = new PrivilegeRoleRelationCriteria();
			frontPrivilegeRoleRelationCriteria.setRoleId(frontRoleList.get(i).getRoleId());
			List<PrivilegeRoleRelation> frontPrivilegeRoleRelationList = frontPrivilegeRoleRelationService.list(frontPrivilegeRoleRelationCriteria);
			ArrayList<Privilege> relatedPrivilegeList = new ArrayList<Privilege>();
			if(frontPrivilegeRoleRelationList != null){
				for(int j = 0; j < frontPrivilegeRoleRelationList.size(); j++){
					Privilege frontPrivilege = frontPrivilegeService.select(frontPrivilegeRoleRelationList.get(j).getPrivilegeId());
					if(frontPrivilege != null){
						relatedPrivilegeList.add(frontPrivilege);
					}
				}
			}
			frontRoleList.get(i).setRelatedPrivilegeList(relatedPrivilegeList);	
		}
		return frontRoleList;
	}

	public 	int count(RoleCriteria frontRoleCriteria){
		return frontRoleDao.count(frontRoleCriteria);
	}

	@Override
	public List<Role> getUnLoginedUserRoles() {
		Role role = select(1);
		List<Role> roleList = new ArrayList<Role>();
		roleList.add(role);
		return roleList;
	}

}
