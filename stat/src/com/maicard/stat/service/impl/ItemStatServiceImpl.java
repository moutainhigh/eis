package com.maicard.stat.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.domain.Item;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.stat.criteria.ItemStatCriteria;
import com.maicard.stat.dao.ItemStatDao;
import com.maicard.stat.domain.ItemStat;
import com.maicard.stat.service.ItemStatService;

@Service
public class ItemStatServiceImpl extends BaseService implements ItemStatService {
	@Resource
	private ItemStatDao itemStatDao;

	@Resource
	private PartnerService partnerService;

	public List<ItemStat> listOnPage(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria == null){
			return null;
		}
		List<ItemStat> itemStatList = itemStatDao.listOnPage(statCriteria);	
		if(itemStatList != null && itemStatList.size() >0 ){
			for(int i = 0; i < itemStatList.size(); i++){
				itemStatList.get(i).setIndex(i+1);
			}
		}
		return itemStatList;

	}
	@Override
	public List<ItemStat> listByInviter(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria == null){
			return null;
		}
		List<ItemStat> itemStatList = itemStatDao.listByInviter(statCriteria);	
		if(itemStatList != null && itemStatList.size() >0 ){
			for(int i = 0; i < itemStatList.size(); i++){
				itemStatList.get(i).setIndex(i+1);
			}
		}
		return itemStatList;

	}	

	@Override
	public int countByInviter(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals(""))
			statCriteria.setBeginTime(  new SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addWeeks(new Date(),-1)));		
		
		return itemStatDao.countByInviter(statCriteria);
	}

	@Override
	public int gather(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria == null){
			logger.error("数据采集时未提供条件");
			return -1;
		}	
		return itemStatDao.gather(statCriteria);
		
	}

   public int change_Inviter(ItemStatCriteria statCriteria) throws Exception {
	   if (statCriteria ==null){
		   return -1;
	   }
		return itemStatDao.change_Inviter(statCriteria);    
   }
	@Override
	public void moveToHistory(ItemCriteria itemCriteria) throws Exception {
		itemStatDao.moveToHistory(itemCriteria);
	}
	@Override
	public int count(ItemStatCriteria statCriteria) throws Exception {
		if(statCriteria.getBeginTime() == null || statCriteria.getBeginTime().equals(""))
			statCriteria.setBeginTime(  new SimpleDateFormat(CommonStandard.statDayFormat).format(DateUtils.addWeeks(new Date(),-1)));		
		
		return itemStatDao.count(statCriteria);
	}
	@Override
	public float getJWCharge() {
		return itemStatDao.getJWCharge();
	}
	@Override
	public List<Item> getJWShengdaTakeCard(ItemStatCriteria statCriteria) {
		return itemStatDao.getJWShengdaTakeCard(statCriteria);
	}

}
