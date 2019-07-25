package com.maicard.common.base;


import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseDao{

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	
	public SqlSessionTemplate getSqlSessionTemplate(){
		return sqlSessionTemplate;
	}
	
}
