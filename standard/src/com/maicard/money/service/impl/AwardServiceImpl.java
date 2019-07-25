package com.maicard.money.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.money.criteria.AwardCriteria;
import com.maicard.money.dao.AwardDao;
import com.maicard.money.domain.Award;
import com.maicard.money.domain.Money;
import com.maicard.money.service.AwardService;
import com.maicard.standard.DataName;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;

import org.springframework.stereotype.Service;

@Service
public class AwardServiceImpl extends BaseService implements AwardService {

	@Resource
	private ConfigService configService;
	
	@Resource
	private AwardDao awardDao;


	@Override
	public int insert(Award award){
		return awardDao.insert(award);
	}

	public int update(Award award) {
		try{
			return  awardDao.update(award);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}



	public List<Award> list(AwardCriteria awardCriteria) {
		return awardDao.list(awardCriteria);
	}

	public List<Award> listOnPage(AwardCriteria awardCriteria) {
		return awardDao.listOnPage(awardCriteria);
	}






	@Override
	public Award select(long awardId) {
		return awardDao.select(awardId);
	}

	@Override
	public int delete(long awardId) {
		return awardDao.delete(awardId);
	}

	@Override
	public Award convert(Money money) {
		String moneyName = configService.getValue(DataName.moneyName.toString(), money.getOwnerId());
		if(moneyName == null){
			moneyName = MoneyType.money.getName();
		}
		String coinName = configService.getValue(DataName.coinName.toString(), money.getOwnerId());
		if(coinName == null){
			coinName = MoneyType.coin.getName();
		}

		String pointName = configService.getValue(DataName.pointName.toString(), money.getOwnerId());
		if(pointName == null){
			pointName = MoneyType.point.getName();
		}
		String scoreName = configService.getValue(DataName.scoreName.toString(), money.getOwnerId());
		if(scoreName == null){
			pointName = MoneyType.score.getName();
		}		
		Award award = new Award(money.getUuid(), money.getOwnerId());
		award.setObjectType(ObjectType.money.name());
		award.setCreateTime(new Date());
		//award.setAwardCount(awardCount);(objectUnit);
		return null;
	}

	@Override
	public int count(AwardCriteria awardCriteria) {
		return awardDao.count(awardCriteria);
	}


	
	
}
