package com.maicard.common.dao.ibatis;

import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.maicard.common.base.BaseDao;
import com.maicard.common.dao.DirtyDictDao;
import com.maicard.common.service.CacheService;

@Repository
public class DirtyDictDaoImpl extends BaseDao implements DirtyDictDao {

	@Resource
	private CacheService cacheService;


	private final String[] illegalDict = new String[]{",","#","\"", "'", ";","="};

	private static LinkedHashSet<String> dirtyDictSet = new LinkedHashSet<String>();

	public void init(){
		List<String> existWordList = list();
		if(existWordList != null && existWordList.size() > 0){
			for(String word : existWordList){
				if(StringUtils.isBlank(word)){
					continue;
				}
				dirtyDictSet.add(word.trim());
			}
			logger.info("初始化敏感词数据" + existWordList.size() + "个");
		} else {
			logger.warn("未找到任何敏感词数据");
		}

	}

	@Override
	public List<String> list() throws DataAccessException {
		return getSqlSessionTemplate().selectList("DirtyDict.list");
	}

	@Override
	public boolean exist(String word) throws DataAccessException {
		if(word == null){
			return false;
		}
		for(String c : illegalDict){
			if(StringUtils.isBlank(c)){
				continue;
			}
			if(word.indexOf(c) >= 0){
				logger.debug("单词[" + word + "]包含了非法字符[" + c + "]");
				return true;
			}
		}
		if(dirtyDictSet == null || dirtyDictSet.size() < 1){
			init();
		}
		//去除所有空白和标点符号以防止干扰
		word = word.replaceAll("\\s*","").replaceAll("\\pP", "");
		//循环
		for(String existDirtyWord : dirtyDictSet){
			if(StringUtils.isBlank(existDirtyWord)){
				continue;
			}
			if(word.indexOf(existDirtyWord) >= 0){
				logger.debug("从缓存中找到了敏感词数据[" + existDirtyWord + "]与比对数据[" + word + "]相符");
				return true;				
			}

			/*if(word.matches(existDirtyDict)){
							logger.debug("从缓存中找到了敏感词数据[" + existDirtyDict + "]与比对数据[" + word + "]匹配");
							return true;				

						}*/
		}


		return false;

	}
	
	@Override
	public String check(String sentence) {
		if(sentence == null){
			return null;
		}
		for(String c : illegalDict){
			if(StringUtils.isBlank(c)){
				continue;
			}
			if(sentence.indexOf(c) >= 0){
				logger.debug("[" + sentence + "]包含了非法字符[" + c + "]");
				return c;
			}
		}
		if(dirtyDictSet == null || dirtyDictSet.size() < 1){
			init();
		}
		//去除所有空白和标点符号以防止干扰
		sentence = sentence.replaceAll("\\s*","").replaceAll("\\pP", "");
		//循环
		for(String existDirtyWord : dirtyDictSet){
			if(StringUtils.isBlank(existDirtyWord)){
				continue;
			}
			if(sentence.indexOf(existDirtyWord) >= 0){
				logger.debug("从缓存中找到了敏感词数据[" + existDirtyWord + "]与比对数据[" + sentence + "]相符");
				return existDirtyWord;				
			}

			
		}


		return null;

	}

	@Override
	public boolean create(String word) throws DataAccessException {
		if(word == null){
			return false;
		}
		try{
			if(getSqlSessionTemplate().insert("DirtyDict.insert", word) == 1){
				logger.debug("成功创建敏感词数据[" + word + "]");
			}
			return true;
		}catch(Exception e){}
		dirtyDictSet.add(word.trim());
		return true;

	}

	@Override
	public String replace(String word)  {
		if(word == null){
			return word;
		}
		String result = word;
		for(String c : illegalDict){
			if(StringUtils.isBlank(c)){
				continue;
			}
			if(word.indexOf(c) >= 0){
				logger.debug("单词[" + word + "]包含了非法字符[" + c + "]");
				result = result.replaceAll(c, "*");
			}
		}
		if(dirtyDictSet == null || dirtyDictSet.size() < 1){
			init();
		}
		//去除所有空白和标点符号以防止干扰
		word = word.replaceAll("\\s*","").replaceAll("\\pP", "");
		result = result.replaceAll("\\s*","").replaceAll("\\pP", "");
		//循环
		for(String existDirtyWord : dirtyDictSet){

			if(word.indexOf(existDirtyWord) >= 0){
				logger.debug("从缓存中找到了敏感词数据[" + existDirtyWord + "]与比对数据[" + word + "]相符");
				result = result.replaceAll(existDirtyWord , "*");
			}

			/*if(word.matches(existDirtyDict)){
							logger.debug("从缓存中找到了敏感词数据[" + existDirtyDict + "]与比对数据[" + word + "]匹配");
							return true;				

						}*/
		}


		return result;

	}




}
