package com.maicard.wpt.service.cartoon;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.processor.PostUserRelationProcessor;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.Node;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.NodeService;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;


/**
 * 当用户对动漫进行评分、收藏等操作/反向操作后的处理
 * @author GHOST
 * @date 2018-10-25
 *
 */

@Service
public class PostUserRelationProcessorImpl extends BaseService implements PostUserRelationProcessor {

	@Resource
	private DocumentService documentService;

	@Resource
	private NodeService nodeService;


	@Override
	public void postProcess(User frontUser, UserRelation userRelation, String action) {
		logger.debug("处理用户:{}对关联:{}的操作:{}", frontUser.getUuid(), userRelation, action);
		Node node = null;
		//找到对应的栏目
		if(userRelation.getObjectType().equalsIgnoreCase(ObjectType.document.name())) {
			Document document = documentService.select(((int)userRelation.getObjectId()));
			if(document == null) {
				logger.error("找不到用户:{}关注的文章:{}", userRelation.getObjectId());
				return;
			}
			node = document.getDefaultNode();
		} else {
			node = nodeService.select((int)userRelation.getObjectId());
			if(node == null) {
				logger.error("找不到用户:{}关注的栏目:{}", userRelation.getObjectId());
				return;
			}
		}


		if(userRelation.getRelationType().equalsIgnoreCase(UserRelationCriteria.RELATION_TYPE_FAVORITE)) {
			//收藏/关注
			if(action.equalsIgnoreCase(Operate.create.name)) {
				//新建收藏
				long favoriteCount = node.getLongExtraValue(DataName.favoriteCount.name());
				favoriteCount++;
				logger.debug("把用户:{}收藏的栏目:{}的收藏数增加为:{}", frontUser.getUuid(), node.getNodeId(), favoriteCount);
				node.setExtraValue(DataName.favoriteCount.name(), String.valueOf(favoriteCount));
			} else {
				//放弃收藏
				long favoriteCount = node.getLongExtraValue(DataName.favoriteCount.name());
				if(favoriteCount > 1) {
					favoriteCount--;
				}
				logger.debug("把用户:{}收藏的栏目:{}的收藏数减少为:{}", frontUser.getUuid(), node.getNodeId(), favoriteCount);
			node.setExtraValue(DataName.favoriteCount.name(), String.valueOf(favoriteCount));
			}
		} 

	}

}
