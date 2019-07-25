package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedCommentConfigService;
import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.dao.CommentConfigDao;
import com.maicard.common.domain.CommentConfig;
import com.maicard.common.service.CommentConfigService;


@Service
public class CommentConfigServiceImpl extends BaseService implements CommentConfigService {

	@Resource
	private CommentConfigDao commentConfigDao;
	@Resource
	private CachedCommentConfigService cachedCommentConfigService;
	

	



	public int insert(CommentConfig commentConfig) {

		
		int rs = 0;
		try{
			rs = commentConfigDao.insert(commentConfig);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("新增评论失败,数据操作未返回1");
			return -1;
		}

		return 1;
	}

	public int update(CommentConfig commentConfig) {
		int actualRowsAffected = 0;

		long commentConfigId = commentConfig.getCommentConfigId();

		CommentConfig _oldCommentConfig = commentConfigDao.select(commentConfigId);

		if (_oldCommentConfig == null) {
			return 0;
		}
		try{
			actualRowsAffected = commentConfigDao.update(commentConfig);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());

		}
		return actualRowsAffected;
	}

	public int delete(int commentConfigId) {
		int actualRowsAffected = 0;

		CommentConfig _oldCommentConfig = commentConfigDao.select(commentConfigId);

		if (_oldCommentConfig != null) {
			actualRowsAffected = commentConfigDao.delete(commentConfigId);
		}
		return actualRowsAffected;
	}


	public CommentConfig select(int commentConfigId){
		return cachedCommentConfigService.select(commentConfigId);
	}

	public List<CommentConfig> list(CommentConfigCriteria commentConfigCriteria) {
		List<Long> idList = commentConfigDao.listPk(commentConfigCriteria);
		if(idList != null && idList.size() > 0){
			List<CommentConfig> commentConfigList =  new ArrayList<CommentConfig> ();		
			for(Long id : idList){
				CommentConfig commentConfig = cachedCommentConfigService.select(id);
				if(commentConfig != null){
					commentConfigList.add(commentConfig);
				}
			}
			idList = null;
			return commentConfigList;
		}
		return null;
		/*
		List<CommentConfig> commentConfigList = commentConfigDao.list(commentConfigCriteria);
		if(commentConfigList == null){
			return null;
		}
		for(int i = 0; i < commentConfigList.size(); i ++){
			commentConfigList.get(i).setIndex(i+1);		
			afterFetch(commentConfigList.get(i));
		}
		return commentConfigList;
		 */
	}

	public List<CommentConfig> listOnPage(CommentConfigCriteria commentConfigCriteria) {
		List<Long> idList = commentConfigDao.listPkOnPage(commentConfigCriteria);
		if(idList != null && idList.size() > 0){
			List<CommentConfig> commentConfigList =  new ArrayList<CommentConfig> ();		
			for(Long id : idList){
				CommentConfig commentConfig = cachedCommentConfigService.select(id);
				if(commentConfig != null){
					commentConfigList.add(commentConfig);
				}
			}
			idList = null;
			return commentConfigList;
		}
		return null;
		/*
		List<CommentConfig> commentConfigList = commentConfigDao.listOnPage(commentConfigCriteria);
		if(commentConfigList == null){
			return null;
		}
		for(int i = 0; i < commentConfigList.size(); i ++){
			commentConfigList.get(i).setIndex(i+1);
			afterFetch(commentConfigList.get(i));
		}
		return commentConfigList;
		 */
	}

	public int count(CommentConfigCriteria commentConfigCriteria){
		return commentConfigDao.count(commentConfigCriteria);
	}

	

}
