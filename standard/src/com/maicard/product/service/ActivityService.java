package com.maicard.product.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.maicard.common.domain.EisMessage;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.domain.Activity;
import com.maicard.security.domain.User;
import com.maicard.site.domain.Document;

public interface ActivityService {

	int insert(Activity activity);

	int update(Activity activity);

	int delete(long activityId);

	Activity select(long activityId);

	List<Activity> list(ActivityCriteria activityCriteria);

	List<Activity> listOnPage(ActivityCriteria activityCriteria);

	int count(ActivityCriteria activityCriteria);
	
	EisMessage prepare(Activity activity, User frontUser, Object parameter);

	String createActivityBeginUrl(long activityId, String inviteCode);

	EisMessage execute(Activity activity, User user, Map<String, String> requestData);

	Activity generateNewActivity(String activityType);

	Document getRefDocument(Activity activity);

	boolean syncActivityByDocument(Document document) throws ParseException;

	Activity select(String activityCode, long ownerId);


}
