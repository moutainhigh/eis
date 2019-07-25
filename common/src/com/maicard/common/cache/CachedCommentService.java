package com.maicard.common.cache;

import com.maicard.common.domain.Comment;

public interface CachedCommentService {
	Comment select(long commentId);
}
