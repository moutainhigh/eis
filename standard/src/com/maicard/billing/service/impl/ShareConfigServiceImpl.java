package com.maicard.billing.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.dao.ShareConfigDao;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.mb.service.MessageService;
import com.maicard.product.domain.Item;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;

@Service
public class ShareConfigServiceImpl extends BaseService implements ShareConfigService {

	@Resource
	private ShareConfigDao shareConfigDao;
	
	@Resource
	private MessageService messageService;

	public int insert(ShareConfig shareConfig) {
		try{
			return shareConfigDao.insert(shareConfig);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	public int update(ShareConfig shareConfig) {
		if(shareConfig.getSyncFlag() == 0) {
			shareConfig.incrVersion();
		}
		try{
			return  shareConfigDao.update(shareConfig);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}
	
	public int updateNoNull(ShareConfig shareConfig) {
		if(shareConfig.getSyncFlag() == 0) {
			shareConfig.incrVersion();
		}
		try{
			return  shareConfigDao.updateNoNull(shareConfig);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;
	}
	
	public int delete(int shareConfigId) {
		try{
			return  shareConfigDao.delete(shareConfigId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;

	}

	public ShareConfig select(int shareConfigId) {
		return shareConfigDao.select(shareConfigId);
	}


	public List<ShareConfig> list(ShareConfigCriteria shareConfigCriteria) {
		return shareConfigDao.list(shareConfigCriteria);
	}

	public List<ShareConfig> listOnPage(ShareConfigCriteria shareConfigCriteria) {

		List<ShareConfig> shareConfigList =   shareConfigDao.listOnPage(shareConfigCriteria);
		if(shareConfigList == null || shareConfigList.size() < 1){
			return Collections.emptyList();
		}
		for(int i = 0; i < shareConfigList.size(); i++){

			shareConfigList.get(i).setId(shareConfigList.get(i).getShareConfigId());
			shareConfigList.get(i).setIndex(i+1);
		}
		return shareConfigList;


	}

	public int count(ShareConfigCriteria shareConfigCriteria) {
		return shareConfigDao.count(shareConfigCriteria);
	}

	@Override
	//根据UUID和产品ID计算应得的分成比例
	public ShareConfig calculateShare(ShareConfigCriteria shareConfigCriteria) {
		Assert.notNull(shareConfigCriteria,"尝试计算分成比例的shareConfigCriteria不能为空");
		Assert.notNull(shareConfigCriteria.getShareUuid() != 0,"尝试计算分成比例的shareConfigCriteria，其shareUuid不能为0");
		List<ShareConfig> shareConfigList = list(shareConfigCriteria);
		if(shareConfigList != null && shareConfigList.size() > 0){
			return shareConfigList.get(0);
		}
		return null;
	}

	/*
	@Override
	public float calculateShare(Item item) {
		ShareConfig shareConfig = null;

		if(item.getShareConfigId() == 0){
			ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();
			shareConfigCriteria.setShareUuid(item.getChargeFromAccount());
			shareConfigCriteria.setObjectType(ObjectType.product.name());
			shareConfigCriteria.setObjectId(item.getProductId());
			shareConfigCriteria.setTtl(item.getTtl());
			shareConfigCriteria.setCurrentStatus(BasicStatus.normal.getId());
			try{
				shareConfig = calculateShare(shareConfigCriteria);
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			shareConfig = select(item.getShareConfigId());
		}
		float incomingMoney = item.getSuccessMoney();
		if(shareConfig == null){
			logger.debug("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + ",shareConfigId=" + item.getShareConfigId() + "]找不到对应的的分成配置" );
			incomingMoney = 0;
			return incomingMoney;
		} 
		if(shareConfig.getSharePercent() > 1){
			logger.warn("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + "]的分成比例异常:" + shareConfig.getSharePercent() );
			incomingMoney = 0;
			return incomingMoney;
		}

		logger.info("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + "]的分成比例是:" + shareConfig.getSharePercent() );
		incomingMoney *= shareConfig.getSharePercent();
		if(shareConfig.getMoneyDirect() != null && shareConfig.getMoneyDirect().equals(ShareConfigCriteria.MINUS)){
			incomingMoney = -incomingMoney;
		}
		logger.debug("交易[" + item.getTransactionId() + "]实际成功金额[" + item.getSuccessMoney() + "],经过计算，用户收入金额:" + incomingMoney);
		item.setInMoney(incomingMoney);

		return incomingMoney;


	}*/
	
	@Override
	public float calculateFrozenMoney(Item item) {
		ShareConfig shareConfig = null;

		if(item.getShareConfigId() == 0){
			ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();
			shareConfigCriteria.setShareUuid(item.getChargeFromAccount());
			shareConfigCriteria.setObjectType(ObjectType.product.name());
			shareConfigCriteria.setObjectId(item.getProductId());
			shareConfigCriteria.setTtl(item.getTtl());
			shareConfigCriteria.setCurrentStatus(BasicStatus.normal.getId());
			try{
				shareConfig = calculateShare(shareConfigCriteria);
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			shareConfig = select(item.getShareConfigId());
		}
		float frozenMoney = item.getLabelMoney();
		if(shareConfig == null){
			logger.debug("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + ",shareConfigId=" + item.getShareConfigId() + "]找不到对应的的分成配置" );
			frozenMoney = 0;
			return frozenMoney;
		} 

		logger.info("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + "]的分成比例是:" + shareConfig.getSharePercent() );
		if(shareConfig.getSharePercent() > 1){
			logger.warn("当前用户[" + item.getChargeFromAccount() + "]针对交易[" + item.getTransactionId() + "/" + item.getProductId() + "]的分成比例异常:" + shareConfig.getSharePercent() );
			frozenMoney = 0;
			return frozenMoney;
		}
		frozenMoney *= shareConfig.getSharePercent();
		/*if(shareConfig.getMoneyDirect() != null && shareConfig.getMoneyDirect().equals(ShareConfigCriteria.MINUS)){
			frozenMoney = -frozenMoney;
		}*/
		logger.debug("交易[" + item.getTransactionId() + "]应扣除金额:" + frozenMoney);
		return frozenMoney;


	}

	@Override
	public EisMessage clone(ShareConfigCriteria shareConfigCriteria) {
		
		ShareConfigCriteria shareConfigCriteria2 = shareConfigCriteria.clone();
		shareConfigCriteria2.setShareUuid(shareConfigCriteria2.getOwnerId());
		List<ShareConfig> shareConfigList = this.list(shareConfigCriteria2);
		if(shareConfigList == null || shareConfigList.size() < 1){
			logger.error("无法克隆分成配置，因为获取[shareUuid=ownerId=" + shareConfigCriteria2.getShareUuid() + "]的分成配置为空");
			return new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到系统分成配置");
		}

		List<ShareConfig> newShareConfigList = new ArrayList<ShareConfig>();
		
		logger.info("uuid=ownerId=" + shareConfigCriteria2.getShareUuid() + "的分成配置数量有" + shareConfigList.size() + "条");
		//为确保更新，先删除该用户所有分成
		this.deleteByUuid(shareConfigCriteria.getShareUuid());
		for(ShareConfig oldShareConfig : shareConfigList){
			
			ShareConfig newShareConfig = oldShareConfig.clone();

				newShareConfig.setShareConfigId(0);
				newShareConfig.setShareUuid(shareConfigCriteria.getShareUuid());
				
				
				int rs = this.insert(newShareConfig);
				logger.info("从分成配置[" + oldShareConfig + "]新克隆了分成配置[" + newShareConfig + "],数据插入结果:" + rs);
				if(rs != 1){
					logger.error("无法创建新菜单[" + newShareConfig + "]，返回是:" + rs);
					return new EisMessage(EisError.DATA_UPDATE_FAIL.id,"无法新增分成配置");
				} else {
					messageService.sendJmsDataSyncMessage(null, "shareConfigService", "insert", newShareConfig);
				}

		}
		
		EisMessage resultMsg = new EisMessage(OperateResult.success.id,"克隆完成");
		resultMsg.setAttachmentData("shareConfigList", newShareConfigList);
		return resultMsg;	}

	private void deleteByUuid(long shareUuid) {
		shareConfigDao.deleteByUuid(shareUuid);
	}

	@Override
	public ShareConfig calculateShare(Object obj, ShareConfigCriteria shareConfigCriteria) {
		logger.warn("标准实现不支持本方法，直接使用不带第一个参数的方法: calculateShare(shareConfigCriteria)");
		return calculateShare(shareConfigCriteria);
	}

}

