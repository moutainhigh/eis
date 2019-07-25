package com.maicard.site.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.billing.criteria.ShareConfigCriteria;
import com.maicard.billing.domain.ShareConfig;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CacheService;
import com.maicard.money.domain.Money;
import com.maicard.money.service.MoneyService;
import com.maicard.site.criteria.*;
import com.maicard.site.dao.*;
import com.maicard.site.domain.Advert;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.service.AdvertService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;


@Service
public class AdvertServiceImpl extends BaseService implements AdvertService {

	@Resource
	private AdvertDao advertDao;

	@Resource
	private CacheService cacheService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private ShareConfigService shareConfigService;

	private static String cacheName = CommonStandard.cacheNameDocument;

	public int insert(Advert advert) {
		int rs = 0;
		try{
			rs = advertDao.insert(advert);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		if(rs != 1){
			logger.error("新增广告失败,数据操作未返回1");
			return -1;
		}

		return 1;
	}

	public int update(Advert advert) {
		int actualRowsAffected = 0;

		int advertId = advert.getAdvertId();

		Advert _oldAdvert = advertDao.select(advertId);

		if (_oldAdvert == null) {
			return 0;
		}
		try{
			actualRowsAffected = advertDao.update(advert);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());

		}
		return actualRowsAffected;
	}

	public int delete(int advertId) {
		int actualRowsAffected = 0;

		Advert _oldAdvert = advertDao.select(advertId);

		if (_oldAdvert != null) {
			actualRowsAffected = advertDao.delete(advertId);
		}
		return actualRowsAffected;
	}


	public Advert select(int advertId){
		return advertDao.select(advertId);
	}

	public List<Advert> list(AdvertCriteria advertCriteria) {
		List<Integer> idList = advertDao.listPk(advertCriteria);
		if(idList != null && idList.size() > 0){
			List<Advert> advertList =  new ArrayList<Advert> ();		
			for(Integer id : idList){
				Advert advert = advertDao.select(id);
				if(advert != null){
					advertList.add(advert);
				}
			}
			idList = null;
			return advertList;
		}
		return null;
		/*
		List<Advert> advertList = advertDao.list(advertCriteria);
		if(advertList == null){
			return null;
		}
		for(int i = 0; i < advertList.size(); i ++){
			advertList.get(i).setIndex(i+1);		
			afterFetch(advertList.get(i));
		}
		return advertList;
		 */
	}

	public List<Advert> listOnPage(AdvertCriteria advertCriteria) {
		List<Integer> idList = advertDao.listPkOnPage(advertCriteria);
		if(idList != null && idList.size() > 0){
			List<Advert> advertList =  new ArrayList<Advert> ();		
			for(Integer id : idList){
				Advert advert = advertDao.select(id);
				if(advert != null){
					advertList.add(advert);
				}
			}
			idList = null;
			return advertList;
		}
		return null;
		/*
		List<Advert> advertList = advertDao.listOnPage(advertCriteria);
		if(advertList == null){
			return null;
		}
		for(int i = 0; i < advertList.size(); i ++){
			advertList.get(i).setIndex(i+1);
			afterFetch(advertList.get(i));
		}
		return advertList;
		 */
	}

	public int count(AdvertCriteria advertCriteria){
		return advertDao.count(advertCriteria);
	}

	//广告展示了一次，
	@Override
	public void addShow(Advert advert) {
		Advert _oldAdvert = advertDao.select(advert.getAdvertId());
		if(_oldAdvert == null){
			logger.error("找不到需要添加播放次数的广告[" + advert.getAdvertId() + "]");
			return;
		}
		int oldCount = _oldAdvert.getShowCount();
		_oldAdvert.setShowCount(_oldAdvert.getShowCount()+1);
		//写入缓存
		String cacheKey = "Advert#" + _oldAdvert.getAdvertId();
		cacheService.put(cacheName, cacheKey, _oldAdvert);
		logger.info("将产品服务器的活跃度从" + oldCount + "更新到" + _oldAdvert.getShowCount());

	}

