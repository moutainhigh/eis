package com.maicard.common.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.cache.CachedCommentService;
import com.maicard.common.criteria.CommentConfigCriteria;
import com.maicard.common.criteria.CommentCriteria;
import com.maicard.common.dao.CommentDao;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.CommentConfig;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Uuid;
import com.maicard.common.processor.CommentProcessor;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CommentConfigService;
import com.maicard.common.service.CommentService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DirtyDictService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.UuidService;
import com.maicard.standard.EisError;
import static com.maicard.common.criteria.CommentCriteria.CACHE_NAME;

import com.maicard.standard.MessageStandard.MessageStatus;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;


@Service
public class CommentServiceImpl extends BaseService implements CommentService {

	@Resource
	private CommentDao commentDao;

	@Resource
	private ApplicationContextService applicationContextService;

	@Resource
	private CommentConfigService commentConfigService;
	@Resource
	private CachedCommentService cachedCommentService;

	@Resource
	private ConfigService configService;

	@Resource
	private DirtyDictService dirtyDictService;
	@Resource
	private UuidService uuidService;

	@Resource
	private CenterDataService centerDataService;




	@Override
	public EisMessage insert(Comment comment) {

		Assert.notNull(comment, "尝试新增的评论不能为空");
		Assert.notNull(comment.getObjectType(), "尝试新增的评论对象不能为空");
		Assert.isTrue(comment.getObjectId() > 0, "尝试新增的评论对象ID不能为0");


		String dirtyWord = dirtyDictService.check(comment.getTitle());
		if(dirtyWord != null){
			logger.warn("评论标题[" + comment.getTitle() + "]包含敏感词:{}", dirtyWord);
			return new EisMessage(EisError.haveDirtyWord.id,"评论标题[" + comment.getTitle() + "]包含敏感词:" + dirtyWord);
		}
		dirtyWord = dirtyDictService.check(comment.getContent());

		if(dirtyWord != null){
			logger.warn("评论内容[" + comment.getContent() + "]包含敏感词:{}", dirtyWord);
			return new EisMessage(EisError.haveDirtyWord.id,"评论内容[" + comment.getContent() + "]包含敏感词:" + dirtyWord);
		}
		

		if(comment.getCommentId() > 0){
			//是来自数据同步的请求，仅做插入，不做其他判断和处理
			int rs = 0;
			try{
				rs = commentDao.insert(comment);
			}catch(Exception e){
				logger.error("插入数据失败:" + e.getMessage());
			}
			if(rs != 1){
				logger.error("新增评论失败,数据操作未返回1");
				return new EisMessage(OperateResult.failed.id);
			}
			return new EisMessage(rs);
			
		}
		CommentConfig commentConfig = getCommentConfig(comment);
		if(commentConfig == null){
			logger.warn("找不到针对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论配置，将不允许发布评论");
			return new EisMessage(EisError.systemDataError.id);
		}
		comment.applyCommentConfig(commentConfig);

		if(commentConfig.isUnique()){

			CommentCriteria commentCriteria = new CommentCriteria(comment.getOwnerId());
			commentCriteria.setUuid(comment.getUuid());
			commentCriteria.setObjectType(comment.getObjectType());
			commentCriteria.setObjectId(comment.getObjectId());
			int existCount = commentDao.count(commentCriteria);
			if(existCount > 0){
				logger.error("用户[" + comment.getUuid() + "]已经存在对对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论" + existCount + "条，并且当前配置每个用户不允许有多条评论");
				return new EisMessage(EisError.dataDuplicate.id);
			}
			if(!centerDataService.lock(comment.getLockKey())){
				logger.error("无法新增评论[" + comment + "]，因为无法加锁");
				return new EisMessage(EisError.distributedLockFail.id);
			}
		}
		CommentProcessor commentProcessor = null;
		if(commentConfig.getSyncFlag() == 0 && commentConfig.getCommentProcessor() != null){
			Object bean = applicationContextService.getBean(commentConfig.getCommentProcessor());
			if(bean == null){
				logger.error("无法新增评论[" + comment + "]，因为找不到指定的评论处理器:" + commentConfig.getCommentProcessor());
				centerDataService.delete(comment.getLockKey());
				return new EisMessage(EisError.beanNotFound.id);
			}
			if(!(bean instanceof CommentProcessor)){
				logger.error("无法新增评论[" + comment + "]，因为评论处理器类型不匹配:" + bean.getClass().getName());
				return new EisMessage(EisError.beanNotFound.id);
			}
			commentProcessor = (CommentProcessor)bean;
			EisMessage message = commentProcessor.execute(Operate.create.getCode(), comment, null);
			logger.debug("自定义评论处理器返回结果是:" + message );
			if(message == null){
				return new EisMessage(EisError.systemDataError.id);
			}
			if(message.getOperateCode() == OperateResult.success.getId()){
				//return 1;
			} else {
				return message;
			}
		}
		/*if(commentConfig.isSubscribeOnly()){
			if(commentConfig.getSubscribeCheckBean() == null){
				logger.error("评论配置为订阅用户才能发表，但订阅检查bean配置为空");
				return -EisError.requiredDataNotFound.id;
			}
			if(commentConfig.getSubscribeCheckMethod() == null){
				logger.error("评论配置为订阅用户才能发表，但订阅检查bean的检查方法配置为空");
				return -EisError.requiredDataNotFound.id;
			}
			if(commentConfig.getSubscribeCheckMethod() == null){
				logger.error("评论配置为订阅用户才能发表，但订阅检查bean的检查方法配置为空");
				return -EisError.requiredDataNotFound.id;
			}
			Object parameter = null;
			String parameterJson = null;
			if(commentConfig.getSubscribeCheckParameterClassName() != null && commentConfig.getSubscribeCheckParameter() != null){
				parameterJson = commentConfig.getSubscribeCheckParameter().trim().replaceAll("\\$\\{objectId\\}", String.valueOf(comment.getObjectId())).replaceAll("\\$\\{uuid\\}", String.valueOf(comment.getUuid()));
				logger.debug("检查订阅的参数JSON:"+ parameterJson);
				try{
					parameter = JsonUtils.getInstance().readValue(parameterJson , Class.forName(commentConfig.getSubscribeCheckParameterClassName()));
				}catch(Exception e){
					e.printStackTrace();
					return -EisError.dataError.id;
				}
			}
			logger.debug("检查订阅的参数类型是:" + parameter.getClass().getName());
			Object bean = applicationContextService.getBean(commentConfig.getSubscribeCheckBean());
			if(bean == null){
				logger.error("找不到验证bean:" + commentConfig.getSubscribeCheckBean());
				return -EisError.beanNotFound.id;
			}
			Method[] methods = bean.getClass().getMethods();
			if(methods == null || methods.length < 1){
				logger.error("bean[" + bean.getClass().getName() + "]没有任何方法可执行");
				return -EisError.objectIsNull.id;
			}
			for(Method method : methods){
				if(method.getName().equals(commentConfig.getSubscribeCheckMethod())){
					// JDK 1.8才能用	&& method.getParameters() != null && method.getParameters().length == 1){
					logger.debug("准备执行订阅检查[" + bean.getClass().getName() + "." + method.getName() + ",参数JSON:" + parameterJson + ",类型:" + parameter.getClass().getName());
					try {
						Object result = method.invoke(bean, parameter);
						int count = (int)result;
						logger.debug("订阅检查[" + bean.getClass().getName() + "." + method.getName() + "]的结果是:" + count);
						if(count < 1){
							return -EisError.subscribeCountError.id;
						} else {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return -EisError.countLimitExceed.id;
					}
				}
			}


		}*/
		if(comment.getCommentId() < 1){
			long uuid = uuidService.insert(new Uuid());
			if(uuid < 1){
				logger.error("无法生成本地UUID");
				return new EisMessage(EisError.uuidCreateFail.id);
			}
			long commentId = Long.parseLong(configService.getServerId() + "" + uuid);
			comment.setCommentId(commentId);
		}
		if(comment.getCurrentStatus() == MessageStatus.sent.id){
			comment.setPublishTime(comment.getCreateTime());
		}
		int rs = 0;
		try{
			rs = commentDao.insert(comment);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("新增评论失败,数据操作未返回1");
			return new EisMessage(EisError.DATA_UPDATE_FAIL.id);
		}
		if(comment.getSyncFlag() == 0 && commentProcessor != null){
			commentProcessor.execute(Operate.close.getCode(), comment, null);
		}
		return new EisMessage(OperateResult.success.id);
	}

