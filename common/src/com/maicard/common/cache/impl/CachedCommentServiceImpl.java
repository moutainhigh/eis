package com.maicard.common.cache.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedCommentService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.dao.CommentDao;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.service.DataDefineService;
import com.maicard.standard.ObjectType;

import static com.maicard.common.criteria.CommentCriteria.CACHE_NAME;

import java.util.List;

import javax.annotation.Resource;

@Service
public class CachedCommentServiceImpl extends BaseService implements CachedCommentService{

	@Resource
	private CommentDao commentDao;
	@Resource
	private DataDefineService dataDefineService;
	
	
	@Override
	@Cacheable(value = CACHE_NAME, key = "'Comment#' + #commentId")
	public Comment select(long commentId) {
		Comment comment = commentDao.select(commentId);
		if(comment == null){
			return null;
		}
		afterFetch(comment);
		return comment;
	}
	
	protected void afterFetch(Comment comment){
		
		
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria(ObjectType.comment.name(), 0, comment.getOwnerId());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList == null || dataDefineList.size() < 1){
			logger.info("ownerId=" + comment.getOwnerId() + "未定义针对Comment的扩展数据");
			return;
		}
		
		
		
		
	}

}
