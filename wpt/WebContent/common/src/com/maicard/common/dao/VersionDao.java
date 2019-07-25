package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.maicard.common.criteria.VersionCriteria;
import com.maicard.common.domain.Version;

public interface VersionDao {

	int insert(Version version) throws DataAccessException;

	int update(Version version) throws DataAccessException;

	int delete(int versionId) throws DataAccessException;

	Version select(int versionId) throws DataAccessException;

	List<Version> list(VersionCriteria versionCriteria) throws DataAccessException;
	
	List<Version> listOnPage(VersionCriteria versionCriteria) throws DataAccessException;
	
	int count(VersionCriteria versionCriteria) throws DataAccessException;
	List<Integer> listPk(VersionCriteria versionCriteria);
	List<Integer> listPkOnPage(VersionCriteria versionCriteria);

}
