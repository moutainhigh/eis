package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.FrontUserRoleRelationDao;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.FrontUserRoleRelationService;

@Service
public class FrontUserRoleRelationServiceImpl extends BaseService implements FrontUserRoleRelationService {

	@Resource
	private FrontUserRoleRelationDao frontUserRoleRelationDao;



	public int insert(UserRoleRelation userRoleRelation) {
		try{
			return frontUserRoleRelationDao.insert(userRoleRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(UserRoleRelation userRoleRelation) {
		try{
			return frontUserRoleRelationDao.update(userRoleRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;		
		
	}

	public int delete(int frontUserRoleRelationId) {
		try{
			return  frontUserRoleRelationDao.delete(frontUserRoleRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public void deleteByFuuid(long fuuid) {		
		frontUserRoleRelationDao.deleteByUuid(fuuid);
	}
	
	
	public UserRoleRelation select(int frontUserRoleRelationId) {
		return frontUserRoleRelationDao.select(frontUserRoleRelationId);
	}

	public List<UserRoleRelation> list(UserRoleRelationCriteria frontUserRoleRelationCriteria) {
		return frontUserRoleRelationDao.list(frontUserRoleRelationCriteria);
	}
	
	public List<UserRoleRelation> listOnPage(UserRoleRelationCriteria frontUserRoleRelationCriteria) {
		return frontUserRoleRelationDao.listOnPage(frontUserRoleRelationCriteria);
	}


}
