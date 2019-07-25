package com.maicard.common.processor;

import java.util.Map;

import com.maicard.common.domain.Comment;
import com.maicard.common.domain.EisMessage;

public interface CommentProcessor {
	EisMessage execute(String action, Comment comment, Map<String,Object>parameterMap);
}
