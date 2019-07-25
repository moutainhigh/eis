package com.maicard.billing.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.billing.criteria.SettlementCriteria;
import com.maicard.billing.dao.SettlementDao;
import com.maicard.billing.domain.Settlement;
import com.maicard.billing.service.SettlementService;
import com.maicard.common.base.BaseService;
import com.maicard.standard.TransactionStandard;

@Service
public class SettlementServiceImpl extends BaseService implements SettlementService {

	@Resource
	private SettlementDao settlementDao;

	public int insert(SettlementCriteria settlementCriteria) {
		try{
			return settlementDao.insert(settlementCriteria);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(SettlementCriteria settlementCriteria) {
		
       List<Settlement> settlementList = settlementDao.list(settlementCriteria);
		
		for(int i = 0; i < settlementList.size(); i++){			
			Settlement _oldSettlement = settlementDao.select(settlementList.get(i).getSettlementId());			
			if (_oldSettlement != null) {
				_oldSettlement.setCurrentStatus(TransactionStandard.TransactionStatus.success.getId());
				try{
					 settlementDao.update(_oldSettlement);
				}catch(Exception e){
					logger.error("更新数据失败:" + e.getMessage());
					return 0;
				}
			}	
			
		}	
		return 1;
	}

	public int delete(int BillingId) {
		try{
			return settlementDao.delete(BillingId);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	public Settlement select(int BillingId) {
		return settlementDao.select(BillingId);
	}
	

	public List<Settlement> listRecentBilling(SettlementCriteria settlementCriteria) {
		
		
		List<Settlement> billingList =   settlementDao.listRecentBilling(settlementCriteria);
		
		for(int i = 0; i < billingList.size(); i++){
			
			billingList.get(i).setId(billingList.get(i).getSettlementId());
			billingList.get(i).setIndex(i+1);
		}
		return billingList;
		
	}

	public List<Settlement> list(SettlementCriteria settlementCriteria) {
		return settlementDao.list(settlementCriteria);
	}


	public List<Settlement> listOnPage(SettlementCriteria settlementCriteria) {
		
	List<Settlement> billingList =   settlementDao.listOnPage(settlementCriteria);
		
		for(int i = 0; i < billingList.size(); i++){
			
			billingList.get(i).setId(billingList.get(i).getId());
			billingList.get(i).setIndex(i+1);
		}
		return billingList;
	
	}
	
	public int count(SettlementCriteria settlementCriteria) {
		return settlementDao.count(settlementCriteria);
	}

	public int countrecentbilling(SettlementCriteria settlementCriteria) {
		return settlementDao.countrecentbilling(settlementCriteria);
	}


}
