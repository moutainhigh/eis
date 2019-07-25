package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.TagCriteria;
import com.maicard.site.domain.Tag;

public interface TagService {

	int insert(Tag tag);

	int update(Tag tag);

	int delete(long tagId);
	
	Tag select(long tagId);

	List<Tag> list(TagCriteria tagCriteria);

	List<Tag> listOnPage(TagCriteria tagCriteria);

	String parseTag(String src);
	
	int count(TagCriteria tagCriteria);

	Tag select(String tagName, long ownerId);


}
