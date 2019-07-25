package com.maicard.ec.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.ec.criteria.DeliveryPriceCriteria;
import com.maicard.ec.dao.DeliveryPriceDao;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.domain.DeliveryPrice;
import com.maicard.ec.service.DeliveryPriceService;
import com.maicard.ec.utils.AreaTrimUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.domain.Price;
import com.maicard.standard.BasicStatus;
import com.maicard.common.base.BaseService;
import com.maicard.common.util.NumericUtils;

@Service
public class DeliveryPriceServiceImpl  extends BaseService implements DeliveryPriceService{
	
	@Resource
	private MessageService messageService;

	@Resource
	private DeliveryPriceDao deliveryPriceDao;
	
	
	@Override
	public DeliveryPrice select(long deliveryPriceId) {
		return deliveryPriceDao.select(deliveryPriceId);
	}

	@Override
	public int insert(DeliveryPrice deliveryPrice) {
		return deliveryPriceDao.insert(deliveryPrice);
	}

	@Override
	public int update(DeliveryPrice deliveryPrice) {
		return deliveryPriceDao.update(deliveryPrice);
	}

	@Override
	public int delete(long deliveryPriceId) {
		return deliveryPriceDao.delete(deliveryPriceId);
	}

	@Override
	public List<DeliveryPrice> list(DeliveryPriceCriteria deliveryPriceCriteria) {
		return deliveryPriceDao.list(deliveryPriceCriteria);
	}

	@Override
	public int count(DeliveryPriceCriteria deliveryPriceCriteria) {
		return deliveryPriceDao.count(deliveryPriceCriteria);
	}

	@Override
	public List<DeliveryPrice> listOnPage(DeliveryPriceCriteria deliveryPriceCriteria) {
		return deliveryPriceDao.listOnPage(deliveryPriceCriteria);
	}

	@Override
	public Price calculatePrice(DeliveryOrder deliveryOrder) {
		Assert.notNull(deliveryOrder,"尝试计算邮费的配送单不能为空");
		Assert.notNull(deliveryOrder.getToArea(),"尝试计算邮费的配送单目的地不能为空");
		Assert.notNull(deliveryOrder.getFromArea(),"尝试计算邮费的配送单起始地不能为空");
		
		String fromProvince = deliveryOrder.getFromProvince().replaceAll("省$", "");
		String fromArea = deliveryOrder.getFromArea().replaceAll("市$", "");
		String toProvince = deliveryOrder.getToProvince().replaceAll("省$", "");
		String toArea = deliveryOrder.getToArea().replaceAll("市$", "");
		
		DeliveryPriceCriteria deliveryPriceCriteria = new DeliveryPriceCriteria();
		deliveryPriceCriteria.setOwnerId(deliveryOrder.getOwnerId());
		deliveryPriceCriteria.setFromProvince(fromProvince);
		deliveryPriceCriteria.setFromArea(fromArea);
		deliveryPriceCriteria.setToProvince(toProvince);
		deliveryPriceCriteria.setToArea(toArea);
		deliveryPriceCriteria.setDeliveryCompanyId(deliveryOrder.getDeliveryCompanyId());
		deliveryPriceCriteria.setIdentify(deliveryOrder.getIdentify());
		List<DeliveryPrice> deliveryPriceList = list(deliveryPriceCriteria);
		if(deliveryPriceList == null || deliveryPriceList.size() < 1){
			logger.info("根据条件[fromArea=" + fromArea + "=>" +  toArea + ",identify=" + deliveryPriceCriteria.getIdentify() + ",deliveryCompanyId=" + deliveryPriceCriteria.getDeliveryCompanyId() + "]找不到任何配送价格");
			deliveryPriceCriteria.setIdentify(null);
			deliveryPriceList = list(deliveryPriceCriteria);
			if(deliveryPriceList == null || deliveryPriceList.size() < 1){
				logger.info("根据条件[fromArea=" + deliveryPriceCriteria.getFromArea() + "=>" +  deliveryPriceCriteria.getToArea() + ",identify=" + deliveryPriceCriteria.getIdentify() + ",deliveryCompanyId=" + deliveryPriceCriteria.getDeliveryCompanyId() + "]找不到任何配送价格");
				return null;
			} 
		}
		
		Price price = new Price();
		DeliveryPrice deliveryPrice = deliveryPriceList.get(0);
		
		float basicPrice = 0;
		float addPrice = 0;
		//基础价格
		
		if(deliveryOrder.getGoodsWeight() > deliveryPrice.getBasePrice() && deliveryPrice.getAdditinalPrice() > 0){
			addPrice = deliveryPrice.getAdditinalPrice() * (deliveryOrder.getGoodsWeight() - deliveryPrice.getBaseWeight()) / deliveryPrice.getAdditinalWeightUnit();
			logger.debug("当前配送价格配置了进阶重量，需要计算进阶价格,公式:" + deliveryPrice.getAdditinalPrice() + " * " + "(" + deliveryOrder.getGoodsWeight() + " - " +  deliveryPrice.getBaseWeight() + ") / " + deliveryPrice.getAdditinalWeightUnit() + ",计算结果:" + addPrice);
					
		}
		int basicGoodsWeight = 0;
		if(addPrice > 0){

			basicGoodsWeight = deliveryPrice.getBaseWeight();
			logger.debug("当前配送订单计算了进阶价格:" +  addPrice + ",基础价格计算重量是价格中的基础部分:" + basicGoodsWeight);
		}	 else {
			basicGoodsWeight = (deliveryOrder.getGoodsWeight() < 1 ? 1 :deliveryOrder.getGoodsWeight());
			logger.debug("当前配送订单未计算出进阶价格,基础价格计算重量是配送订单的总重量:" + basicGoodsWeight);
		}

		//计算基础部分的价格
		basicPrice = deliveryPrice.getBasePrice() * basicGoodsWeight;
		logger.debug("当前配送单的基础价格是:基础价格 " + deliveryPrice.getBasePrice() + " X 基本重量 " + basicGoodsWeight);
//		logger.debug("当前配送计算价格是:基础价格"  + basicPrice + " + 进阶价格 " + price.getMoney() + " = " + (price.getMoney() + basicPrice));
		logger.debug("当前配送计算价格是:基础价格"  + basicPrice + " + 进阶价格 " + addPrice + " = " + (price.getMoney() + basicPrice));
		price.setMoney(price.getMoney() + basicPrice);
		return price;
	}