	@CacheEvict(value = CACHE_NAME, key = "'Comment#' + #comment.commentId")
	public int update(Comment comment) {
		int actualRowsAffected = 0;

		long commentId = comment.getCommentId();

		Comment _oldComment = commentDao.select(commentId);

		if (_oldComment == null) {
			return 0;
		}
		try{
			actualRowsAffected = commentDao.update(comment);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());

		}
		return actualRowsAffected;
	}

	public int delete(long commentId) {
		int actualRowsAffected = 0;

		Comment _oldComment = commentDao.select(commentId);

		if (_oldComment != null) {
			actualRowsAffected = commentDao.delete(commentId);
		}
		return actualRowsAffected;
	}


	public Comment select(long commentId){
		return cachedCommentService.select(commentId);
	}

	public List<Comment> list(CommentCriteria commentCriteria) {
		List<Long> idList = commentDao.listPk(commentCriteria);
		if(idList != null && idList.size() > 0){
			List<Comment> commentList =  new ArrayList<Comment> ();		
			for(Long id : idList){
				Comment comment = cachedCommentService.select(id);
				if(comment != null){
					commentList.add(comment);
				}
			}
			idList = null;
			return commentList;
		}
		return null;
		/*
		List<Comment> commentList = commentDao.list(commentCriteria);
		if(commentList == null){
			return null;
		}
		for(int i = 0; i < commentList.size(); i ++){
			commentList.get(i).setIndex(i+1);		
			afterFetch(commentList.get(i));
		}
		return commentList;
		 */
	}

	public List<Comment> listOnPage(CommentCriteria commentCriteria) {
		List<Long> idList = commentDao.listPkOnPage(commentCriteria);
		if(idList != null && idList.size() > 0){
			List<Comment> commentList =  new ArrayList<Comment> ();		
			for(Long id : idList){
				Comment comment = cachedCommentService.select(id);
				if(comment != null){
					commentList.add(comment);
				}
			}
			idList = null;
			return commentList;
		}
		return null;
		/*
		List<Comment> commentList = commentDao.listOnPage(commentCriteria);
		if(commentList == null){
			return null;
		}
		for(int i = 0; i < commentList.size(); i ++){
			commentList.get(i).setIndex(i+1);
			afterFetch(commentList.get(i));
		}
		return commentList;
		 */
	}

	public int count(CommentCriteria commentCriteria){
		return commentDao.count(commentCriteria);
	}


	@Override
	public CommentConfig getCommentConfig(Comment comment){
		CommentConfigCriteria commentConfigCriteria = new CommentConfigCriteria(comment.getOwnerId());
		commentConfigCriteria.setObjectType(comment.getObjectType());
		commentConfigCriteria.setObjectId(comment.getObjectId());
		commentConfigCriteria.setWithGlobalConfig(true);
		List<CommentConfig> commentConfigList = commentConfigService.list(commentConfigCriteria);
		if(commentConfigList == null || commentConfigList.size() < 1){
			logger.warn("找不到针对对象[" + commentConfigCriteria.getObjectType() + "#" + commentConfigCriteria.getObjectId() + "]的评论配置");
			return null;
		}
		CommentConfig commentConfig = null;
		for(CommentConfig cc : commentConfigList){
			if(cc.getObjectId() == comment.getObjectId()){
				//找到了针对该对象的评论配置
				if(cc.getInitStatus() == MessageStatus.deleted.id){
					logger.debug("对象[" + commentConfigCriteria.getObjectType() + "#" + commentConfigCriteria.getObjectId() + "]不允许评论");
					return null;
				}
				commentConfig = cc;
				break;				
			}
		}
		if(commentConfig == null){
			//查找全局配置
			for(CommentConfig cc : commentConfigList){
				if(cc.getObjectType().equals(comment.getObjectType()) && cc.getObjectId() == 0 ){
					//找到了全局配置
					if(cc.getInitStatus() == MessageStatus.deleted.id){
						logger.debug("对象[" + commentConfigCriteria.getObjectType() + "被全局配置为不允许评论");
						return null;
					}
					commentConfig = cc;

					break;
				}
			}
		}
		if(commentConfig == null){
			logger.warn("找不到针对对象[" + commentConfigCriteria.getObjectType() + "#" + commentConfigCriteria.getObjectId() + "]的评论配置，将不允许发布评论");
			return null;
		}
		logger.debug("对象[" + comment.getObjectType() + "#" + comment.getObjectId() + "]的评论配置是:" + commentConfig);
		return commentConfig;
	}


	@Override
	public List<List<Comment>> sort(List<Comment> commentList){
		List<List<Comment>> commentList2 = new ArrayList<List<Comment>>();

		//把第一个评价和跟帖放入一个列表commentList3，然后把这个作为一个一级元素，也就是一个整体评论放入commentList2
		for(Comment comment : commentList){
			if(comment.getRootCommentId() == 0){				
				List<Comment> commentList3 = new ArrayList<Comment>();
				commentList3.add(comment);
				for(Comment comment2 : commentList){
					if(comment2.getRootCommentId() == comment.getCommentId()){
						commentList3.add(comment2);
					}
				}
				commentList2.add(commentList3);
			}
		}



		//按发布时间进行排序
		Collections.sort(commentList2, new Comparator<List<Comment>>(){
			@Override
			public int compare(List<Comment> o1, List<Comment> o2) {
				if(o1.get(0).getPublishTime() == null || o2.get(0).getPublishTime() == null){
					if(o1.get(0).getCreateTime().before(o2.get(0).getCreateTime())){
						return 1;
					}
					return -1;
				}	else if(o1.get(0).getPublishTime().before(o2.get(0).getPublishTime())){
					return 1;
				}
				return -1;
			}});
		
		for(List<Comment> list1 : commentList2){
			//按发布ID大小进行排序
			Collections.sort(list1, new Comparator<Comment>(){
				@Override
				public int compare(Comment o1, Comment o2) {
					if(o1.getCommentId() > o2.getCommentId()){
						return 1;
					}
					return -1;
				}});

		}
		return commentList2;



	}




}
