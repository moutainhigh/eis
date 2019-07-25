package com.maicard.common.dao.ibatis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.criteria.ExtraDataCriteria;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.base.BaseDao;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisObject;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.ClassUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.dao.ExtraDataDao;
import com.maicard.standard.Operate;

@Repository
public class ExtraDataDaoImpl extends BaseDao implements ExtraDataDao {

	@Resource
	private DataDefineService dataDefineService;

	@Override
	public int insert(ExtraData extraData) throws DataAccessException {
		logger.debug("新增扩展数据[" + extraData + "]到表:" + extraData.getTableName());
		return getSqlSessionTemplate().insert("com.maicard.common.sql.ExtraData.insert", extraData);
	}

	@Override
	public int update(ExtraData extraData) throws DataAccessException {
		logger.debug("更新扩展数据[" + extraData + "]到表:" + extraData.getTableName());
		return getSqlSessionTemplate().update("com.maicard.common.sql.ExtraData.update", extraData);
	}

	@Override
	public int delete(long extraDataId) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.common.sql.ExtraData.delete", extraDataId);
	}

	@Override
	public ExtraData select(ExtraDataCriteria extraDataCriteria) throws DataAccessException {
		return getSqlSessionTemplate().selectOne("com.maicard.common.sql.ExtraData.select", extraDataCriteria);
	}

	@Override
	public List<ExtraData> list(ExtraDataCriteria extraDataCriteria) throws DataAccessException {
		Assert.notNull(extraDataCriteria, "extraDataCriteria must not be null");
		if(extraDataCriteria.getTableName() == null){
			extraDataCriteria.setTableName( getTableNameByModelName(extraDataCriteria.getObjectType()));
		}
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.ExtraData.list",extraDataCriteria);
	}

	@Override
	public List<Long> listPk(ExtraDataCriteria extraDataCriteria) throws DataAccessException {
		Assert.notNull(extraDataCriteria, "extraDataCriteria must not be null");
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.ExtraData.listPK",extraDataCriteria);
	}

	@Override
	public List<Long> listPkOnPage(ExtraDataCriteria extraDataCriteria) throws DataAccessException {
		Assert.notNull(extraDataCriteria, "extraDataCriteria must not be null");
		Assert.notNull(extraDataCriteria.getPaging(), "paging must not be null");

		int totalResults = count(extraDataCriteria);
		Paging paging = extraDataCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.common.sql.ExtraData.listPK", extraDataCriteria, rowBounds);
	}

	@Override
	public int count(ExtraDataCriteria extraDataCriteria) throws DataAccessException {
		Assert.notNull(extraDataCriteria, "extraDataCriteria must not be null");
		return ((Integer)getSqlSessionTemplate().selectOne("com.maicard.common.sql.ExtraData.count", extraDataCriteria)).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int sync(EisObject eisObject){
		String objectType = ClassUtils.getObjectType(eisObject);
		long objectId = ClassUtils.getObjectId(eisObject);
		String tableName = getTableNameByModelName(objectType);
		logger.debug("当前的对象[" + objectType + "/" + objectId + "]扩展数据将存储到:" + tableName);
		ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();
		extraDataCriteria.setObjectId(objectId);
		extraDataCriteria.setObjectType(objectType);
		extraDataCriteria.setTableName(tableName);
		List<ExtraData> oldUserConfigList = list(extraDataCriteria);
		List<ExtraData> opereateConfigList = new ArrayList<ExtraData>();

		Method getMethod = null;
		try {
			getMethod = eisObject.getClass().getMethod("getData", (Class<?>[])null);
		} catch (NoSuchMethodException | SecurityException e2) {
			e2.printStackTrace();
		}
		if(getMethod == null){
			logger.error("对象[" + eisObject.getClass().getName() + "没有getData()方法");
			return -1;
		}

		Object result =	null;
		try {
			result = getMethod.invoke(eisObject, new Object[]{});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		if(!(result instanceof HashMap)){
			logger.error("对象[" + eisObject.getClass().getName() + "的getData返回数据不是HashMap");
			return -1;
		}
		HashMap<String,ExtraData> extraDataMap = null;
		try{
			extraDataMap = (HashMap<String,ExtraData>)result;
		}catch(Exception e){
			
		}

		if(extraDataMap == null || extraDataMap.size() < 1){
			/* 有旧的数据但没有新的数据
			 * 如果是系统更新，则删除所有旧数据
			 * 否则删除所有非只读的旧数据
			 */
			logger.debug("对象[" + objectType + "/" + objectId + "]传入的内存数据为空，删除之前可能存在的数据库数据");

			extraDataCriteria = new ExtraDataCriteria();
			extraDataCriteria.setObjectId(objectId);
			extraDataCriteria.setObjectType(objectType);
			extraDataCriteria.setTableName(tableName);
			int deleted = deleteByObjectId(extraDataCriteria);			
			logger.warn("对象[" + objectType + "/" + objectId + "]之前的所有扩展数据已被删除:" + deleted);
		} else {
			//先循环所有新数据，比对旧数据，如果
			/* 先比较旧数据跟新数据
			 * 如果旧数据有而新数据没有，则应当是删除
			 */
			logger.debug("对象[" + objectType + "/" + objectId + "]有数据需要更新，传入的内存数据共" + extraDataMap.size() + "条数据");			

			int deleteCount = 0;
			if(oldUserConfigList != null){
				for(ExtraData uc : oldUserConfigList){
					if(uc.getExtraDataId() < 1){
						logger.debug("不比较动态添加的扩展数据:" + uc);
						continue;
					}
					boolean delete = true;
					for(String key: extraDataMap.keySet()){
						logger.debug("比对当前对象[" + objectType + "/" + objectId + "]的数据配置:" + key + ",dataDefineId=" + extraDataMap.get(key).getDataDefineId() + ", dataCode=" + extraDataMap.get(key).getDataCode() + ",dataValue=" + extraDataMap.get(key).getDataValue() + "与旧数据:" + ",dataDefineId=" + uc.getDataDefineId() + ", dataCode=" + uc.getDataCode() + ",dataValue=" + uc.getDataValue());

						if(key != null && key.equals(uc.getDataCode())){
							logger.debug("对象[" + objectType + "/" + objectId + "]的数据配置[" +  key + "" + uc.getDataDefineId() + "]相同,不删除");
							delete = false;
							break;
						}
					}
					if(delete){
						if(uc.getExtraDataId() < 1){
							logger.debug("不尝试删除动态添加的对象[" + objectType + "/" + objectId + "]的数据:" + uc);
						} else {
							ExtraDataCriteria deleteUserConfigCriteria = new ExtraDataCriteria();
							deleteUserConfigCriteria.setObjectId(objectId);
							deleteUserConfigCriteria.setObjectType(objectType);
							deleteUserConfigCriteria.setTableName(tableName);
							logger.debug("对象[" + objectType + "/" + objectId + "]数据[" + uc + "]已被删除");
							deleteByObjectId(deleteUserConfigCriteria);
							deleteCount++;

						}
					}
				}
			}
			if(deleteCount > 0){//重新加载旧数据
				logger.debug("对象[" + objectType + "/" + objectId + "]的部分旧扩展数据已删除，重新加载旧数据");
				oldUserConfigList = list(extraDataCriteria);
			}
			/* 再比较新数据与旧数据
			 * 如果是系统更新，则更新所有旧数据
			 * 否则只更新所有非只读的旧数据
			 */
			//List<UserConfig> newUserConfigList = new ArrayList<UserConfig>();
			logger.debug("对象[" + objectType + "/" + objectId + "]内存配置中有" + (extraDataMap == null ? "空" : extraDataMap.size()) + "条数据，数据库配置有[" + (oldUserConfigList == null ? "空" : oldUserConfigList.size()) + "]条");


			for(String key: extraDataMap.keySet()){
				boolean update = false;
				//logger.info("-------------------------------------------");
				if(oldUserConfigList != null){
					for(ExtraData uc : oldUserConfigList){
						//logger.debug("比对" + key + ":::::::::::" + uc.getDataCode() + "/" + uc.getDataValue() + "/" + uc.getFlag());
						if(key.equals(uc.getDataCode())  && uc.getDataDefineId() > 1 && !uc.getDataValue().equals(extraDataMap.get(key))){//存在相同名称且旧内容为空且非动态生成的数据
							/*//logger.info(key + ":::::::::::" + uc.getDataCode() + "/" + uc.getDataValue() + "/" + uc.getFlag() + "标记为update");

							extraDataMap.get(key).setUserConfigId(uc.getUserConfigId());
							extraDataMap.get(key).setDataDefineId(uc.getDataDefineId());
							update = true;
							if(uc.getCurrentStatus() == Constants.basicHidden){
								extraDataMap.get(key).setCurrentStatus(Constants.basicHidden);
								hidden = true;
							}
							if(uc.getCurrentStatus() == Constants.BasicStatus.readOnly.getId()){
								extraDataMap.get(key).setCurrentStatus(Constants.BasicStatus.readOnly.getId());
								readonly = true;
							}
							 */

							logger.debug("对象[" + objectType + "/" + objectId + "]的数据[" + key + "]已存在需要更新,将其dataDefineId从" + extraDataMap.get(key).getDataDefineId() + "设置为" + uc.getDataDefineId());
							update = true;
							extraDataMap.get(key).setDataDefineId(uc.getDataDefineId());							
							extraDataMap.get(key).setExtraDataId(uc.getExtraDataId());							
							break;					
						}


					}
				}
				if(update){
					logger.debug("对象[" + objectType + "/" + objectId + "]当前的数据[" + key + "/" + extraDataMap.get(key) + "]将被更新");
					extraDataMap.get(key).setFlag(Operate.update.getId());

					opereateConfigList.add(extraDataMap.get(key));


				} else { //新增	
					//从数据许可规则中进行过滤，不在许可列表中的数据不允许插入，同时，使用许可列表中定义的状态码代替数据中的状态码
					logger.debug("找不到对象[" + objectType + "/" + objectId + "]的[" + key + "]旧数据");
					DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
					DataDefine dd = null;
					dataDefineCriteria.setDataCode(key);
					dataDefineCriteria.setObjectType(objectType);
					dataDefineCriteria.setObjectId((int)objectId);
					dd = dataDefineService.select(dataDefineCriteria);
					if(dd != null){
						//	logger.info("!!!!!!!!!!!!!!!"+key+"!!!!!!!!!!!,"+extraDataMap.get(key).getDataValue()+"!!!!!!!!!!!!");
						//FIXME
						extraDataMap.get(key).setObjectId((int)objectId);
						extraDataMap.get(key).setDataDefineId(dd.getDataDefineId());
						extraDataMap.get(key).setDataValue(extraDataMap.get(key).getDataValue());
						extraDataMap.get(key).setFlag(Operate.create.getId());
						opereateConfigList.add(extraDataMap.get(key));
						logger.debug("对象[" + objectType + "/" + objectId + "]的数据[" + key + "/" + extraDataMap.get(key) + "]将被添加");

					} else {
						logger.error("找不到代码为[" + key + "]的数据定义");
					}


				}
			}
			//logger.debug("删除了[" + deleteCount + "]个用户配置数据，新增了[" + insertCount + "]个用户配置数据，更新了[" + updateCount  + "]个用户配置数据");

		}


		DataDefineCriteria userDataDefinePolicyCriteria = new DataDefineCriteria();
		userDataDefinePolicyCriteria.setObjectType(objectType);
		//FIXME
		logger.warn("onjectId : " + objectId + "  objectType : " +objectType + " userDataDefinePolicyCriteria : " + userDataDefinePolicyCriteria);
		List<DataDefine> userConfigDataDefinePolicyList = dataDefineService.list(userDataDefinePolicyCriteria);
		logger.warn("userConfigDataDefinePolicyList : "+userConfigDataDefinePolicyList);
		for(ExtraData extraData : opereateConfigList){
			extraData.setTableName(tableName);

			//FIXME
			extraData.setObjectId((int)objectId);
			extraData.setObjectType(objectType);
			logger.warn("extraData : " + extraData);
			boolean exist = false;
			for(DataDefine userDataDefinePolicy : userConfigDataDefinePolicyList){
				if(extraData.getDataCode().equals(userDataDefinePolicy.getDataCode()) ){
					exist = true;
					extraData.setDataDefineId(userDataDefinePolicy.getDataDefineId());
				}
			}
			if(exist){
				logger.debug("为对象[" + objectType + "/" + objectId + "]操作数据[" + extraData + ",操作码:" + extraData.getFlag());

				if(extraData.getFlag() == Operate.create.getId()){
					extraData.setFlag(0);
					if(extraData.getDataValue() != null){
						extraData.setExtraDataId(0);
						int rs = insert(extraData);
						if(rs != 1){
							logger.error("为对象[" + objectType + "/" + objectId + "]添加数据[" + extraData.getExtraDataId() + "]失败:" + rs);
						}
						else{
							logger.debug("为对象[" + objectType + "/" + objectId + "]添加数据[" + extraData.getExtraDataId() + "]成功:" + rs);

						}
					}
				}
				if(extraData.getFlag() == Operate.update.getId()){
					int rs =update(extraData);
					if(rs != 1){
						logger.error("为对象[" + objectType + "/" + objectId + "]更新数据[" + extraData.getExtraDataId() + "]失败:" + rs);
					}
					else
					{
						logger.debug("为对象[" + objectType + "/" + objectId + "]更新数据[" + extraData.getExtraDataId() + "]成功:" + rs);

					}
				}
				if(extraData.getFlag() == Operate.delete.getId()){
					extraDataCriteria = new ExtraDataCriteria();
					extraDataCriteria.setDataCode(extraData.getDataCode());
					extraDataCriteria.setDataDefineId(extraData.getDataDefineId());
					extraDataCriteria.setTableName(tableName);
					deleteByObjectId(extraDataCriteria);

				}

			} else {
				logger.debug("为对象[" + objectType + "/" + objectId + "]强行删除数据[" + extraData + ",操作码:" + extraData.getFlag());
				delete(extraData.getExtraDataId());
			}
		}
		return 0;
	}

	@Override
	public int deleteByObjectId(ExtraDataCriteria extraDataCriteria) {
		logger.debug("按对象ID[objectId=" + extraDataCriteria.getObjectId() + ",objectType=" + extraDataCriteria.getObjectType() + "]删除扩展数据到表:" + extraDataCriteria.getTableName());
		return getSqlSessionTemplate().delete("com.maicard.common.sql.ExtraData.deleteByObjectId", extraDataCriteria);

	}

	private String getTableNameByModelName(String src) {
		/*if(src.equals("Role")){
			return "role_data";
		}*/
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < src.length(); i++){
			if(src.charAt(i) >= 'A' && src.charAt(i) <= 'Z'){
				sb.append('_');
				sb.append((char)(src.charAt(i)+32));
			} else {
				sb.append(src.charAt(i));
			}
		}
		return sb.toString() + "_data";
	}

	@Override
	public Map<String, ExtraData> map(ExtraDataCriteria extraDataCriteria) {
		List<ExtraData> list = list(extraDataCriteria);
		if(list == null || list.size() < 1){
			return null;
		}
		Map<String, ExtraData> map = new HashMap<String, ExtraData>();
		for(ExtraData extraData : list){
			map.put(extraData.getDataCode(), extraData);
		}
		return map;
	}
}
