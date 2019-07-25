package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.VersionCriteria;
import com.maicard.common.domain.Version;

public interface VersionService {

	int insert(Version version);
	
	int update(Version version);

	int delete(int versionId);
	
	Version select(int versionId);

	List<Version> list(VersionCriteria versionCriteria);

	List<Version> listOnPage(VersionCriteria versionCriteria);
	
	int count(VersionCriteria versionCriteria);

	Version getLastVersion(long partnerId);
	
}
