package com.maicard.common.cache;

import com.maicard.common.domain.CommentConfig;

public interface CachedCommentConfigService {
	CommentConfig select(long commentConfigId);
}
