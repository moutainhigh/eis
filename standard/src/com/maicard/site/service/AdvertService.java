package com.maicard.site.service;

import java.util.List;

import com.maicard.site.criteria.AdvertCriteria;
import com.maicard.site.domain.Advert;
import com.maicard.site.domain.Document;

public interface AdvertService {

	int insert(Advert advert);

	int update(Advert advert);

	int delete(int advertId);
	
	Advert select(int advertId);
	
	List<Advert> list(AdvertCriteria advertCriteria);

	List<Advert> listOnPage(AdvertCriteria advertCriteria);
	
	int count(AdvertCriteria advertCriteria);

	void applyAdvertData(Document document);

	void addShow(Advert advert);


}
