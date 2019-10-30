package com.maicard.security.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.annotation.IgnoreJmsDataSync;
import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.JsonUtils;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.dao.UserDataDao;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.UserDataService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;

@Service
public class UserDataServiceImpl extends BaseService implements UserDataService {

	@Resource
	private UserDataDao userDataDao;

	@Resource
	private DataDefineService dataDefineService;



	@IgnoreJmsDataSync
	public int insert(UserData userData)  {
		if(logger.isDebugEnabled()){
			logger.debug("准备插入数据[dataDefineId=" + userData.getDataDefineId() + ", dataCode=" + userData.getDataCode() + ",dataValue=" + userData.getDataValue() + ",是否允许多个同名数据存在:" + "]");
		}
		if(userData.getUserDataId() < 1 && userData.getDataCode() == null){
			return 0;
		}
		if(userData.getUserDataId() < 1 && userData.getDataDefineId() < 1){
			DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
			dataDefineCriteria.setDataCode(userData.getDataCode());
			DataDefine dd = dataDefineService.select(dataDefineCriteria);
			if(dd == null){
				logger.warn("未能找到dataCode=" + userData.getDataCode() + "的数据定义");
				return 0;
			}
			userData.setDataDefineId(dd.getDataDefineId());
		} 

		try{
			if(logger.isDebugEnabled()){
				logger.debug("插入数据[dataDefineId=" + userData.getDataDefineId() + ", dataCode=" + userData.getDataCode() + ",dataValue=" + userData.getDataValue() + ",是否允许多个同名数据存在:" + "]");
			}
			return userDataDao.insert(userData);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@IgnoreJmsDataSync
	public int update(UserData userData){
		if(userData == null){
			return -1;
		}
		if(userData.getUuid() < 1){
			return -1;
		}
		/*		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(userData.getUuid());
		userDataCriteria.setUserDataId(userData.getUserDataId());
		UserData _oldUserConfig = userDataDao.select(userDataCriteria);

		if (_oldUserConfig != null) {
			if(_oldUserConfig.getTtl() > 0){			
				if( (int)((System.currentTimeMillis() - _oldUserConfig.getCreateTime().getTime()) / 1000) > _oldUserConfig.getTtl()){
					logger.debug("配置已存活[" + (int)((System.currentTimeMillis() - _oldUserConfig.getCreateTime().getTime()) / 1000) + "秒，应存活时间:" + _oldUserConfig.getTtl() + ",自动删除超过存活期的配置[" + userDataCriteria.getUuid() + "/" + userDataCriteria.getUserDataId() + "]" );
					return delete(userDataCriteria);
				}
			}*/
		//}
		try{
			return  userDataDao.update(userData);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}

	
	@IgnoreJmsDataSync
	@Override
	public int replace(UserData userData) throws Exception{
		if(userData == null){
			return -1;
		}
		if(userData.getUuid() < 1){
			return -1;
		}
		if(userData.getUserDataId() > 0) {
			return userDataDao.update(userData);
		}
		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(userData.getUuid());
		userDataCriteria.setUserDataId(userData.getUserDataId());
		userDataCriteria.setDataCode(userData.getDataCode());
		userDataCriteria.setDataDefineId(userData.getDataDefineId());
		userDataCriteria.setUserTypeId((int)userData.getObjectId());
		UserData _oldUserConfig = selectByCriteria(userDataCriteria);

		if (_oldUserConfig != null && _oldUserConfig.getUuid() > 0 && _oldUserConfig.getUuid() == userData.getUuid()) {
			logger.debug("找到了已存在的数据:{}，更新值为:{}", JsonUtils.toStringFull(_oldUserConfig), userData.getDataValue());
			_oldUserConfig.setDataValue(userData.getDataValue());
			return userDataDao.update(_oldUserConfig);
		} else {
			if(userData.getDataDefineId() > 0 || userData.getUserDataId() > 0) {
				//已经有数据定义
			} else {
				if(StringUtils.isBlank(userData.getDataCode())) {
					logger.error("尝试替换-新增的用户数据没有dataDefineId、userDataId，也没有dataCode，无法新增", JsonUtils.toStringFull(userData));
					return 0;
				}
				DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
				dataDefineCriteria.setObjectType(ObjectType.user.name());
				dataDefineCriteria.setObjectId(userData.getObjectId());
				DataDefine dd = dataDefineService.select(dataDefineCriteria);
				if(dd == null) {
					logger.error("尝试替换-新增的用户数据，找不到对应的DataDefine无法新增", JsonUtils.toStringFull(userData));
					return 0;
				}
				logger.debug("为替换-新增的用户数据找到了对应的DataDefine={}", dd.getDataDefineId());
				userData.setDataDefineId(dd.getDataDefineId());
			}
			return userDataDao.insert(userData);
		}
	}

	@Override
	public UserData selectByCriteria(UserDataCriteria userDataCriteria) {
		List<UserData> list = list(userDataCriteria);
		if(list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	public UserData select(UserDataCriteria userDataCriteria){
		List<String> pkList = null;
		try {
			pkList = userDataDao.listPk(userDataCriteria);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(pkList == null || pkList.size() < 1){
			return null;
		}
		if(pkList.size() != 1){
			logger.warn("根据查询条件[" + userDataCriteria + "]查询的结果不是1，是" + pkList.size());
			return null;
		}

		if(StringUtils.isBlank(pkList.get(0))){
			logger.warn("第一个返回数据是空");
			return null;
		}
		String[] data = StringUtils.split(pkList.get(0), '#');
		if(data == null || data.length != 2){
			logger.warn("无法将第一个返回数据用#分割");
			return null;
		}
		if(StringUtils.isNumeric(data[0]) && StringUtils.isNumeric(data[1])){
			userDataCriteria.setUuid(Integer.parseInt(data[0]));
			userDataCriteria.setUserDataId(Integer.parseInt(data[1]));
			try {
				return userDataDao.select(userDataCriteria);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		logger.warn("未能处理！");

		return null;
	}

	public List<UserData> list(UserDataCriteria userDataCriteria) {
		if(userDataCriteria == null){
			throw new RequiredObjectIsNullException("未提供查询条件");
		}
		if(userDataCriteria.getUserTypeId() < 1){
			throw new RequiredObjectIsNullException("未提供用户类型");
		}

		List<UserData> userDataList = null;
		try{
			userDataList = userDataDao.list(userDataCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}


		if(logger.isDebugEnabled()){
			logger.debug("当前从数据库返回的userData数据列表是[" + (userDataList == null ? -1 :userDataList.size()) + "]");
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.name());
		dataDefineCriteria.setObjectId(userDataCriteria.getUserTypeId());
		dataDefineCriteria.setObjectExtraId(userDataCriteria.getUserExtraTypeId());
		List<DataDefine> userDataDefinePolicyList = dataDefineService.list(dataDefineCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("用户[" + userDataCriteria.getUuid() + "/" + userDataCriteria.getUserTypeId() + "/" + userDataCriteria.getUserExtraTypeId() + "]对应[" + (userDataDefinePolicyList == null ? -1 : userDataDefinePolicyList.size()) + "]个用户可用配置数据规则");
		}
		//过滤那些未在规范中定义的数据
		if(userDataList != null){
			if(userDataDefinePolicyList != null && userDataDefinePolicyList.size() > 0){
				for(UserData userData : userDataList ){
					boolean isValid = false;
					for(DataDefine userConfigDataMapPolicy : userDataDefinePolicyList ){
						if(userConfigDataMapPolicy.getDataDefineId() == userData.getDataDefineId()){
							isValid = true;
							break;
						}
					}
					if(!isValid){
						if(logger.isDebugEnabled()){
							logger.debug("数据[dataCode=" + userData.getDataCode() + ",dataDefineId=" + userData.getDataDefineId() + "dataValue=" + userData.getDataValue() + "]不符合数据规范，强行删除");
						}
						userDataList.remove(userData);
						break;
					}
				}

			} else {
				userDataList = null;
			}
		}
		if(userDataDefinePolicyList != null && userDataDefinePolicyList.size() > 0){
			if(userDataList != null){
				for(DataDefine userConfigDataMapPolicy : userDataDefinePolicyList ){
					boolean exist = false;
					for(UserData userData : userDataList ){
						if(userConfigDataMapPolicy.getDataDefineId() == userData.getDataDefineId()){
							//将已存在的配置的状态码设置为规则中的状态
							userData.setCurrentStatus(userConfigDataMapPolicy.getCurrentStatus());
							exist = true;
							break;
						}
					}
					if(!exist && userDataCriteria.isCorrectWithDynamicData()){
						DataDefine dataDefine = dataDefineService.select(userConfigDataMapPolicy.getDataDefineId());
						UserData uc = new UserData(dataDefine);
						uc.setUuid(userDataCriteria.getUuid());
						if(logger.isDebugEnabled()){
							logger.debug("添加之前不存在的配置字段[" + dataDefine.getDataCode() + "]");
						}
						userDataList.add(uc);
					}
				}

			}
		}
		//logger.debug("返回前的配置数据:" + userDataCriteria.getUuid() + "/" + (userDataList == null ? 0 : userDataList.size()));
		if( userDataList == null ) {
			return Collections.emptyList();
		} else {
			return userDataList;
		}
	}




	public List<UserData> listOnPage(UserDataCriteria userDataCriteria)  {
		if(userDataCriteria == null){
			throw new RequiredObjectIsNullException("未提供查询条件");
		}
		if(userDataCriteria.getUserTypeId() < 1){
			throw new RequiredObjectIsNullException("未提供用户类型");
		}
		/* 缓存模式
		List<String> pkList = null;
		try {
			pkList = userDataDao.listPkOnPage(userDataCriteria);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(pkList == null || pkList.size() < 1){
			return null;
		}
		List<UserData> userDataList = new ArrayList<UserData>();

		for(String pk : pkList){
			String[] data = StringUtils.split(pk, '#');
			if(data == null || data.length != 2){
				return null;
			}
			if(StringUtils.isNumeric(data[0]) && StringUtils.isNumeric(data[1])){
				userDataCriteria.setUuid(Integer.parseInt(data[0]));
				userDataCriteria.setUserDataId(Integer.parseInt(data[1]));
				try {
					UserData ud = userDataDao.select(userDataCriteria);
					if(ud != null){
						userDataList.add(ud);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		 */

		/* 非缓存模式 */
		List<UserData> userDataList = null;
		try{
			userDataList = userDataDao.listOnPage(userDataCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			logger.debug("当前从数据库返回的userData数据列表是[" + (userDataList == null ? -1 :userDataList.size()) + "]");
		}
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(userDataCriteria.getUserTypeId());
		dataDefineCriteria.setObjectExtraId(userDataCriteria.getUserExtraTypeId());
		List<DataDefine> userDataDefinePolicyList = dataDefineService.list(dataDefineCriteria);
		if(logger.isDebugEnabled()){
			logger.debug("用户[" + userDataCriteria.getUuid() + "/" + userDataCriteria.getUserTypeId() + "/" + userDataCriteria.getUserExtraTypeId() + "]对应[" + (userDataDefinePolicyList == null ? -1 : userDataDefinePolicyList.size()) + "]个用户可用配置数据规则");
		}
		//过滤那些未在规范中定义的数据
		if(userDataList != null){
			if(userDataDefinePolicyList != null && userDataDefinePolicyList.size() > 0){
				for(UserData userData : userDataList ){
					boolean isValid = false;
					for(DataDefine userConfigDataMapPolicy : userDataDefinePolicyList ){
						if(userConfigDataMapPolicy.getDataDefineId() == userData.getDataDefineId()){
							isValid = true;
							break;
						}
					}
					if(!isValid){
						if(logger.isDebugEnabled()){
							logger.debug("数据[dataCode=" + userData.getDataCode() + ",dataDefineId=" + userData.getDataDefineId() + "dataValue=" + userData.getDataValue() + "]不符合数据规范，强行删除");
						}
						userDataList.remove(userData);
						break;
					}
				}

			} else {
				userDataList = null;
			}
		}
		if(userDataDefinePolicyList != null && userDataDefinePolicyList.size() > 0){
			if(userDataList != null){
				for(DataDefine userConfigDataMapPolicy : userDataDefinePolicyList ){
					boolean exist = false;
					for(UserData userData : userDataList ){
						if(userConfigDataMapPolicy.getDataDefineId() == userData.getDataDefineId()){
							//将已存在的配置的状态码设置为规则中的状态
							userData.setCurrentStatus(userConfigDataMapPolicy.getCurrentStatus());
							exist = true;
							break;
						}
					}
					if(!exist && userDataCriteria.isCorrectWithDynamicData()){
						DataDefine dataDefine = dataDefineService.select(userConfigDataMapPolicy.getDataDefineId());
						UserData uc = new UserData(dataDefine);
						uc.setUuid(userDataCriteria.getUuid());
						if(logger.isDebugEnabled()){
							logger.debug("添加之前不存在的配置字段[" + dataDefine.getDataCode() + "]");
						}
						userDataList.add(uc);
					}
				}

			}
		}
		//logger.debug("返回前的配置数据:" + userDataCriteria.getUuid() + "/" + (userDataList == null ? 0 : userDataList.size()));
		return userDataList;
	}

	public 	int count(UserDataCriteria userDataCriteria){
		try{
			return userDataDao.count(userDataCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public HashMap<String, UserData>map(UserDataCriteria userDataCriteria){
		if(userDataCriteria == null){
			throw new RequiredObjectIsNullException("未提供查询条件");
		}
		if(userDataCriteria.getUserTypeId() < 1){
			throw new RequiredObjectIsNullException("未提供用户类型");
		}

		try{
			return (HashMap<String,UserData>)userDataDao.map(userDataCriteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}

	/* 更新用户配置
	 * 1、先得到旧的用户配置
	 * 2、把旧的跟新的对比，没有的删除
	 * 3、把新的跟旧的进行对比，有的更新，没有的插入
	 * 规则：根据updateMode判断是谁来更新，用户自行更新(self)，则不更新隐藏和只读字段
	 *      如果是系统更新，则可更新所有数据
	 */
	@Override
	public void processUserConfig(User user) throws Exception{

		UserDataCriteria userDataCriteria = new UserDataCriteria();
		userDataCriteria.setUuid(user.getUuid());
		userDataCriteria.setUserTypeId(user.getUserTypeId());
		userDataCriteria.setUserExtraTypeId(user.getUserExtraTypeId());
		List<UserData> oldUserConfigList = list(userDataCriteria);
		List<UserData> opereateConfigList = new ArrayList<UserData>();
		//  logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!,"+user.getUuid()+",!!!!"+ user.getUserConfigMap().get("userBindMailBox").getDataValue()+"!!!!");
		if(user.getUserConfigMap() == null || user.getUserConfigMap().size() < 1){
			/* 有旧的数据但没有新的数据
			 * 如果是系统更新，则删除所有旧数据
			 * 否则删除所有非只读的旧数据
			 */
			if(logger.isDebugEnabled()){
				logger.debug("用户没有任何数据配置，删除之前可能存在的数据");
			}
			userDataCriteria = new UserDataCriteria();
			userDataCriteria.setUuid(user.getUuid());

			//系统更新，删除普通状态和隐藏状态的数据（不删除只读状态的数据）
			userDataCriteria.setCurrentStatus(BasicStatus.normal.getId());
			delete(userDataCriteria);			
			userDataCriteria.setCurrentStatus(BasicStatus.hidden.getId());
			delete(userDataCriteria);			
			logger.warn("所有普通与隐藏状态的数据已被删除");
		} else {
			//先循环所有新数据，比对旧数据，如果
			/* 先比较旧数据跟新数据
			 * 如果旧数据有而新数据没有，则应当是删除
			 */
			logger.debug("用户有配置进行更新，共计" + user.getUserConfigMap().size() + "条数据");			

			int deleteCount = 0;
			if(oldUserConfigList != null){
				for(UserData uc : oldUserConfigList){
					if(uc.getUserDataId() < 1){
						logger.debug("不比较动态添加的用户数据[" + uc.getUserDataId() + "/" + uc.getDataCode() + "/" + uc.getDataDefineId() + "=>" + uc.getDataValue() + "/" + uc.getCurrentStatus() + "/"  + ":" + uc.getTtl() + "]");
						continue;
					}
					boolean delete = true;
					for(String key: user.getUserConfigMap().keySet()){
						logger.debug("比对当前用户的数据配置:" + key + ",dataDefineId=" + user.getUserConfigMap().get(key).getDataDefineId() + ", dataCode=" + user.getUserConfigMap().get(key).getDataCode() + ",dataValue=" + user.getUserConfigMap().get(key).getDataValue() + "与旧数据:" + ",dataDefineId=" + uc.getDataDefineId() + ", dataCode=" + uc.getDataCode() + ",dataValue=" + uc.getDataValue());

						if(key != null && key.equals(uc.getDataCode())){
							logger.debug("数据配置[" +  key + "" + uc.getDataDefineId() + "]相同,不删除");
							delete = false;
							break;
						}
					}
					if(delete){
						if(uc.getUserDataId() < 1){
							logger.debug("不尝试删除动态添加的用户数据[" + uc.getUserDataId() + "/" + uc.getDataCode() + "/" + uc.getDataDefineId() + "=>" + uc.getDataValue() + "/" + uc.getCurrentStatus() + "/" + ":" + uc.getTtl() + "]");
						} else {
							UserDataCriteria deleteUserConfigCriteria = new UserDataCriteria();
							deleteUserConfigCriteria.setUuid(uc.getUuid());
							deleteUserConfigCriteria.setUserDataId(uc.getUserDataId());
							if(uc.getCurrentStatus() == BasicStatus.normal.getId()){
								if(logger.isDebugEnabled()){
									logger.debug("用户" + uc.getUuid() + "数据[" + uc.getUserDataId() + "/" + uc.getDataCode() + "/" + uc.getDataDefineId() + "=>" + uc.getDataValue() + "/" + uc.getCurrentStatus() + "/" +  ":" + uc.getTtl() + "]已被删除");
								}
								delete(deleteUserConfigCriteria);
								deleteCount++;
							}
						}
					}
				}
			}
			if(deleteCount > 0){//重新加载旧数据
				if(logger.isDebugEnabled()){
					logger.debug("部分旧配置已删除，重新加载旧数据");
				}
				oldUserConfigList = list(userDataCriteria);
			}
			/* 再比较新数据与旧数据
			 * 如果是系统更新，则更新所有旧数据
			 * 否则只更新所有非只读的旧数据
			 */
			//List<UserConfig> newUserConfigList = new ArrayList<UserConfig>();
			if(logger.isDebugEnabled()){
				logger.debug("用户[" + user.getUuid() + "]内存配置中有" + (user.getUserConfigMap() == null ? "空" : user.getUserConfigMap().size()) + "条数据，旧配置有[" + (oldUserConfigList == null ? "空" : oldUserConfigList.size()) + "]条");
			}

			for(String key: user.getUserConfigMap().keySet()){
				boolean update = false;
				//logger.info("-------------------------------------------");
				if(oldUserConfigList != null){
					for(UserData uc : oldUserConfigList){
						//logger.debug("比对" + key + ":::::::::::" + uc.getDataCode() + "/" + uc.getDataValue() + "/" + uc.getFlag());
						if(key.equals(uc.getDataCode())  && uc.getDataDefineId() > 1){//存在相同名称且旧内容为空且非动态生成的数据
							/*//logger.info(key + ":::::::::::" + uc.getDataCode() + "/" + uc.getDataValue() + "/" + uc.getFlag() + "标记为update");

							user.getUserConfigMap().get(key).setUserConfigId(uc.getUserConfigId());
							user.getUserConfigMap().get(key).setDataDefineId(uc.getDataDefineId());
							update = true;
							if(uc.getCurrentStatus() == Constants.basicHidden){
								user.getUserConfigMap().get(key).setCurrentStatus(Constants.basicHidden);
								hidden = true;
							}
							if(uc.getCurrentStatus() == Constants.BasicStatus.readOnly.getId()){
								user.getUserConfigMap().get(key).setCurrentStatus(Constants.BasicStatus.readOnly.getId());
								readonly = true;
							}
							 */
							if(uc.getUserDataId() < 1){
								logger.debug("配置[" + key + "]已存在但UserDataId=0,需要新增，并将其dataDefineId从" + user.getUserConfigMap().get(key).getDataDefineId() + "设置为" + uc.getDataDefineId());
							} else {
								logger.debug("配置[" + key + "]已存在需要更新,将其dataDefineId从" + user.getUserConfigMap().get(key).getDataDefineId() + "设置为" + uc.getDataDefineId());
								update = true;
							}
							user.getUserConfigMap().get(key).setDataDefineId(uc.getDataDefineId());							
							user.getUserConfigMap().get(key).setUserDataId(uc.getUserDataId());	
							user.getUserConfigMap().get(key).setUuid(user.getUuid());	
							break;					
						}


					}
				}
				if(update){
					if(logger.isDebugEnabled()){
						logger.debug("当前的数据[" + key + "/" + ",userDataId=" + user.getUserConfigMap().get(key).getUserDataId() + ",dataDefineId=" + user.getUserConfigMap().get(key).getDataDefineId() + ",dataValue=" + user.getUserConfigMap().get(key).getDataValue() + "/" + user.getUserConfigMap().get(key).getCurrentStatus()  + ":" + user.getUserConfigMap().get(key).getTtl() + "]将被更新");
					}
					user.getUserConfigMap().get(key).setFlag(Operate.update.getId());
					//FIXME

					opereateConfigList.add(user.getUserConfigMap().get(key));


				} else { //新增	
					//从数据许可规则中进行过滤，不在许可列表中的数据不允许插入，同时，使用许可列表中定义的状态码代替数据中的状态码
					if(logger.isDebugEnabled()){
						logger.debug("[" + key + "]找不到旧数据");
					}
					DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
					DataDefine dd = null;
					dataDefineCriteria.setDataCode(key);
					dataDefineCriteria.setObjectType(ObjectType.user.name());
					dataDefineCriteria.setObjectId(user.getUserTypeId());
					dataDefineCriteria.setObjectExtraId(user.getUserExtraTypeId());
					dd = dataDefineService.select(dataDefineCriteria);
					if(dd != null){
						//	logger.info("!!!!!!!!!!!!!!!"+key+"!!!!!!!!!!!,"+user.getUserConfigMap().get(key).getDataValue()+"!!!!!!!!!!!!");
						user.getUserConfigMap().get(key).setUuid(user.getUuid());
						user.getUserConfigMap().get(key).setDataDefineId(dd.getDataDefineId());
						user.getUserConfigMap().get(key).setDataValue(user.getUserConfigMap().get(key).getDataValue());
						user.getUserConfigMap().get(key).setFlag(Operate.create.getId());
						opereateConfigList.add(user.getUserConfigMap().get(key));
						if(logger.isDebugEnabled()){
							logger.debug("数据[" + key + "/" + user.getUserConfigMap().get(key).getDataValue() + "/" + user.getUserConfigMap().get(key).getCurrentStatus()  + ":" + user.getUserConfigMap().get(key).getTtl() + "]将被添加");
						}
					} else {
						logger.error("找不到代码为[" + key + "]的数据定义");
					}


				}
			}
			//logger.debug("删除了[" + deleteCount + "]个用户配置数据，新增了[" + insertCount + "]个用户配置数据，更新了[" + updateCount  + "]个用户配置数据");

		}


		DataDefineCriteria userDataDefinePolicyCriteria = new DataDefineCriteria();
		userDataDefinePolicyCriteria.setObjectType(ObjectType.user.name());
		userDataDefinePolicyCriteria.setObjectId(user.getUserTypeId());
		userDataDefinePolicyCriteria.setObjectExtraId(user.getUserExtraTypeId());
		List<DataDefine> userConfigDataDefinePolicyList = dataDefineService.list(userDataDefinePolicyCriteria);
		for(UserData uc : opereateConfigList){
			boolean exist = false;
			for(DataDefine userDataDefinePolicy : userConfigDataDefinePolicyList){
				if(uc.getDataCode().equals(userDataDefinePolicy.getDataCode()) ){
					exist = true;
					uc.setCurrentStatus(userDataDefinePolicy.getCurrentStatus());
					uc.setDataDefineId(userDataDefinePolicy.getDataDefineId());
				}
			}
			if(exist){
				if(logger.isDebugEnabled()){
					logger.debug("操作数据[" + uc.getDataDefineId() + "/" + uc.getDataCode() + "/" + uc.getCurrentStatus() + ",操作码:" + uc.getFlag());
				}
				if(uc.getFlag() == Operate.create.getId()){
					uc.setFlag(0);
					if(uc.getDataValue() != null){
						uc.setUserDataId(0);
						int rs = insert(uc);
						if(rs != 1){
							logger.error("添加数据[" + uc.getUserDataId() + "]失败:" + rs);
						}
						else{
							if(logger.isDebugEnabled()){
								logger.debug("添加数据[" + uc.getUserDataId() + "]成功:" + rs);
							}
						}
					}
				}
				if(uc.getFlag() == Operate.update.getId()){
					int rs =update(uc);
					if(rs != 1){
						logger.error("更新数据[" + uc.getUserDataId() + "]失败:" + rs);
					}
					else
					{
						if(logger.isDebugEnabled()){
							logger.debug("更新数据[" + uc.getUserDataId() + "]成功:" + rs);
						}
					}
				}
				if(uc.getFlag() == Operate.delete.getId()){
					userDataCriteria = new UserDataCriteria();
					userDataCriteria.setDataCode(uc.getDataCode());
					userDataCriteria.setDataDefineId(uc.getDataDefineId());
					userDataCriteria.setUuid(uc.getUuid());	
					delete(userDataCriteria);
					if(logger.isDebugEnabled()){
						logger.debug("!!!!!!!!!!!!!!!!!!!删除数据!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					}
				}

			} else {
				if(logger.isDebugEnabled()){
					logger.debug("强行删除数据[" + uc.getDataDefineId() + "/" + uc.getDataCode() + "/" + uc.getCurrentStatus() + ",操作码:" + uc.getFlag());
				}
				uc.setCurrentStatus(BasicStatus.disable.getId());
				update(uc);
			}
		}
	}

	@Override
	@IgnoreJmsDataSync
	public int delete(UserDataCriteria userDataCriteria) {
		Assert.notNull(userDataCriteria);
		if(userDataCriteria.getUuid() < 1){
			throw new RequiredObjectIsNullException("查询条件中没有uuid");			
		}
		List<String> pkList = null;
		try {
			pkList = userDataDao.listPk(userDataCriteria);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(pkList == null || pkList.size() < 1){
			return 0;
		}
		int deleted = 0;
		UserDataCriteria userDataCriteria2 = new UserDataCriteria();
		for(String pk : pkList){
			String[] data = StringUtils.split(pk, '#');
			if(data == null || data.length != 2){
				continue;
			}
			if(StringUtils.isNumeric(data[0]) && StringUtils.isNumeric(data[1])){
				userDataCriteria2.setUuid(Long.parseLong(data[0]));
				userDataCriteria2.setUserDataId(Integer.parseInt(data[1]));
				try {
					if(userDataDao.delete(userDataCriteria2) == 1){
						deleted++;
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		if(logger.isDebugEnabled()){
			logger.debug("根据条件[" + userDataCriteria + "]需要删除" + pkList.size() + "条数据，实际删除" + deleted + "条");
		}
		return deleted;

	}

	@Override
	public HashMap<String, UserData> generateStandardMap(UserDataCriteria userDataCriteria){
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.user.toString());
		dataDefineCriteria.setObjectId(userDataCriteria.getUserTypeId());
		dataDefineCriteria.setObjectExtraId(userDataCriteria.getUserExtraTypeId());

		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList == null || dataDefineList.size() < 1){
			return null;
		}
		HashMap<String, UserData> map = new  HashMap<String, UserData>();
		for(DataDefine dataDefine : dataDefineList){
			UserData userData = new UserData(dataDefine);
			if(userData != null){
				map.put(userData.getDataCode(), userData);
			}
		}		
		return map;
	}

}
