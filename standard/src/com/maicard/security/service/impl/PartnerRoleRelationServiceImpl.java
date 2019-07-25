package com.maicard.security.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserRoleRelationCriteria;
import com.maicard.security.dao.PartnerRoleRelationDao;
import com.maicard.security.domain.UserRoleRelation;
import com.maicard.security.service.PartnerRoleRelationService;

@Service
public class PartnerRoleRelationServiceImpl extends BaseService implements PartnerRoleRelationService {

	@Resource
	private PartnerRoleRelationDao partnerRoleRelationDao;


	public int insert(UserRoleRelation partnerRoleRelation) {
		try{
			return partnerRoleRelationDao.insert(partnerRoleRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(UserRoleRelation partnerRoleRelation) {
		try{
			return partnerRoleRelationDao.update(partnerRoleRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int partnerRoleRelationId) {
		try{
			return partnerRoleRelationDao.delete(partnerRoleRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public UserRoleRelation select(int partnerRoleRelationId) {
		return partnerRoleRelationDao.select(partnerRoleRelationId);
	}

	public List<UserRoleRelation> list(UserRoleRelationCriteria partnerRoleRelationCriteria) {
		List<UserRoleRelation> list = partnerRoleRelationDao.list(partnerRoleRelationCriteria);
		if(list == null){
			return Collections.emptyList();
		} else {
			return list;
		}
	}
	
	public List<UserRoleRelation> listOnPage(UserRoleRelationCriteria partnerRoleRelationCriteria) {
		return partnerRoleRelationDao.listOnPage(partnerRoleRelationCriteria);
	}

	@Override
	public void deleteByUuid(long uuid) {
		partnerRoleRelationDao.deleteByUuid(uuid);
		
	}

}
