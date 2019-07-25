package com.maicard.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.ExtraDataCriteria;
import com.maicard.common.dao.ExtraDataDao;
import com.maicard.common.domain.EisObject;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.service.ExtraDataService;

@Service
public class ExtraDataServiceImpl extends BaseService implements ExtraDataService {

	@Resource
	private ExtraDataDao extraDataDao;



	public int insert(ExtraData extraData) {
		try{
			return extraDataDao.insert(extraData);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(ExtraData extraData) {
		try{
			return  extraDataDao.update(extraData);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
			}

	public int delete(long extraDataId) {
		try{
			return  extraDataDao.delete(extraDataId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}
	
	public ExtraData select(long extraDataId, String tableName) {
		ExtraDataCriteria extraDataCriteria = new ExtraDataCriteria();
		extraDataCriteria.setExtraDataId(extraDataId);
		extraDataCriteria.setTableName(tableName);
		ExtraData extraData =  extraDataDao.select(extraDataCriteria);
		if(extraData == null){
			extraData = new ExtraData();
		}
		return extraData;
	}
	
	


	public List<ExtraData> list(ExtraDataCriteria extraDataCriteria) {
		return extraDataDao.list(extraDataCriteria);
	}
	
	
	public int count(ExtraDataCriteria extraDataCriteria) {
		return extraDataDao.count(extraDataCriteria);
	}
	
	@Override
	public int sync(EisObject object){
		return extraDataDao.sync(object);
	}

	@Override
	public List<ExtraData> listOnPage(ExtraDataCriteria extraDataCriteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/*
	 * 根据一个类的名称，返回应该访问哪个数据库
	 * 把类名的驼峰式转换为下划线小写
	 * 例如role则返回role_data
	 * cardModel则返回card_model_data
	 */
	@SuppressWarnings("unused")
	private String getTableNameByModelName(String src) {
		/*if(src.equals("Role")){
			return "role_data";
		}*/
		src = StringUtils.uncapitalize(src);
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
	public int deleteByObjectId(ExtraDataCriteria extraDataCriteria) {
		return extraDataDao.deleteByObjectId(extraDataCriteria);
	}

	
}