	//广告主资金的扣除，和观看者资金的增加（如果需要增加）
	public void prcessMoney(Advert advert, long readUuid){
		if(advert == null){
			logger.error("尝试处理的广告为空");
			return;
		}
		if(advert.getPublisherId() <= 0){
			logger.error("尝试处理的广告[" + advert.getAdvertId() + "]发布商ID异常:" + advert.getPublisherId());
			return;
		}
		//查找广告对应的广告主资金配置
		ShareConfigCriteria shareConfigCriteria = new ShareConfigCriteria();
		shareConfigCriteria.setObjectType(ObjectType.advert.name());
		shareConfigCriteria.setObjectId(advert.getAdvertId());
		shareConfigCriteria.setShareUuid(advert.getPublisherId());
		shareConfigCriteria.setMode(Operate.minus.name());
		ShareConfig shareConfig = shareConfigService.calculateShare(shareConfigCriteria);
		if(shareConfig == null){
			logger.warn("找不到对象ID是[" + advert.getAdvertId() + "]的advert对象分成配置");
			return;
		}
		if(shareConfig.getSharePercent() <= 0){
			logger.warn("针对[" + advert.getAdvertId() + "]的advert对象分成价格是:" + shareConfig.getSharePercent() + "]，不进行处理");
			return;
		}
		//扣除广告主对应的冻结金额
		Money money = new Money();
		money.setUuid(advert.getPublisherId());
		money.setFrozenMoney(shareConfig.getSharePercent());
		EisMessage result = moneyService.minus(money);
		if(result != null &&( result.getOperateCode() == OperateResult.success.getId() || result.getOperateCode() == OperateResult.accept.getId())){
			logger.info("扣除广告主[" + advert.getPublisherId() + "]针对广告[" + advert.getAdvertId() + "]的资金[" + shareConfig.getSharePercent() + "]成功");
		} else {
			logger.error("扣除广告主[" + advert.getPublisherId() + "]针对广告[" + advert.getAdvertId() + "]的资金[" + shareConfig.getSharePercent() + "]失败");
			return;
		}

		//处理点击广告的观看者资金
		shareConfigCriteria = new ShareConfigCriteria();
		shareConfigCriteria.setObjectType(ObjectType.advert.name());
		shareConfigCriteria.setObjectId(advert.getAdvertId());
		shareConfigCriteria.setMoneyDirect(Operate.plus.name());
		shareConfig = shareConfigService.calculateShare(shareConfigCriteria);
		if(shareConfig == null){
			logger.warn("找不到对象ID是[" + advert.getAdvertId() + "]、模式为plus的advert对象分成配置");
			return;
		}
		if(shareConfig.getSharePercent() <= 0){
			logger.warn("针对[" + advert.getAdvertId() + "]、模式为plus的advert对象分成价格是:" + shareConfig.getSharePercent() + "]，不进行处理");
			return;
		}
		//为阅读广告的用户增加对应的资金
		money = new Money();
		money.setUuid(readUuid);
		String moneyType = null;
		if(shareConfig.getShareType().equals(MoneyType.incomingMoney.name())){
			money.setIncomingMoney(shareConfig.getSharePercent());
			moneyType = MoneyType.incomingMoney.getName();
		} else {
			money.setCoin(shareConfig.getSharePercent());
			moneyType = MoneyType.coin.getName();
		}
		result = moneyService.plus(money);
		if(result != null &&( result.getOperateCode() == OperateResult.success.getId() || result.getOperateCode() == OperateResult.accept.getId())){
			logger.info("增加阅读者[" +readUuid + "]针对广告[" + advert.getAdvertId() + "]的" + moneyType + "[" + shareConfig.getSharePercent() + "]成功");
		} else {
			logger.error("增加阅读者[" +readUuid + "]针对广告[" + advert.getAdvertId() + "]的" + moneyType + "[" + shareConfig.getSharePercent() + "]失败");
		}
	}

	@Override
	public void applyAdvertData(Document document) {
		if(document == null){
			logger.error("尝试应用广告数据的文档是空");
		}
		if(document.getDocumentDataMap() == null){
			logger.error("尝试应用广告数据的文档扩展数据是空");
		}
		int advertId = 0;
		try{
			advertId = Integer.parseInt(document.getDocumentDataMap().get(DataName.advertId.toString()).getDataValue());
		}catch(Exception e){
			logger.warn("找不到文章[" + document.getUdid() + "]的扩展数据advertId");
		}
		if(advertId == 0){
			logger.info("文章[" + document.getUdid() + "]没有关联广告ID，不是广告");
		} else {
			Advert advert = select(advertId);
			if(advert == null){
				logger.error("找不到文档[" + document.getUdid() + "]所对应的广告[" + advertId + "]");
				return;	
			}
			DocumentData dd1 = new DocumentData();
			dd1.setDataCode(DataName.pointPerRead.toString());
			dd1.setDataValue("" + advert.getPointPerRead());		
			document.getDocumentDataMap().put(DataName.pointPerRead.toString(), dd1);

			DocumentData dd2 = new DocumentData();
			dd2.setDataCode(DataName.maxReadCount.toString());
			dd2.setDataValue("" + advert.getMaxShowCount());		
			document.getDocumentDataMap().put(DataName.maxReadCount.toString(), dd2);

			DocumentData dd3 = new DocumentData();
			dd3.setDataCode(DataName.readCount.toString());
			dd3.setDataValue("" + advert.getShowCount());		
			document.getDocumentDataMap().put(DataName.readCount.toString(), dd3);
		}
	}
	
	public void flushAdvertCache(){
		List<String> keys = cacheService.listKeys(cacheName, null);
		if(keys == null || keys.size() < 1){
			if(logger.isDebugEnabled()){
				logger.debug("尝试刷新的广告数据缓存为空");
			}
			return;
		}
		if(logger.isDebugEnabled()){
			logger.debug("当前广告缓存共有[" + keys.size() + "]条");
		}
		for(String key : keys){
			if(!key.startsWith("Advert#")){
				continue;
			}

			logger.info("尝试刷新产品数据[" + key  + "]");

			Advert advert = cacheService.get(cacheName, key);
			
			if(advert != null){
				if(advert.getShowCount() <= 0){
					logger.info("广告的观看数量为0，不更新");

					continue;
				}
				logger.debug("更新广告[" + advert.getAdvertId() + "]的观看数" + advert.getShowCount() + "到数据库");
				advertDao.update(advert);
			}


		}
	}


}
