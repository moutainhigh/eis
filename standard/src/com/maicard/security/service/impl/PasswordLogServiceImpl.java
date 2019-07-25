package com.maicard.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.DataDefineService;
import com.maicard.security.criteria.PasswordLogCriteria;
import com.maicard.security.dao.PasswordLogDao;
import com.maicard.security.domain.PasswordLog;
import com.maicard.security.service.PasswordLogService;

@Service
public class PasswordLogServiceImpl extends BaseService implements PasswordLogService {

	@Resource
	private PasswordLogDao passwordLogDao;
	
	@Resource
	private DataDefineService dataDefineService;

	public int insert(PasswordLog passwordLog) {
		return passwordLogDao.insert(passwordLog);
	}

	



	public List<PasswordLog> list(PasswordLogCriteria passwordLogCriteria) {
		List<PasswordLog> passwordLogList = passwordLogDao.list(passwordLogCriteria);
		if(passwordLogList != null){
			for(int i = 0; i < passwordLogList.size(); i++){
				passwordLogList.get(i).setIndex(i+1);
			}
		}
		return passwordLogList;
	}

	public List<PasswordLog> listOnPage(PasswordLogCriteria passwordLogCriteria) {
		List<PasswordLog> passwordLogList = passwordLogDao.listOnPage(passwordLogCriteria);
		if(passwordLogList != null){
			for(int i = 0; i < passwordLogList.size(); i++){
				passwordLogList.get(i).setIndex(i+1);
			}
		}
		return passwordLogList;	
	}

	@Override
	public int count(PasswordLogCriteria passwordLogCriteria) {
		return passwordLogDao.count(passwordLogCriteria);
	}



	@Override
	public PasswordLog select(int id) {
		PasswordLog passwordLog =  passwordLogDao.select(id);	
		if(passwordLog != null){
		}
		return passwordLog;
	}

	

	
	

}
