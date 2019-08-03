package com.maicard.money.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.JsonUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.dao.BankAccountDao;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.service.BankAccountService;

@Service
public class BankAccountServiceImpl extends BaseService implements BankAccountService {

	@Resource
	private BankAccountDao bankAccountDao;

	@Resource
	private MessageService messageService;
	@Resource
	private DataDefineService dataDefineService;

	public int insert(BankAccount bankAccount) {
		if(bankAccount.getCreateTime() == null){
			bankAccount.setCreateTime(new Date());
		}
	
		return bankAccountDao.insert(bankAccount);
		
	}

	public int update(BankAccount bankAccount) {
		int actualRowsAffected = 0;

		//int id = (int)bankAccount.getId();
		int id = bankAccount.getBankAccountId();
		BankAccount _oldBankAccount = bankAccountDao.select(id);

		if (_oldBankAccount != null) {
			actualRowsAffected = bankAccountDao.update(bankAccount);

		}

		return actualRowsAffected;
	}



	public List<BankAccount> list(BankAccountCriteria bankAccountCriteria) {
		List<BankAccount> bankAccountList = bankAccountDao.list(bankAccountCriteria);
		if(bankAccountList != null){
			for(int i = 0; i < bankAccountList.size(); i++){
				bankAccountList.get(i).setIndex(i+1);
				afterFetch(bankAccountList.get(i));
			}
		}
		return bankAccountList;
	}

	public List<BankAccount> listOnPage(BankAccountCriteria bankAccountCriteria) {
		List<BankAccount> bankAccountList = bankAccountDao.listOnPage(bankAccountCriteria);
		if(bankAccountList == null){
			return Collections.emptyList();

		}
		for(int i = 0; i < bankAccountList.size(); i++){
			bankAccountList.get(i).setIndex(i+1);
			afterFetch(bankAccountList.get(i));
		}
		return bankAccountList;	
	}

	@Override
	public int count(BankAccountCriteria bankAccountCriteria) {
		return bankAccountDao.count(bankAccountCriteria);
	}

	private void afterFetch(BankAccount bankAccount){


	}

	@Override
	public BankAccount select(int id) {
		BankAccount bankAccount =  bankAccountDao.select(id);	
		if(bankAccount != null){
			afterFetch(bankAccount);
		}
		return bankAccount;
	}

	@Override
	public int delete(int id) {
		BankAccount _oldBankAccount = select(id);
		int actualRowsAffected = 0;
		if(_oldBankAccount != null){
			actualRowsAffected = bankAccountDao.delete(id);
		}
		return actualRowsAffected;
	}

	@Override
	public int setDefaultAdd(BankAccount bankAccount)
	{
		int actualRowsAffected = bankAccountDao.setDefaultAdd(bankAccount);
		return actualRowsAffected;
	}

	@Override
	public int createIfNotExist(BankAccount bankAccount) {
		Assert.notNull(bankAccount,"尝试不重复创建的银行帐号为空");
		Assert.isTrue(bankAccount.getUuid() != 0,"尝试不重复创建的银行帐号，所属用户不能为空");
		Assert.notNull(bankAccount.getBankName(),"尝试不重复创建的银行帐号，银行名称为空");
		Assert.notNull(bankAccount.getBankName(),"尝试不重复创建的银行帐号，银行名称为空");
		Assert.notNull(bankAccount.getBankAccountName(),"尝试不重复创建的银行帐号，开户名字或帐号为空");
		if(bankAccount.getBankAccountId() > 0){
			logger.error("不重复创建的银行帐号ID大于0,不执行创建尝试");
			return 0;
		}
		int rs = 0;
		try{
			rs = this.insert(bankAccount);
		}catch(Exception e) {
			logger.error("无法创建账号:{}，错误原因:{}", JsonUtils.toStringFull(bankAccount), e.getMessage());
			e.printStackTrace();
			return 0;
		}
		if(rs == 1){
			messageService.sendJmsDataSyncMessage(null, "bankAccountService", "insert", bankAccount);
		}
		return rs;
		
		
	}


}
