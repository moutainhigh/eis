package com.maicard.security.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.security.criteria.UserTypeCriteria;
import com.maicard.security.dao.UserTypeDao;
import com.maicard.security.domain.UserType;
import com.maicard.security.service.UserTypeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;

@Service
public class UserTypeServiceImpl extends BaseService implements UserTypeService {

	@Resource
	private UserTypeDao userTypeDao;
	
	@Resource
	private DataDefineService dataDefineService;

	public int insert(UserType userType) {
		return 1;
		/*if(userType.getDataDefineMap() != null){
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setObjectType(ObjectType.user.toString());
			dataDefineCriteria.setObjectId(userType.getUserTypeId());
			dataDefineCriteria.setObjectExtraId(userType.getUserExtraTypeId());
			dataDefineService.delete(dataDefineCriteria);
			for(DataDefine userDataDefinePolicy : userType.getDataDefineMap().values()){
				userDataDefinePolicy.setObjectType(ObjectType.user.toString());
				userDataDefinePolicy.setObjectId(userType.getUserTypeId());
				userDataDefinePolicy.setObjectExtraId(userType.getUserExtraTypeId());
				userDataDefinePolicy.setCurrentStatus(BasicStatus.normal.getId());
				dataDefineService.insert(userDataDefinePolicy);
			}

		}*/
	}

	public int update(UserType userType) {
		int actualRowsAffected = 0;

		int id = userType.getUserTypeId();

		UserType _oldUserType = userTypeDao.select(id);

		if (_oldUserType != null) {
			actualRowsAffected = userTypeDao.update(userType);
			if(userType.getDataDefineMap() != null){
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
				dataDefineCriteria.setObjectType(ObjectType.user.toString());
				dataDefineCriteria.setObjectId(userType.getUserTypeId());
				dataDefineCriteria.setObjectExtraId(userType.getUserExtraTypeId());
				dataDefineService.delete(dataDefineCriteria);
				for(DataDefine userDataDefinePolicy : userType.getDataDefineMap().values()){
					userDataDefinePolicy.setObjectType(ObjectType.user.toString());
					userDataDefinePolicy.setObjectId(userType.getUserTypeId());
					userDataDefinePolicy.setObjectExtraId(userType.getUserExtraTypeId());
					userDataDefinePolicy.setCurrentStatus(BasicStatus.normal.getId());
					dataDefineService.insert(userDataDefinePolicy);
				}

			}
		}

		return actualRowsAffected;
	}

	public void delete(UserTypeCriteria userTypeCriteria) {

		if(userTypeCriteria == null){
			return;
		}
		if(userTypeCriteria.getUserTypeId() < 1){
			return;
		}
		List<UserType> userTypeList = list(userTypeCriteria);
		if(userTypeList == null || userTypeList.size() < 1){
			return;
		}
		userTypeDao.deleteByCriteria(userTypeCriteria);
		for(UserType userType : userTypeList){
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setObjectType(ObjectType.user.toString());
			dataDefineCriteria.setObjectId(userType.getUserTypeId());
			dataDefineCriteria.setObjectExtraId(userType.getUserExtraTypeId());
			dataDefineService.delete(dataDefineCriteria);
		}

	}



	public List<UserType> list(UserTypeCriteria userTypeCriteria) {
		List<UserType> userTypeList = userTypeDao.list(userTypeCriteria);
		if(userTypeList != null){
			for(int i = 0; i < userTypeList.size(); i++){
				userTypeList.get(i).setIndex(i+1);
				afterFetch(userTypeList.get(i));
			}
		}
		return userTypeList;
	}

	public List<UserType> listOnPage(UserTypeCriteria userTypeCriteria) {
		List<UserType> userTypeList = userTypeDao.listOnPage(userTypeCriteria);
		if(userTypeList != null){
			for(int i = 0; i < userTypeList.size(); i++){
				userTypeList.get(i).setIndex(i+1);
				afterFetch(userTypeList.get(i));
			}
		}
		return userTypeList;	
	}

	@Override
	public int count(UserTypeCriteria userTypeCriteria) {
		return userTypeDao.count(userTypeCriteria);
	}

	private void afterFetch(UserType userType){
		if(userType == null){
			return;
		}
		
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(userType.getUserTypeId());
		dataDefineCriteria.setObjectExtraId(userType.getUserExtraTypeId());
		List<DataDefine> userDataDefinePolicyList = dataDefineService.list(dataDefineCriteria);
		userType.setDataDefineMap(new HashMap<String, DataDefine>());
		for(DataDefine  userDataDefinePolicy : userDataDefinePolicyList){
			userType.getDataDefineMap().put(userDataDefinePolicy.getDataCode(), userDataDefinePolicy);
		}
		
	}

	@Override
	public UserType select(int id) {
		UserType userType =  userTypeDao.select(id);	
		if(userType != null){
			afterFetch(userType);
		}
		return userType;
	}

	@Override
	public int delete(int id) {
		UserType _oldUserType = select(id);
		int actualRowsAffected = 0;
		if(_oldUserType != null){
			actualRowsAffected = userTypeDao.delete(id);
		}
		return actualRowsAffected;
	}

	@Override
	public UserType select(UserTypeCriteria userTypeCriteria) {
		if(userTypeCriteria == null){
			return null;
		}
		if(userTypeCriteria.getUserTypeId() < 1){
			return null;
		}
		if(userTypeCriteria.getUserExtraTypeId() < 1){
			return null;
		}
		List<UserType> userTypeList = userTypeDao.list(userTypeCriteria);
		if(userTypeList == null || userTypeList.size() != 1){
			return null;
		}	
		afterFetch(userTypeList.get(0));
		return userTypeList.get(0);
	}

	

}
