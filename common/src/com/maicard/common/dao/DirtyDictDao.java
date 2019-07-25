package com.maicard.common.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;



public interface DirtyDictDao {

	
	boolean exist(String word) throws DataAccessException;

	boolean create(String word) throws DataAccessException;

	List<String> list() throws DataAccessException;

	String replace(String word);

	/**
	 * 检查一句话是否有敏感词，并返回第一个找到的敏感词
	 * 
	 *
	 * @author NetSnake
	 * @date 2018-04-11
	 */
	String check(String sentence);

}
