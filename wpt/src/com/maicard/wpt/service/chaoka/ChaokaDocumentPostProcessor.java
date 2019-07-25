package com.maicard.wpt.service.chaoka;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.util.JsonUtils;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.site.domain.Document;
import com.maicard.site.service.DocumentPostProcessor;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.PriceType;

@Service
public class ChaokaDocumentPostProcessor extends BaseService implements DocumentPostProcessor{

	@Resource
	private PriceService priceService;

	@Override
	public int process(Document document, String mode) throws Exception {

		if(document.getDocumentTypeId() != 171007 && document.getDocumentTypeId() != 171008){
			logger.info("文档:{}类型是:{},不支持对该类型的文档进行后期处理", document.getUdid(), document.getDocumentTypeId());
			return 0;
		}
		if(!(document.getParam() instanceof HttpServletRequest)) {
			logger.error("需要的参数不是HttpServletRequest,而是:" + (document.getParam() == null ? "空" : document.getParam().getClass().getName()));
			throw new Exception("处理文档时提供了错误的参数类型");
		}
		if(mode.equalsIgnoreCase(Operate.delete.name())) {
			//删除
			PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
			priceCriteria.setObjectType(ObjectType.document.name());
			priceCriteria.setObjectId(document.getUdid());
			List<Price> priceList = priceService.list(priceCriteria);
			logger.info("文档:{}被删除，删除其对应的{}条价格", document.getUdid(), priceList.size());
			return 0;
		}
		HttpServletRequest request = (HttpServletRequest)document.getParam();

		float buyMoney = ServletRequestUtils.getFloatParameter(request, DataName.productBuyMoney.name(), 0);
		Price price = new Price();
		price.setPriceType(PriceType.PRICE_STANDARD.name());
		price.setObjectType(ObjectType.document.name());
		price.setObjectId(document.getUdid());
		price.setOwnerId(document.getOwnerId());
		price.setCurrentStatus(BasicStatus.normal.getId());
		price.setMoney(buyMoney);
		if(mode.equalsIgnoreCase(Operate.create.name())) {
			priceService.insert(price);
		} else {
			PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
			priceCriteria.setObjectType(ObjectType.document.name());
			priceCriteria.setObjectId(document.getUdid());
			priceCriteria.setPriceType(PriceType.PRICE_STANDARD.name());
			List<Price> priceList = priceService.list(priceCriteria);
			if(priceList.size() > 1) {
				String msg = "根据条件:" + JsonUtils.toStringFull(priceCriteria) + "得到的价格不唯一";
				logger.error(msg);
				throw new Exception(msg);
			}
			if(priceList.size() == 1) {
				price = priceList.get(0);
				price.setMoney(buyMoney);
				priceService.update(price);
			} else {
				priceService.insert(price);
			}
		}
		float saleMoney = ServletRequestUtils.getFloatParameter(request, DataName.productSaleMoney.name(), 0);
		Price price2 = new Price();
		price2.setPriceType(PriceType.PRICE_SALE.name());
		price2.setObjectType(ObjectType.document.name());
		price2.setObjectId(document.getUdid());
		price2.setOwnerId(document.getOwnerId());
		price2.setCurrentStatus(BasicStatus.normal.getId());
		price2.setMoney(saleMoney);
		if(mode.equalsIgnoreCase(Operate.create.name())) {
			priceService.insert(price2);
		} else {
			PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
			priceCriteria.setObjectType(ObjectType.document.name());
			priceCriteria.setObjectId(document.getUdid());
			priceCriteria.setPriceType(PriceType.PRICE_SALE.name());
			List<Price> priceList = priceService.list(priceCriteria);
			if(priceList.size() > 1) {
				String msg = "根据条件:" + JsonUtils.toStringFull(priceCriteria) + "得到的价格不唯一";
				logger.error(msg);
				throw new Exception(msg);
			}
			if(priceList.size() == 1) {
				price2 = priceList.get(0);
				price2.setMoney(buyMoney);
				priceService.update(price2);
			} else {
				priceService.insert(price2);
			}
		}


		document.setExtraValue(DataName.productBuyMoney.name(), null);
		document.setExtraValue(DataName.productSaleMoney.name(), null);

		return 0;
	}

	@Override
	public void asyncProcess(Document document, String mode) {
		logger.warn(this.getClass().getSimpleName() + "暂不支持异步处理");
	}

}
