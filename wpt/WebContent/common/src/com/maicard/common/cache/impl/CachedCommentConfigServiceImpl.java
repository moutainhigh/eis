package com.maicard.common.cache.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedCommentConfigService;
import com.maicard.common.dao.CommentConfigDao;
import com.maicard.common.domain.CommentConfig;

import static com.maicard.common.criteria.CommentConfigCriteria.CACHE_NAME;

import javax.annotation.Resource;

@Service
public class CachedCommentConfigServiceImpl extends BaseService implements CachedCommentConfigService{

	@Resource
	private CommentConfigDao commentConfigDao;
	
	@Override
	@Cacheable(value = CACHE_NAME, key = "'CommentConfig#' + #commentConfigId")
	public CommentConfig select(long commentConfigId) {
		return commentConfigDao.select(commentConfigId);
	}

}
