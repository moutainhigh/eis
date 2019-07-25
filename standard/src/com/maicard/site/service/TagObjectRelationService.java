package com.maicard.site.service;

import java.util.List;


import com.maicard.common.domain.EisObject;
import com.maicard.site.criteria.TagObjectRelationCriteria;
import com.maicard.site.domain.Tag;
import com.maicard.site.domain.TagObjectRelation;

public interface TagObjectRelationService {

	int insert(TagObjectRelation tagObjectRelation);

	int update(TagObjectRelation tagObjectRelation);

	int delete(long tagObjectRelationId);
	
	int delete(TagObjectRelationCriteria tagObjectRelationCriteria);

	
	TagObjectRelation select(long tagObjectRelationId);

	List<TagObjectRelation> list(TagObjectRelationCriteria tagObjectRelationCriteria);

	List<TagObjectRelation> listOnPage(TagObjectRelationCriteria tagObjectRelationCriteria);


	int sync(long ownerId, String objectType, long objectId, String... tags);

	List<Tag> listTags(TagObjectRelationCriteria tagObjectRelationCriteria);

	void processTagForTag(EisObject target) throws Exception;

	int count(TagObjectRelationCriteria tagObjectRelationCriteria);
	
	

}
