package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.dao.PartnerPrivilegeRoleRelationDao;
import com.maicard.security.domain.PrivilegeRoleRelation;
import com.maicard.security.service.PartnerPrivilegeRoleRelationService;

@Service
public class PartnerPrivilegeRoleRelationServiceImpl extends BaseService implements PartnerPrivilegeRoleRelationService {

	@Resource
	private PartnerPrivilegeRoleRelationDao partnerPrivilegeRoleRelationDao;


	public void insert(PrivilegeRoleRelation sysPrivilegeGroupRelation) {
		partnerPrivilegeRoleRelationDao.insert(sysPrivilegeGroupRelation);
	}

	public int update(PrivilegeRoleRelation sysPrivilegeGroupRelation) {
		try{
			return  partnerPrivilegeRoleRelationDao.update(sysPrivilegeGroupRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int sysPrivilegeGroupRelationId) {
		try{
			return partnerPrivilegeRoleRelationDao.delete(sysPrivilegeGroupRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	
	public PrivilegeRoleRelation select(int sysPrivilegeGroupRelationId) {
		return partnerPrivilegeRoleRelationDao.select(sysPrivilegeGroupRelationId);
	}

	public List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) {
		return partnerPrivilegeRoleRelationDao.list(privilegeRoleRelationCriteria);
	}
	
	public List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria privilegeRoleRelationCriteria) {
		return partnerPrivilegeRoleRelationDao.listOnPage(privilegeRoleRelationCriteria);
	}

	

}
