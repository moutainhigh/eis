package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.security.criteria.PrivilegeRelationCriteria;
import com.maicard.security.dao.FrontPrivilegeDao;
import com.maicard.security.domain.Privilege;
import com.maicard.security.service.FrontPrivilegeService;
import com.maicard.standard.BasicStatus;


@Service
public class FrontPrivilegeServiceImpl extends BaseService implements FrontPrivilegeService {

	@Resource
	private FrontPrivilegeDao frontPrivilegeDao;
	
	public int  insert(Privilege frontPrivilege) {
		try{
			return frontPrivilegeDao.insert(frontPrivilege);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		}

	public int update(Privilege frontPrivilege) {
		try{
			return  frontPrivilegeDao.update(frontPrivilege);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int privilegeId) {
		try{
			return  frontPrivilegeDao.delete(privilegeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public Privilege select(int privilegeId) {
		Privilege frontPrivilege = frontPrivilegeDao.select(privilegeId);
		
		PrivilegeRelationCriteria frontPrivilegeRelationCriteria = new PrivilegeRelationCriteria();
		frontPrivilegeRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		frontPrivilegeRelationCriteria.setPrivilegeId(frontPrivilege.getPrivilegeId());
		//List<PrivilegeRelation> frontPrivilegeRelationList = frontPrivilegeRelationService.list(frontPrivilegeRelationCriteria);
		
		return frontPrivilege;

	}

	public List<Privilege> list(PrivilegeCriteria frontPrivilegeCriteria) {
		List<Privilege> frontPrivilegeList = frontPrivilegeDao.list(frontPrivilegeCriteria);
		if(frontPrivilegeList == null){
			return null;
		}
		PrivilegeRelationCriteria frontPrivilegeRelationCriteria = new PrivilegeRelationCriteria();
		frontPrivilegeRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		
		/*for(int i = 0; i< frontPrivilegeList.size(); i++){
			frontPrivilegeList.get(i).setId(frontPrivilegeList.get(i).getPrivilegeId());
			frontPrivilegeList.get(i).setIndex(i+1);
			frontPrivilegeList.get(i).setCurrentStatusName(BasicStatus.normal.findById(frontPrivilegeList.get(i).getCurrentStatus()).getName());
			frontPrivilegeRelationCriteria.setPrivilegeId(frontPrivilegeList.get(i).getPrivilegeId());
			//List<PrivilegeRelation> frontPrivilegeRelationList = frontPrivilegeRelationService.list(frontPrivilegeRelationCriteria);
			
			
		}*/
		return frontPrivilegeList;	
	}
	
	public List<Privilege> listOnPage(PrivilegeCriteria frontPrivilegeCriteria) {
		List<Privilege> frontPrivilegeList =  frontPrivilegeDao.listOnPage(frontPrivilegeCriteria);
		if(frontPrivilegeList == null){
			return null;
		}
		PrivilegeRelationCriteria frontPrivilegeRelationCriteria = new PrivilegeRelationCriteria();
		frontPrivilegeRelationCriteria.setCurrentStatus(BasicStatus.normal.getId());
		
		/*for(int i = 0; i< frontPrivilegeList.size(); i++){
			frontPrivilegeList.get(i).setId(frontPrivilegeList.get(i).getPrivilegeId());
			frontPrivilegeList.get(i).setIndex(i+1);
			frontPrivilegeList.get(i).setCurrentStatusName(BasicStatus.normal.findById(frontPrivilegeList.get(i).getCurrentStatus()).getName());
			frontPrivilegeRelationCriteria.setPrivilegeId(frontPrivilegeList.get(i).getPrivilegeId());
			//List<PrivilegeRelation> frontPrivilegeRelationList = frontPrivilegeRelationService.list(frontPrivilegeRelationCriteria);
			
			
		}*/
		return frontPrivilegeList;	
	}
	
	public 	int count(PrivilegeCriteria frontPrivilegeCriteria){
		return frontPrivilegeDao.count(frontPrivilegeCriteria);
	}

}
