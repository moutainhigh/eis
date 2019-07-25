package com.maicard.wpt.service.general;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Comment;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.processor.CommentProcessor;

import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.DataName;
import com.maicard.standard.OperateResult;

@Service
public class DocumentCommentProcessor extends BaseService implements CommentProcessor{

	@Resource
	private DocumentService documentService;

	@Override
	public EisMessage execute(String action, Comment comment, Map<String, Object> parameterMap) {
		
		return _create(comment);

	}

	
	private EisMessage _create(Comment comment) {
		
		Document document = documentService.select((int)comment.getObjectId());
		
		if(document != null){
			comment.setExtraValue("refUrl", document.getViewUrl());
			comment.setExtraValue("refTitle", document.getTitle());
			String image = document.getExtraValue(DataName.productSmallImage.toString());
			if(image != null){
				comment.setExtraValue("refImage", image);
			}
		}
		
		return new EisMessage(OperateResult.success.getId(), "允许评论");
	}

}
