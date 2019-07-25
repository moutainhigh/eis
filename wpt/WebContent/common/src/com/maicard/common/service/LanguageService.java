package com.maicard.common.service;

import java.util.List;

import com.maicard.common.criteria.LanguageCriteria;
import com.maicard.common.domain.Language;

public interface LanguageService {

	int insert(Language language);
	
	int update(Language language);

	int delete(int languageId);
	
	Language select(int languageId);

	List<Language> list(LanguageCriteria languageCriteria);

	List<Language> listOnPage(LanguageCriteria languageCriteria);
	
	int count(LanguageCriteria languageCriteria);
	
}
