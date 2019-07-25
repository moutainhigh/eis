package com.maicard.common.cache;

import com.maicard.common.domain.Area;

public interface CachedAreaService {
	Area select(long areaId);
}
