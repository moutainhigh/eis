package com.maicard.money.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.money.criteria.PointExchangeLogCriteria;
import com.maicard.money.dao.PointExchangeLogDao;
import com.maicard.money.domain.PointExchange;
import com.maicard.money.service.PointExchangeLogService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

@Service
public class PointExchangeLogServiceImpl extends BaseService implements PointExchangeLogService {

	@Resource
	private PointExchangeLogDao pointExchangeLogDao;

	@Override
	public int insert(PointExchange pointExchange) {
		if(pointExchange.getCurrentStatus() == 0){
			pointExchange.setCurrentStatus(TransactionStatus.newOrder.getId());
		}
		return pointExchangeLogDao.insert(pointExchange);
	}



	public int update(PointExchange pointExchange) throws Exception {
		return pointExchangeLogDao.update(pointExchange);		
	}

	

	public int delete(int pointExchangeId) {
		int actualRowsAffected = 0;

		PointExchange _oldPointExchange = pointExchangeLogDao.select(pointExchangeId);

		if (_oldPointExchange != null) {
			actualRowsAffected = pointExchangeLogDao.delete(pointExchangeId);
		}

		return actualRowsAffected;
	}

	public PointExchange select(int pointExchangeId) {
		return pointExchangeLogDao.select(pointExchangeId);
	}


	public List<PointExchange> list(PointExchangeLogCriteria pointExchangeLogCriteria) {
		return pointExchangeLogDao.list(pointExchangeLogCriteria);
	}

	public List<PointExchange> listOnPage(PointExchangeLogCriteria pointExchangeLogCriteria) {
		

		List<PointExchange> pointExchangeList = pointExchangeLogDao.listOnPage(pointExchangeLogCriteria);
		if(pointExchangeList == null){
			return null;
		}
		/*for(int i = 0; i < pointExchangeList.size(); i++){
			pointExchangeList.get(i).setIndex(i+1);
			afterFetch(pointExchangeList.get(i));
		}*/
		return pointExchangeList;
	}
	


	public int count(PointExchangeLogCriteria pointExchangeLogCriteria) {
		return pointExchangeLogDao.count(pointExchangeLogCriteria);
	}

	

	




}
