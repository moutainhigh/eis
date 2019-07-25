package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.ConfigCriteria;
import com.maicard.common.domain.Config;

public interface ConfigDao {

	int insert(Config config) throws DataAccessException;

	int update(Config config) throws DataAccessException;

	int delete(int configId) throws DataAccessException;

	Config select(int configId) throws DataAccessException;

	List<Config> list(ConfigCriteria configCriteria) throws DataAccessException;
	
	List<Config> listOnPage(ConfigCriteria configCriteria) throws DataAccessException;
	
	int count(ConfigCriteria configCriteria) throws DataAccessException;

	Config selectByName(ConfigCriteria configCriteria);

}
