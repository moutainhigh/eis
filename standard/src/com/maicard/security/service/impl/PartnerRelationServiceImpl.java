package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.dao.PartnerRelationDao;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.PartnerRelationService;

@Service
public class PartnerRelationServiceImpl extends BaseService implements PartnerRelationService {

	@Resource
	private PartnerRelationDao partnerRelationDao;

	public int insert(UserRelation partnerRelation) {
		try{
			return partnerRelationDao.insert(partnerRelation);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;	
	}

	public int update(UserRelation partnerRelation) {
		try{
			return  partnerRelationDao.update(partnerRelation);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	
	}

	public int delete(int partnerRelationId) {
		try{
			return   partnerRelationDao.delete(partnerRelationId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public UserRelation select(int partnerRelationId) {
		return partnerRelationDao.select(partnerRelationId);
	}

	public List<UserRelation> list(UserRelationCriteria partnerRelationCriteria) {
		return partnerRelationDao.list(partnerRelationCriteria);
	}
	
	public List<UserRelation> listOnPage(UserRelationCriteria partnerRelationCriteria) {
		return partnerRelationDao.listOnPage(partnerRelationCriteria);
	}
	
	public int count(UserRelationCriteria partnerRelationCriteria) {
		return partnerRelationDao.count(partnerRelationCriteria);
	}

}
