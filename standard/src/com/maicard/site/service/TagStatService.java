package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.TagStatCriteria;
import com.maicard.site.domain.TagStat;

public interface TagStatService {

	void insert(TagStat tagStat);

	int update(TagStat tagStat);

	int delete(int tagStatId);
	
	TagStat select(int tagStatId);

	List<TagStat> list(TagStatCriteria tagStatCriteria);

	List<TagStat> listOnPage(TagStatCriteria tagStatCriteria);

}
