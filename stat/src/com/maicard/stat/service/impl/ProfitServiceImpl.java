package com.maicard.stat.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.stat.criteria.ProfitCriteria;
import com.maicard.stat.dao.ProfitDao;
import com.maicard.stat.domain.Profit;
import com.maicard.stat.service.ProfitService;

@Service
public class ProfitServiceImpl extends BaseService implements ProfitService {
	@Resource
	private ProfitDao profitDao;


	/*public void setProfitDao(ProfitDao profitDao) {
		this.profitDao = profitDao;
	}
	 */

	@Override
	public List<Profit> listOnPage(ProfitCriteria profitCriteria) {

		if(profitCriteria == null){
			return Collections.emptyList();
		}
		
		return profitDao.listOnPage(profitCriteria);
	}
	@Override
	public List<Profit> list(ProfitCriteria profitCriteria) {
		if(profitCriteria == null){
			return null;
		}
		return profitDao.list(profitCriteria);
	}



	@Override
	public int count(ProfitCriteria profitCriteria){
		return profitDao.count(profitCriteria);    	
	}
	
	
	@Override
	public int insert(Profit profit) {
		return profitDao.insert(profit);    	
	}
	
	
	@Override
	public int deleteBy(ProfitCriteria profitCriteria) {
		return profitDao.deleteBy(profitCriteria);    	
	}

}
