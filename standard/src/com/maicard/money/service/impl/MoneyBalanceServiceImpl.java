package com.maicard.money.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.CenterDataService;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.MoneyBalanceCriteria;
import com.maicard.money.dao.MoneyBalanceDao;
import com.maicard.money.domain.MoneyBalance;
import com.maicard.money.service.MoneyBalanceService;

@Service
public class MoneyBalanceServiceImpl extends BaseService implements MoneyBalanceService {

	@Resource
	private MoneyBalanceDao moneyBalanceDao;

	@Resource
	private MessageService messageService;
	@Resource
	private CenterDataService centerDataService;

	public int insert(MoneyBalance moneyBalance) {
		if(moneyBalance.getCreateTime() == null){
			moneyBalance.setCreateTime(new Date());
		}

		try{
			moneyBalanceDao.insert(moneyBalance);
		}
		catch(Exception e){
			return 0;
		}
		return 1;
	}

	public int update(MoneyBalance moneyBalance) {
		int actualRowsAffected = 0;

		//int id = (int)moneyBalance.getId();
		int id = moneyBalance.getMoneyBalanceId();
		MoneyBalance _oldMoneyBalance = moneyBalanceDao.select(id);

		if (_oldMoneyBalance != null) {
			actualRowsAffected = moneyBalanceDao.update(moneyBalance);

		}

		return actualRowsAffected;
	}



	public List<MoneyBalance> list(MoneyBalanceCriteria moneyBalanceCriteria) {
		List<MoneyBalance> moneyBalanceList = moneyBalanceDao.list(moneyBalanceCriteria);
		if(moneyBalanceList == null){
			return Collections.emptyList();

		}		
		for(int i = 0; i < moneyBalanceList.size(); i++){
			moneyBalanceList.get(i).setIndex(i+1);
			afterFetch(moneyBalanceList.get(i));
		}

		return moneyBalanceList;
	}

	public List<MoneyBalance> listOnPage(MoneyBalanceCriteria moneyBalanceCriteria) {
		List<MoneyBalance> moneyBalanceList = moneyBalanceDao.listOnPage(moneyBalanceCriteria);
		if(moneyBalanceList == null){
			return Collections.emptyList();

		}
		for(int i = 0; i < moneyBalanceList.size(); i++){
			moneyBalanceList.get(i).setIndex(i+1);
			afterFetch(moneyBalanceList.get(i));
		}
		return moneyBalanceList;	
	}

	@Override
	public int count(MoneyBalanceCriteria moneyBalanceCriteria) {
		return moneyBalanceDao.count(moneyBalanceCriteria);
	}

	private void afterFetch(MoneyBalance moneyBalance){


	}

	@Override
	public MoneyBalance select(int id) {
		MoneyBalance moneyBalance =  moneyBalanceDao.select(id);	
		if(moneyBalance != null){
			afterFetch(moneyBalance);
		}
		return moneyBalance;
	}

	@Override
	public int delete(int id) {
		MoneyBalance _oldMoneyBalance = select(id);
		int actualRowsAffected = 0;
		if(_oldMoneyBalance != null){
			actualRowsAffected = moneyBalanceDao.delete(id);
		}
		return actualRowsAffected;
	}

	
}
