package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.dao.DataDefineDao;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.standard.DataName;

@Service
public class DataDefineServiceImpl extends BaseService implements DataDefineService {

	@Resource
	private DataDefineDao dataDefineDao;

	/**
	 * 如果这些基本数据定义没有将自动创建
	 */
	private static final String[] BASIC_DATA_DEFINE = new String[]{DataName.clientIp.toString()};



	public int insert(DataDefine dataDefine) {
		try{
			return dataDefineDao.insert(dataDefine);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(DataDefine dataDefine) {
		try{
			return  dataDefineDao.update(dataDefine);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	public int delete(int dataDefineId) {
		try{
			return  dataDefineDao.delete(dataDefineId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	public DataDefine select(int dataDefineId) {
		return dataDefineDao.select(dataDefineId);
	}

	public List<DataDefine> list(DataDefineCriteria dataDefineCriteria) {
		List<Integer> idList = dataDefineDao.listPk(dataDefineCriteria);
		if(idList != null && idList.size() > 0){
			logger.debug("根据条件得到的主键是:" + idList.size());
			List<DataDefine> dataDefineList =  new ArrayList<DataDefine> ();		
			for(int id : idList){
				logger.debug("尝试获取数据定义:" + id);
				DataDefine dataDefine = dataDefineDao.select(id);
				logger.debug("尝试获取数据定义:" + id + "结果:" + dataDefine);
			if(dataDefine != null){
					dataDefineList.add(dataDefine);
				}
			}
			//idList = null;
			return dataDefineList;
		}
		return null;
		/*List<DataDefine> dataDefineList =  dataDefineDao.list(dataDefineCriteria);
		if(dataDefineList != null){
			for(int i = 0; i < dataDefineList.size(); i++ ){
				dataDefineList.get(i).setId(dataDefineList.get(i).getDataDefineId());
				dataDefineList.get(i).setIndex(i+1);
			}
		}
		return dataDefineList;
		 */
	}

	public List<DataDefine> listOnPage(DataDefineCriteria dataDefineCriteria) {
		List<Integer> idList = dataDefineDao.listPkOnPage(dataDefineCriteria);
		if(idList != null && idList.size() > 0){
			List<DataDefine> dataDefineList =  new ArrayList<DataDefine> ();		
			for(Integer id : idList){
				DataDefine dataDefine = dataDefineDao.select(id);
				if(dataDefine != null){
					dataDefineList.add(dataDefine);
				}
			}
			idList = null;
			return dataDefineList;
		}
		return null;
		/*
		List<DataDefine> dataDefineList =  dataDefineDao.listOnPage(dataDefineCriteria);
		if(dataDefineList != null){
			for(int i = 0; i < dataDefineList.size(); i++ ){
				dataDefineList.get(i).setId(dataDefineList.get(i).getDataDefineId());
				dataDefineList.get(i).setIndex(i+1);
			}
		}
		return dataDefineList;
		 */
	}

	@Override
	public DataDefine select(DataDefineCriteria dataDefineCriteria) {
		List<Integer> idList = dataDefineDao.listPk(dataDefineCriteria);
		if(idList != null) {
			if(idList.size() == 1){
				return dataDefineDao.select(idList.get(0));
			} else if(idList.size() > 0 && dataDefineCriteria.getObjectId() > 0) {
				List<DataDefine> dataDefineList = this.list(dataDefineCriteria);
				for(DataDefine  dd : dataDefineList) {
					if(dd.getObjectId() == dataDefineCriteria.getObjectId()) {
						return dd;
					}
				}
			}
		} 
		if(dataDefineCriteria.getDataCode() != null){
			boolean isBasic = false;
			for(String bd :  BASIC_DATA_DEFINE){
				if(bd.equals(dataDefineCriteria.getDataCode())){
					isBasic = true;
					break;
				}
			}
			if(isBasic){
				DataDefine dd = new DataDefine();
				dd.setDataCode(dataDefineCriteria.getDataCode());
				dd.setObjectType(dataDefineCriteria.getObjectType());
				dd.setObjectId(dataDefineCriteria.getObjectId());
				int rs = this.insert(dd);
				logger.debug("自动创建新的基本数据定义:" + dd + ",创建结果:" + rs);
				return dd;
				
			}
		}
		return null;
	}

	@Override
	public int count(DataDefineCriteria dataDefineCriteria) {
		return dataDefineDao.count(dataDefineCriteria);
	}

	@Override
	public DataDefine select(String dataCode) {
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setDataCode(dataCode);
		List<Integer> idList = dataDefineDao.listPk(dataDefineCriteria);
		if(idList != null && idList.size() == 1){
			return dataDefineDao.select(idList.get(0));
		}
		return null;
	}

	@Override
	public void delete(DataDefineCriteria dataDefineCriteria) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, DataDefine> map(DataDefineCriteria dataDefineCriteria) {
		List<DataDefine> dataDefineList = dataDefineDao.list(dataDefineCriteria);
		if(dataDefineList != null && dataDefineList.size() > 0)	{
			HashMap<String, DataDefine> dataDefineMap = new HashMap<String, DataDefine>();
			for(DataDefine dataDefine : dataDefineList){
				dataDefineMap.put(dataDefine.getDataCode(), dataDefine);
			}
			return dataDefineMap;
		}
		return null;
	}

}