	@Override
	public int loadBatch(List<String> lines, long deliveryPartnerId, String identify, long ownerId) {
		int count = 0;
		for(String line : lines){
			if(StringUtils.isBlank(line)){
				logger.debug("忽略导入的空行文件");
				continue;
			}
			if(line.trim().startsWith("#")){
				logger.debug("忽略以#开头的内容");
				continue;
			}
			DeliveryPrice deliveryPrice = new DeliveryPrice(ownerId);
			String[] data = line.split(",");
			if(data.length < 8){
				logger.error("数据字段不足8个,无法解析");
				return -1;
			}
			logger.debug("准备解析数据:" + line);
			String fromProvince = data[0].trim();
			String fromArea = null;
			if(StringUtils.isNotBlank(data[1])){
				fromArea = data[1].trim();
			} else {
				fromArea = "*";
			}
			String toProvince = data[2].trim();
			
			String toArea = null;
			if(StringUtils.isNotBlank(data[3])){
				toArea = data[3].trim();
			} else {
				toArea = "*";
			}
			float basePrice = Float.parseFloat(data[4].trim());
			int baseWeight = Integer.parseInt(data[5].trim());
			float addPrice = 0;
			if(NumericUtils.isFloatNumber(data[6])){
				addPrice = Float.parseFloat(data[6]);
			}
			int addWeightUnit = 0;
			if(NumericUtils.isIntNumber(data[7])){
				addWeightUnit = Integer.parseInt(data[7]);
			}
			deliveryPrice.setDeliveryCompanyId(deliveryPartnerId);
			deliveryPrice.setCurrentStatus(BasicStatus.normal.getId());
			deliveryPrice.setIdentify(identify);
			
			deliveryPrice.setFromProvince(AreaTrimUtils.trimArea(fromProvince));
			deliveryPrice.setFromArea(AreaTrimUtils.trimArea(fromArea));
			deliveryPrice.setToProvince(AreaTrimUtils.trimArea(toProvince));
			deliveryPrice.setToArea(AreaTrimUtils.trimArea(toArea));
			deliveryPrice.setBasePrice(basePrice);
			deliveryPrice.setBaseWeight(baseWeight);
			deliveryPrice.setAdditinalPrice(addPrice);
			deliveryPrice.setAdditinalWeightUnit(addWeightUnit);
			
			int rs = insert(deliveryPrice);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "deliveryPriceService", "insert", deliveryPrice);
				count++;
			} else {
				logger.error("无法新增数据:" + line);
			}			
		}
		
		return count;
		
	}





}
