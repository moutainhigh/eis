package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;

public interface DataDefineDao {

	int insert(DataDefine dataDefine) throws DataAccessException;

	int update(DataDefine dataDefine) throws DataAccessException;

	int delete(int dataDefineId) throws DataAccessException;

	DataDefine select(int dataDefineId) throws DataAccessException;

	List<DataDefine> list(DataDefineCriteria dataDefineCriteria) throws DataAccessException;
	
	List<DataDefine> listOnPage(DataDefineCriteria dataDefineCriteria) throws DataAccessException;
	
	int count(DataDefineCriteria dataDefineCriteria) throws DataAccessException;

	List<Integer> listPk(DataDefineCriteria dataDefineCriteria);

	List<Integer> listPkOnPage(DataDefineCriteria dataDefineCriteria);

}
