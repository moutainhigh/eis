package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserLevelProjectCriteria;
import com.maicard.security.dao.UserLevelProjectDao;
import com.maicard.security.domain.UserLevelProject;
import com.maicard.security.service.UserLevelProjectService;
import com.maicard.standard.BasicStatus;

@Service
public class UserLevelProjectServiceImpl extends BaseService implements UserLevelProjectService {

	@Resource
	private UserLevelProjectDao userLevelProjectDao;
	
	public int insert(UserLevelProject userLevelProject) {
		try{
			return userLevelProjectDao.insert(userLevelProject);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(UserLevelProject userLevelProject) {
		try{
			return  userLevelProjectDao.update(userLevelProject);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int delete(int userLevelProjectId) {
		try{
			return  userLevelProjectDao.delete(userLevelProjectId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public UserLevelProject select(int userLevelProjectId) {
		return userLevelProjectDao.select(userLevelProjectId);
	}
	
	@Override
	public UserLevelProject selectByLevel(long userLevelId) {
		UserLevelProjectCriteria userLevelProjectCriteria = new UserLevelProjectCriteria();
		userLevelProjectCriteria.setCurrentStatus(BasicStatus.normal.getId());
		userLevelProjectCriteria.setUserLevelId(userLevelId);
		List<UserLevelProject> userLevelProjectList = list(userLevelProjectCriteria);
		logger.debug("用户级别[" + userLevelId + "]的配置方案有[" + (userLevelProjectList == null ? -1 :userLevelProjectList.size()) + "]个");
		if(userLevelProjectList == null || userLevelProjectList.size() < 1){
			return null;
		}
		return userLevelProjectList.get(0);
	}

	public List<UserLevelProject> list(UserLevelProjectCriteria userLevelProjectCriteria) {
		return userLevelProjectDao.list(userLevelProjectCriteria);
	}
	
	public List<UserLevelProject> listOnPage(UserLevelProjectCriteria userLevelProjectCriteria) {
		return userLevelProjectDao.listOnPage(userLevelProjectCriteria);
	}
	
	public 	int count(UserLevelProjectCriteria userLevelProjectCriteria){
		return userLevelProjectDao.count(userLevelProjectCriteria);
	}

}
