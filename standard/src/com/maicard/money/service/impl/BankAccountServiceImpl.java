package com.maicard.money.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.DataDefineService;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.BankAccountCriteria;
import com.maicard.money.dao.BankAccountDao;
import com.maicard.money.domain.BankAccount;
import com.maicard.money.service.BankAccountService;
import com.maicard.standard.EisError;

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
		
		BankAccountCriteria bankAccountCriteria = new BankAccountCriteria();
		bankAccountCriteria.setOwnerId(bankAccount.getOwnerId());
		bankAccountCriteria.setUuid(bankAccount.getUuid());
		bankAccountCriteria.setBankName(bankAccount.getBankName());
		bankAccountCriteria.setBankAccountName(bankAccount.getBankAccountName());
		//查找银行、账号名一样的记录，同一个银行，只应该有一个同名帐号，因此不把帐号accountNumber放入判断条件
		List<BankAccount> bankAccountList = this.list(bankAccountCriteria);
		if(bankAccountList == null || bankAccountList.size() < 1){
			logger.debug("根据条件[uuid=" + bankAccountCriteria.getUuid() + ",bankName=" + bankAccountCriteria.getBankName() + ",bankAccountName=" + bankAccountCriteria.getBankAccountName() + "]未找到银行帐号，新增并返回");
			int rs = this.insert(bankAccount);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "bankAccountService", "insert", bankAccount);
			}
			return rs;
		}
		if(bankAccountList.size() != 1){
			logger.debug("根据条件[uuid=" + bankAccountCriteria.getUuid() + ",bankName=" + bankAccountCriteria.getBankName() + ",bankAccountName=" + bankAccountCriteria.getBankAccountName() + "]得到的帐号数量不唯一，是:" +  bankAccountList.size());
			return -1;
		}
		BankAccount _oldBankAccount = bankAccountList.get(0);
		if(_oldBankAccount.getBankAccountNumber() != null){
			if(bankAccount.getBankAccountNumber() == null){
				logger.error("尝试创建的帐号没有提供银行帐号数据，但相同用户名、相同银行的已存在帐号有对应的帐号数据:" + _oldBankAccount);
				return EisError.ACCOUNT_ERROR.id;
			}
			if(!_oldBankAccount.getBankAccountNumber().equals(bankAccount.getBankAccountNumber())){
				logger.error("尝试创建的帐号的银行帐号数据[" + bankAccount.getBankAccountNumber() + "]与相同用户名、相同银行的已存在帐号有对应的帐号数据[" + _oldBankAccount + "]不一致");
				return EisError.ACCOUNT_ERROR.id;
			}
		}
		logger.debug("根据条件[uuid=" + bankAccountCriteria.getUuid() + ",bankName=" + bankAccountCriteria.getBankName() + ",bankAccountName=" + bankAccountCriteria.getBankAccountName() + "]得到的帐号数量是1[" + _oldBankAccount + "]不再新增");
		BeanUtils.copyProperties(_oldBankAccount, bankAccount);
		return 0;
	}


}
