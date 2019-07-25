package com.maicard.mb.service;

import java.util.List;

import com.maicard.mb.criteria.EisTopicCriteria;
import com.maicard.mb.domain.EisTopic;

public interface EisTopicService {

	int insert(EisTopic eisTopic);

	int update(EisTopic eisTopic);

	int delete(int udid);
		
	EisTopic select(int topicId);
	
	EisTopic select(String topicCode);
	
	List<EisTopic> list(EisTopicCriteria topicCriteria);

	List<EisTopic> listOnPage(EisTopicCriteria topicCriteria);
		
	int count(EisTopicCriteria topicCriteria);
	
		


}
