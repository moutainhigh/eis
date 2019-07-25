package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.PrivilegeRoleRelationCriteria;
import com.maicard.security.dao.FrontPrivilegeRoleRelationDao;
import com.maicard.security.domain.PrivilegeRoleRelation;
import com.maicard.security.service.FrontPrivilegeRoleRelationService;

@Service
public class FrontPrivilegeRoleRelationServiceImpl extends BaseService implements FrontPrivilegeRoleRelationService {

	@Resource
	private FrontPrivilegeRoleRelationDao frontPrivilegeRoleRelationDao;

	public int insert(PrivilegeRoleRelation privilegeRoleRelation) {
		try{
			return frontPrivilegeRoleRelationDao.insert(privilegeRoleRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(PrivilegeRoleRelation privilegeRoleRelation) {
		try{
			return  frontPrivilegeRoleRelationDao.update(privilegeRoleRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int frontPrivilegeRoleRelationId) {
		try{
			return  frontPrivilegeRoleRelationDao.delete(frontPrivilegeRoleRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;		
		
	}
	
	public PrivilegeRoleRelation select(int frontPrivilegeRoleRelationId) {
		return frontPrivilegeRoleRelationDao.select(frontPrivilegeRoleRelationId);
	}

	public List<PrivilegeRoleRelation> list(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) {
		return frontPrivilegeRoleRelationDao.list(frontPrivilegeRoleRelationCriteria);
	}
	
	public List<PrivilegeRoleRelation> listOnPage(PrivilegeRoleRelationCriteria frontPrivilegeRoleRelationCriteria) {
		return frontPrivilegeRoleRelationDao.listOnPage(frontPrivilegeRoleRelationCriteria);
	}
	
	public 	void deleteByRoleId(int frontRoleId){
		frontPrivilegeRoleRelationDao.deleteByFrontRoleId(frontRoleId);
	}


}
