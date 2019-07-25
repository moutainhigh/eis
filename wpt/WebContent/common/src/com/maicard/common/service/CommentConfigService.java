package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.domain.CommentConfig;

public interface CommentConfigService {

	int insert(CommentConfig commentConfig);

	int update(CommentConfig commentConfig);

	int delete(int commentConfigId);
	
	CommentConfig select(int commentConfigId);
	
	List<CommentConfig> list(CommentConfigCriteria commentConfigCriteria);

	List<CommentConfig> listOnPage(CommentConfigCriteria commentConfigCriteria);
	
	int count(CommentConfigCriteria commentConfigCriteria);



}
