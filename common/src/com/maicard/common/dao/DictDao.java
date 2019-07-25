package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.DictCriteria;
import com.maicard.common.domain.Dict;

public interface DictDao {

	int insert(Dict dict) throws DataAccessException;

	int update(Dict dict) throws DataAccessException;

	int delete(int dictId) throws DataAccessException;

	Dict select(int dictId) throws DataAccessException;

	List<Dict> list(DictCriteria dictCriteria) throws DataAccessException;
	
	List<Dict> listOnPage(DictCriteria dictCriteria) throws DataAccessException;
	
	int count(DictCriteria dictCriteria) throws DataAccessException;

	List<Integer> listPk(DictCriteria dictCriteria);

	List<Integer> listPkOnPage(DictCriteria dictCriteria);

}
