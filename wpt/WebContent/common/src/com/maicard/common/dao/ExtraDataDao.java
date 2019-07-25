package com.maicard.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.maicard.common.domain.EisObject;
import com.maicard.common.domain.ExtraData;
import com.maicard.common.criteria.ExtraDataCriteria;

public interface ExtraDataDao {
	int insert(ExtraData extraData) throws DataAccessException;
	
	int update(ExtraData extraData) throws DataAccessException;
	
	int delete(long extraDataId) throws DataAccessException;
	
	ExtraData select(ExtraDataCriteria extraDataCriteria) throws DataAccessException;
	
	List<Long> listPk(ExtraDataCriteria extraDataCriteria) throws DataAccessException;
	
	List<Long> listPkOnPage(ExtraDataCriteria extraDataCriteria) throws DataAccessException;
	
	int count(ExtraDataCriteria extraDataCriteria) throws DataAccessException;

	//int sync(Object object, long objectId, String objectType);

	List<ExtraData> list(ExtraDataCriteria extraDataCriteria) throws DataAccessException;

	int deleteByObjectId(ExtraDataCriteria extraDataCriteria);

	Map<String, ExtraData> map(ExtraDataCriteria extraDataCriteria);

	int sync(EisObject eisObject);

}
