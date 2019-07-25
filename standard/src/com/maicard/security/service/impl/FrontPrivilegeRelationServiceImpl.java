package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.dao.FrontPrivilegeRelationDao;
import com.maicard.security.domain.PrivilegeRelation;
import com.maicard.security.service.FrontPrivilegeRelationService;

@Service
public class FrontPrivilegeRelationServiceImpl extends BaseService implements FrontPrivilegeRelationService {

	@Resource
	private FrontPrivilegeRelationDao frontPrivilegeRelationDao;

	public int  insert(PrivilegeRelation frontPrivilegeRelation) {
		try{
			return frontPrivilegeRelationDao.insert(frontPrivilegeRelation);
			}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(PrivilegeRelation frontPrivilegeRelation) {
		try{
			return  frontPrivilegeRelationDao.update(frontPrivilegeRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;	
	}

	public int delete(int privilegeId) {
		try{
			return frontPrivilegeRelationDao.delete(privilegeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public void deleteByFrontPrivilegeId(int frontPrivilegeId){
		frontPrivilegeRelationDao.deleteByFrontPrivilegeId(frontPrivilegeId);
	}
	
	public PrivilegeRelation select(int privilegeId) {
		return frontPrivilegeRelationDao.select(privilegeId);
	}

	public List<PrivilegeRelation> list(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) {
		return frontPrivilegeRelationDao.list(frontPrivilegeRelationCriteria);
	}
	
	public List<PrivilegeRelation> listOnPage(PrivilegeRelationCriteria frontPrivilegeRelationCriteria) {
		return frontPrivilegeRelationDao.listOnPage(frontPrivilegeRelationCriteria);
	}


}
