package com.maicard.billing.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;

public interface ShareConfigDao {

	int insert(ShareConfig shareConfig) throws DataAccessException;

	int update(ShareConfig shareConfig) throws DataAccessException;

	int updateNoNull(ShareConfig shareConfig) throws DataAccessException;
	
	int delete(int shareConfigId) throws DataAccessException;

	ShareConfig select(int shareConfigId) throws DataAccessException;
	


	List<ShareConfig> list(ShareConfigCriteria shareConfigCriteria) throws DataAccessException;
	
	List<ShareConfig> listOnPage(ShareConfigCriteria shareConfigCriteria) throws DataAccessException;
	
	int count(ShareConfigCriteria shareConfigCriteria) throws DataAccessException;

	void deleteByUuid(long shareUuid);

}
