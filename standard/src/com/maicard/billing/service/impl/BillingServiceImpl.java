package com.maicard.billing.service.impl;

import com.maicard.billing.criteria.BillingCriteria;
import com.maicard.billing.dao.BillingDao;
import com.maicard.billing.domain.Billing;
import com.maicard.billing.service.BillingService;
import com.maicard.common.base.BaseService;
import com.maicard.money.criteria.PayCriteria;
import com.maicard.money.service.PayService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.TransactionStandard.TransactionStatus;
import com.maicard.standard.ts.SettlementStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class BillingServiceImpl extends BaseService implements BillingService {

	@Resource
	private BillingDao billingDao;
	
	@Resource
	private PayService payService;
	
	final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@Override
	public int insert(Billing billing) {
		return billingDao.insert(billing);

	}

	public int update(Billing billing) {
		try{
			return billingDao.update(billing);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
		
	}

	public int delete(int BillingId) {
		try{
			return billingDao.delete(BillingId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
		
	}
	
	//@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Billing#' + #BillingId")
	public Billing select(int BillingId) {
		return billingDao.select(BillingId);
	}
	

	

	public List<Billing> list(BillingCriteria billingCriteria) {
		return billingDao.list(billingCriteria);
	}
	
	public List<Billing> listOnPage(BillingCriteria billingCriteria) {
		
	List<Billing> billingList =   billingDao.listOnPage(billingCriteria);
		
		for(int i = 0; i < billingList.size(); i++){
			
			billingList.get(i).setId(billingList.get(i).getBillingId());
			billingList.get(i).setIndex(i+1);
		}
		return billingList;
	
	}
	
	public int count(BillingCriteria billingCriteria) {
		return billingDao.count(billingCriteria);
	}



	@Override
	public Billing billing(BillingCriteria billingCriteria) {
		
		Assert.notNull(billingCriteria.getBillingBeginTimeBegin(),"结算开始日期起始时间beginTimeBegin不能为空");
		Assert.notNull(billingCriteria.getBillingEndTimeEnd(),"结算结束日期截至时间endTimeEnd不能为空");
		Assert.isTrue(billingCriteria.getUuid() > 0,"结算用户不能为空");
		
		PayCriteria payCriteria = new PayCriteria(billingCriteria.getOwnerId());
		payCriteria.setEndTimeBegin(billingCriteria.getBillingBeginTimeBegin());
		payCriteria.setEndTimeEnd(billingCriteria.getBillingEndTimeEnd());
		payCriteria.setPayFromAccount(billingCriteria.getUuid());
		payCriteria.setCurrentStatus(TransactionStatus.success.id);
		payCriteria.setPayMethodId((int)billingCriteria.getObjectId());
		if(StringUtils.isNotBlank(billingCriteria.getPayCardType())){
			payCriteria.setPayCardType(billingCriteria.getPayCardType());
		}

		int successCount = payService.count(payCriteria);
		if(successCount < 1){
			logger.info("用户:" + billingCriteria.getUuid() + "在时间段[" + sdf.format(payCriteria.getEndTimeBegin()) + "=>" + sdf.format(payCriteria.getEndTimeEnd()) + "]期间的成功订单为0，忽略");
			return null;
		}
		Billing billing = new Billing(billingCriteria.getOwnerId());
		
		billing.setUuid(billingCriteria.getUuid());
		billing.setBillingBeginTime(billingCriteria.getBillingBeginTimeBegin());
		billing.setBillingEndTime(billingCriteria.getBillingEndTimeEnd());
		billing.setObjectId(billingCriteria.getObjectId());
		billing.setShareConfigId(billingCriteria.getShareConfigId());
		billing.setCurrentStatus(SettlementStatus.settled.id);
		int rs = this.insert(billing);
		if(rs != 1){
			logger.error("无法创建结算，因为新增结算数据返回为:" + rs);
			return null;
		}
		
		billingDao.billing(billing);
		return select(billing.getBillingId());
		
	}

}
