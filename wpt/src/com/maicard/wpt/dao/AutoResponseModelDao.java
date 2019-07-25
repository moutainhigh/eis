package com.maicard.wpt.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.wpt.criteria.AutoResponseModelCriteria;
import com.maicard.wpt.domain.AutoResponseModel;


public interface AutoResponseModelDao {
	int insert(AutoResponseModel autoResponseModel) throws DataAccessException;

	int update(AutoResponseModel autoResponseModel) throws DataAccessException;

	int delete(long autoResponseModelId) throws DataAccessException;

	AutoResponseModel select(long autoResponseModelId) throws DataAccessException;
	
	List<AutoResponseModel> list(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException;
	
	List<AutoResponseModel> listOnPage(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException;
	
	int count(AutoResponseModelCriteria autoResponseModelCriteria) throws DataAccessException;
}
