package com.maicard.security.dao.ibatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.security.criteria.UserDataCriteria;
import com.maicard.security.dao.UserDataDao;
import com.maicard.security.domain.UserData;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.TablePartitionPolicy;

@Repository
public class UserDataDaoImpl extends BaseDao implements UserDataDao {

	private final String 	 defaultTableName = "user_data";
	private final String allViewName = "user_data_all";
	
	private final String cacheName = CommonStandard.cacheNameUser;

	public int insert(UserData userData) throws Exception {
		if(userData == null){
			return 0;
		}
		if(userData.getUuid() < 1){
			throw new RequiredAttributeIsNullException("尝试新增userData，但未指定uuid");
		}
		getTableName(userData);
		int rs = getSqlSessionTemplate().insert("com.maicard.security.sql.UserData.insert", userData);
		if(logger.isDebugEnabled()){
			logger.debug("插入数据[dataDefineId=" + userData.getDataDefineId() + ", dataCode=" + userData.getDataCode() + ",dataValue=" + userData.getDataValue() + ",是否允许多个同名数据存在:" + "]:" + rs);
		}		
		return rs;
	}

	@CacheEvict(value=cacheName, key ="'UserData#' + #userData.uuid + '#' + #userData.userDataId")
	public int update(UserData userData) throws Exception {
		if(userData == null){
			return 0;
		}
		if(userData.getUuid() < 1){
			throw new RequiredAttributeIsNullException("尝试更新userData[dataCode=" + userData.getDataCode() + ",userDataId=" + userData.getUserDataId() + ",dataValue=" + userData.getDataValue() + "]，但未指定uuid");
		}
		if(userData.getUserDataId() < 1){
			throw new RequiredAttributeIsNullException("尝试更新userData[dataCode=" + userData.getDataCode() + ",uuid=" + userData.getUuid() + ",,dataValue=" + userData.getDataValue() + "]，但未指定userDataId");
		}
		getTableName(userData);
		return getSqlSessionTemplate().update("com.maicard.security.sql.UserData.update", userData);


	}


	@Cacheable(value=cacheName, key ="'UserData#' + #userDataCriteria.uuid + '#' + #userDataCriteria.userDataId")
	public UserData select(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		if(userDataCriteria.getUuid() < 1){
			logger.error("查询条件未提供必须的uuid");
			throw new RequiredAttributeIsNullException("查询条件未提供必须的uuid");
			
		}
		getTableName(userDataCriteria);
		logger.debug("根据查询条件查询单条用户数据[" + userDataCriteria + "]");
		return (UserData) getSqlSessionTemplate().selectOne("com.maicard.security.sql.UserData.select", userDataCriteria);
	}


	public List<UserData> list(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		getTableName(userDataCriteria);
		//logger.debug("查找数据表:" + userDataCriteria.getTableName());
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserData.list", userDataCriteria);
	}


	public List<UserData> listOnPage(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		Assert.notNull(userDataCriteria.getPaging(), "paging must not be null");

		int totalResults = count(userDataCriteria);
		Paging paging = userDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		getTableName(userDataCriteria);	
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserData.list", userDataCriteria, rowBounds);
	}

	@Override
	public List<String> listPk(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userDataCriteria must not be null");
		getTableName(userDataCriteria);
		logger.debug("查找数据表:" + userDataCriteria.getTableName());
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserData.listPk", userDataCriteria);
	}


	@Override
	public List<String> listPkOnPage(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		Assert.notNull(userDataCriteria.getPaging(), "paging must not be null");

		int totalResults = count(userDataCriteria);
		Paging paging = userDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		getTableName(userDataCriteria);	
		return getSqlSessionTemplate().selectList("com.maicard.security.sql.UserData.listPk", userDataCriteria, rowBounds);
	}

	public int count(UserDataCriteria userDataCriteria) throws Exception {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		getTableName(userDataCriteria);	
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.security.sql.UserData.count", userDataCriteria)).intValue();
	}


	@Override
	@CacheEvict(value=cacheName, key ="'UserData#' + #userDataCriteria.uuid + '#' + #userDataCriteria.userDataId")
	public int delete(UserDataCriteria userDataCriteria) {
		Assert.notNull(userDataCriteria, "userConfigCriteria不能为空.");
		if(userDataCriteria.getUuid() <= 0){
			throw new IllegalArgumentException("userConfigCriteria的uuid不能为空");
		}
		getTableName(userDataCriteria);
		/*if(userDataCriteria.getUserDataId() <= 0){
			throw new IllegalArgumentException("userConfigCriteria的userDataId不能为空");
		}*/
		return getSqlSessionTemplate().delete("com.maicard.security.sql.UserData.deleteByCriteria", userDataCriteria);
		/*List<Integer> idList = getSqlSessionTemplate().selectList("UserData.listPk", userDataCriteria);
		int rs = 0;
		if(idList == null || idList.size() < 1){
		} else {
			SqlSession session = getSqlSessionTemplate().getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			try{
				for(Integer id : idList){
					session.delete("UserData.delete", id);
					rs++;
				}
			}catch(Exception e){
				logger.error("在批量删除用户数据时失败:" + e.getMessage());
				e.printStackTrace();
			}
			session.commit();
			session.close();
			session = null;
			idList = null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("根据条件将要删除的用户数据有[" + (idList == null ? "-1" : idList.size()) + "]个,实际删除数据有[" + rs + "]个");
		}
		return rs;*/
	}


	private void getTableName(UserData userData){
		if(userData == null){
			throw new RequiredObjectIsNullException("userData为空");
		}
		if(userData.getUuid() == 0){
			throw new RequiredAttributeIsNullException("userData中的uuid为0");
		}
		int offset = 0;
		try{
			offset = Integer.parseInt(TablePartitionPolicy.userData.getName());			
		}catch(Exception e){
			offset = 0;
		}
		String uuidString = String.valueOf(userData.getUuid());
		if(offset > 0){
			userData.setTableName(defaultTableName + "_" + (uuidString.substring(uuidString.length() - offset)));
		} else {
			userData.setTableName(defaultTableName);
		}
	}

	private void getTableName(UserDataCriteria userDataCriteria){
		if(userDataCriteria == null){
			throw new RequiredObjectIsNullException("userData为空");
		}
		if(userDataCriteria.getUuid() == 0){
			userDataCriteria.setTableName(allViewName);
			return;
		}
		int offset = 0;
		try{
			offset = Integer.parseInt(TablePartitionPolicy.userData.getName());			
		}catch(Exception e){
			offset = 0;
		}
		String uuidString = String.valueOf(userDataCriteria.getUuid());
		if(offset > 0){
			userDataCriteria.setTableName(defaultTableName + "_" + (uuidString.substring(uuidString.length() - offset)));
		} else {
			userDataCriteria.setTableName(defaultTableName);
		}

	}

	@Override
	public Map<String, UserData> map(UserDataCriteria userDataCriteria) {
		Assert.notNull(userDataCriteria, "userConfigCriteria must not be null");
		getTableName(userDataCriteria);
		return getSqlSessionTemplate().selectMap("com.maicard.security.sql.UserData.list", userDataCriteria, "dataCode");
	}

}
