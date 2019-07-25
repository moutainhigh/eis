package com.maicard.money.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.ServletRequestUtils;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EOEisObject;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.Crypt;
import com.maicard.common.util.CryptKeyUtils;
import com.maicard.common.util.NumericUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.dao.PriceDao;
import com.maicard.money.domain.Price;
import com.maicard.money.service.PriceService;
import com.maicard.product.domain.Item;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;

@Service
public class PriceServiceImpl extends BaseService implements PriceService {

	@Resource
	private PriceDao priceDao;

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;
	
	final DecimalFormat defaultMoneyFormat = new DecimalFormat("0.##");

	private String aesKey = null;
	private int priceTtl = 1800;
	private final SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@PostConstruct
	public void init(){
		try {
			aesKey = CryptKeyUtils.readAesKey();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}

	}

	
	@Override
	public List<Price> bindPrice(HttpServletRequest request, EOEisObject targetObject) {
		
		String availablePriceType = configService.getValue(DataName.availablePriceType.name(), targetObject.getOwnerId());
		if(StringUtils.isBlank(availablePriceType)) {
			availablePriceType = PriceType.PRICE_STANDARD.name();
		}
		
		List<Price> priceList = new ArrayList<Price>();
		float marketPrice = ServletRequestUtils.getFloatParameter(request, "marketPrice", 0);
		logger.info("当前获取到的marketPrice={}", marketPrice);
		String[] priceTypeList =  availablePriceType.split(",");
		for(String priceType : priceTypeList) {
			Price price = new Price(priceType);
			price.setMarketPrice(marketPrice);
			float money = ServletRequestUtils.getFloatParameter(request, priceType + ".money", 0f);
			if(money > 0) {
				price.setMoney(money);
			}
			float coin = ServletRequestUtils.getFloatParameter(request, priceType + ".coin", 0f);
			if(coin > 0) {
				price.setCoin(coin);
			}
			float point = ServletRequestUtils.getFloatParameter(request, priceType + ".point", 0f);
			if(point > 0) {
				price.setPoint(point);
			}
			long score = ServletRequestUtils.getLongParameter(request, priceType + ".score", 0);
			if(score > 0) {
				price.setScore(score);
			}
			if(!price.isZero()) {
				priceList.add(price);			
			}
		}

		return priceList;
	}		

	public int insert(Price price) {
		if(price == null){
			logger.warn("尝试插入的价格是空");
			return -1;
		}
		if(price.getObjectType() == null){
			logger.warn("尝试插入的价格对象类型是空");
			return -1;
		}
		if(price.getObjectId() < 1){
			logger.warn("尝试插入的价格对象ID是0");
			return -1;
		}
		if(price.getPriceType() == null){
			logger.warn("尝试插入的价格类型是空");
			return -1;
		}
		if(price.getMoney() < 0 && price.getCoin() < 0 && price.getPoint() < 0 && price.getScore() < 0 && price.getMarketPrice() < 0){
			logger.warn("尝试插入的价格，没有至少一个价格值:" + price);
			return -1;
		}
		try{
			return priceDao.insert(price);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Price#' + #price.priceId")
	public int update(Price price) {
		if(price == null){
			logger.warn("尝试更新的价格是空");
			return -1;
		}
		if(price.getPriceId() < 1){
			logger.warn("尝试更新的价格ID是0");
			return -1;
		}	

		if(price.getObjectType() == null){
			logger.warn("尝试更新的价格对象类型是空");
			return -1;
		}
		if(price.getObjectId() < 1){
			logger.warn("尝试更新的价格对象ID是0");
			return -1;
		}
		if(price.getPriceType() == null){
			logger.warn("尝试更新的价格类型是空");
			return -1;
		}
		if(price.getMoney() <= 0 && price.getCoin() <= 0 && price.getPoint() <= 0 && price.getScore() <= 0){
			logger.warn("尝试更新的价格，没有至少一个价格值:" + price);
			return -1;
		}
		try{
			return  priceDao.update(price);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'Price#' + #priceId")
	public int delete(long priceId) {
		try{
			return  priceDao.delete(priceId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'Price#' + #priceId")
	public Price select(long priceId) {
		return priceDao.select(priceId);
	}

	public List<Price> list(PriceCriteria priceCriteria) {
		return priceDao.list(priceCriteria);
	}

	public List<Price> listOnPage(PriceCriteria priceCriteria) {
		return priceDao.listOnPage(priceCriteria);
	}

	public int count(PriceCriteria priceCriteria) {
		return priceDao.count(priceCriteria);
	}



	@Override
	public int applyPrice(Item item, Price price){
		if(price.getMoney() <= 0 && price.getCoin() <= 0 && price.getPoint() <= 0 && price.getScore() <= 0){
			logger.warn("价格[" + price + "]中的所有资金都是0");
			//return EisError.moneyRangeError.getId();
		}
		if(item.getCount() < 1){
			logger.error("当前交易[" + item.getTransactionId() + "]的购买数量是0");
			return EisError.countIsZero.getId();
		}
		item.setLabelMoney(price.getMoney());
		item.setRequestMoney(price.getMoney() * item.getCount());
		item.setFrozenMoney(price.getCoin()* item.getCount());
		item.setSuccessMoney(price.getPoint()* item.getCount());
		item.setInMoney(price.getScore()* item.getCount());
		item.setBillingStatus((int)price.getPriceId());
		Price p = price.clone();
		Price.compact(p);
		item.setPrice(p);
		logger.info("经计算，[" + item.getObjectType() + "/" + item.getProductId() + "]的价格规则是[" + price + "]，设置交易[" + item.getTransactionId() + "]的labelMoney为单价:" + item.getLabelMoney() + ",requestMoney为现金money:" + item.getRequestMoney() + ",frozenMoney为coin:" + item.getFrozenMoney() + ",successMoney为点数point:" + item.getSuccessMoney());
		return OperateResult.success.getId();
	}

	@Override
	public boolean generatePriceExtraData(EOEisObject object, String priceType) {
		boolean validPriceType = false;
		for(PriceType pt : PriceType.values()){
			if(priceType.equalsIgnoreCase(pt.toString())){
				validPriceType = true;
				break;
			}
		}
		if(!validPriceType){
			logger.error("无效的价格类型:" + priceType);
			return false;
		}

		Price price = getPrice(object, priceType);
		if(price == null) {
			return false;
		}
		return generatePriceExtraData(object, price);

	}


	@Override
	public Price getPrice(EOEisObject object, String priceType) {
		
		Assert.notNull(object,"尝试获取价格的对象不能为空");
		Assert.notNull(object.getObjectType(),"尝试获取价格的对象类型不能为空");
		Assert.isTrue(object.getId() > 0,"尝试获取价格的对象ID不能为空");
		
		PriceCriteria priceCriteria = new PriceCriteria(object.getOwnerId());
		priceCriteria.setObjectType(object.getObjectType());
		priceCriteria.setObjectId(object.getId());
		priceCriteria.setCurrentStatus(BasicStatus.normal.getId());
		priceCriteria.setPriceType(priceType);
		List<Price> priceList = list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			logger.info("找不到objectType={},id={}的价格规则,使用反射检查{}类的getPrice方法", priceCriteria.getObjectType(), priceCriteria.getObjectId(), object.getClass().getName());
			Method method = ClassUtils.getMethodIfAvailable(object.getClass(), "getPrice", new Class<?>[0]);
			if(method == null) {
				return null;
			}
			try {
				Object result = method.invoke(object, new Object[] {});
				if(result != null) {
					//按标准逻辑解析价格字符串 money#coin#point#score					
					return Price.parse(result.toString());
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			
			return null;
		} else {
			return priceList.get(0);
		}
	}


	@Override
	public boolean generatePriceExtraData(EOEisObject document, Price price) {


/*
		StringBuffer sb = new StringBuffer();
		//sb.append("PRICE_");
		//sb.append(price.getPriceType());
		sb.append(MoneyType.money.name());
		sb.append('=');
		sb.append(price.getMoney());
		sb.append('&');
		sb.append(MoneyType.coin.name());
		sb.append('=');
		sb.append(price.getCoin());
		sb.append('&');
		sb.append(MoneyType.point.name());
		sb.append('=');
		sb.append(price.getPoint());
		sb.append('&');
		sb.append(MoneyType.score.name());
		sb.append('=');
		sb.append(price.getScore());
		sb.append('&');
		document.setExtraValue("PRICE_" + price.getPriceType(), sb.toString().replaceAll("&$",""));

		sb = new StringBuffer();			
		if(price.getMoney() > 0){
			sb.append("money:");
			sb.append(price.getMoney());
			sb.append(';');
		}
		if(price.getCoin() > 0){
			sb.append("coin:");
			sb.append(price.getCoin());
			sb.append(';');
		}
		if(price.getPoint() > 0){
			sb.append("point:");
			sb.append(price.getPoint());
			sb.append(';');
		}
		if(price.getScore() > 0){
			sb.append("score:");
			sb.append(price.getScore());
			sb.append(';');
		}
		*/

		document.setExtraValue(DataName.productMarketPrice.toString(), String.valueOf(price.getMarketPrice()));

		if(price.getPriceType().equalsIgnoreCase(PriceType.PRICE_SALE.name())) {
			document.setExtraValue(DataName.productSaleMoney.toString(), defaultMoneyFormat.format(price.getMoney()));
		} else {
			document.setExtraValue(DataName.productBuyMoney.toString(),  defaultMoneyFormat.format(price.getMoney()));

		}



		return true;
	}

	@Override
	public int applyPrice(Item item, String priceType) {
		boolean validPriceType = false;
		for(PriceType pt : PriceType.values()){
			if(priceType.equalsIgnoreCase(pt.toString())){
				validPriceType = true;
				break;
			}
		}
		logger.info("开始为订单:{}应用价格:{}", item.getTransactionId(), priceType);
		if(!validPriceType){
			logger.error("无效的价格类型:" + priceType);
			return EisError.priceMoneyError.id;
		}
		Assert.notNull(item, "尝试应用价格的Item是空");
		Assert.isTrue(item.getChargeFromAccount() > 0, "尝试应用价格的Item，未指定用户");
		Assert.isTrue(item.getProductId() > 0, "尝试应用价格的Item，未指定产品Id");
		Assert.notNull(priceType, "尝试应用价格的Item，未指定价格类型priceType");


		PriceCriteria priceCriteria = new PriceCriteria(item.getOwnerId());
		priceCriteria.setObjectType(item.getObjectType());
		priceCriteria.setObjectId(item.getProductId());
		priceCriteria.setPriceType(priceType);
		priceCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Price> priceList = list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			logger.warn("找不到{}/{}的价格规则", item.getObjectType(), item.getProductId());
			return EisError.systemDataError.getId();
		}
		if(priceList.size() != 1){
			logger.warn("{}/{}的类型={}的价格规则不唯一", item.getObjectType(), item.getProductId(), priceType);
			return EisError.systemDataError.getId();
		}
		Price price = priceList.get(0);
		return applyPrice(item, price);
	}


	@Override
	public Price getPrice(PriceCriteria priceCriteria) {

		Assert.notNull(priceCriteria,"尝试获取价格的条件不能为空");
		Assert.isTrue(priceCriteria.getObjectId() > 0, "尝试获取价格的条件，其价格对象ID不能为空");
		if(priceCriteria.getPriceType() == null){
			logger.error("尝试获取价格的条件未指定价格类型，使用默认类型:" + PriceType.PRICE_STANDARD.toString());
		} else {
			boolean validPriceType = false;
			for(PriceType pt : PriceType.values()){
				if(priceCriteria.getPriceType().equalsIgnoreCase(pt.toString())){
					validPriceType = true;
					break;
				}
			}
			if(!validPriceType){
				logger.error("无效的价格类型:" + priceCriteria.getObjectType() + "，使用默认价格类型");
				priceCriteria.setPriceType(PriceType.PRICE_STANDARD.toString());
			}
		}
		if(priceCriteria.getObjectType() == null){
			logger.error("尝试获取价格的条件未指定价格对象类型，使用默认类型product");
			priceCriteria.setObjectType(ObjectType.product.name());
		}

		priceCriteria.setCurrentStatus(BasicStatus.normal.getId());
		List<Price> priceList = list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			logger.warn("找不到类型=" + priceCriteria.getObjectType() + "ID=" + priceCriteria.getObjectId() + "的" + priceCriteria.getPriceType() + "价格规则");
			return null;
		}
		if(priceList.size() != 1){
			logger.warn("类型=" + priceCriteria.getObjectType() + "ID=" + priceCriteria.getObjectId() + "的" + priceCriteria.getPriceType() + "价格规则不唯一");
			return null;
		}
		return priceList.get(0);
	}



	@Override
	public String generateTransactionToken(Price price, long uuid) {
		long ts = new Date().getTime();
		String src = new StringBuffer().append(price.getPriceId()).append('|').append(price.getObjectId()).append('|').append(uuid).append('|').append(ts).toString();
		logger.debug("根据价格[" + price.getPriceId() + "]、产品[" + price.getObjectId() + "]和用户UUID[" + uuid + "]生成交易令牌源:" + src);
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		return crypt.aesEncrypt(src);		
	}


	@Override
	public Price getPriceByToken(EOEisObject object, long uuid, String transactionToken) {
		Assert.notNull(object, " 尝试通过令牌获取价格的对象不能为空");
		Assert.isTrue(object.getId() > 0, "尝试通过获取价格的objectId不能为0");
		if(uuid < 1){
			logger.error("尝试获取价格的uuid异常:" + uuid);
			return null;
		}
		if(StringUtils.isBlank(transactionToken)){
			logger.error("尝试获取价格的transactionToken为空");
			return null;
		}
		Crypt crypt = new Crypt();
		crypt.setAesKey(aesKey);
		String src = crypt.aesDecrypt(transactionToken);
		if(src == null){
			logger.error("无法对transactionToken进行解密:" + transactionToken);
			return null;
		}
		String[] data = src.split("\\|");
		if(data == null || data.length < 5){
			logger.error("无法对解密数据进行分组:" + src);
			return null;
		}
		String objectType = data[1];
		if(!objectType.equalsIgnoreCase(object.getObjectType())) {
			logger.error("交易令牌中的第2个数据[" + data[1] + "]与objectType[" + object.getObjectType() + "]不一致");
			return null;
		}
		long objectId = NumericUtils.parseLong(data[2]);
		
		if(objectId <= 0 || objectId != object.getId()){
			logger.error("交易令牌中的第3个数据[" + data[2] + "]与objectId[" + objectId + "]不一致");
			return null;
		}
		if(!NumericUtils.isNumeric(data[2])){
			logger.error("交易令牌数据异常，第3个数据不是数字:" + src);
			return null;
		}
		if(!data[2].equals(String.valueOf(uuid))){
			logger.error("交易令牌中的第3个数据[" + data[2] + "]与uuid[" + uuid + "]不一致");
			return null;
		}
		if(!NumericUtils.isNumeric(data[3])){
			logger.error("交易令牌数据异常，第4个数据不是数字:" + src);
			return null;
		}
		long tokenTs = Long.parseLong(data[3]);
		long currentTs = new Date().getTime();
		logger.debug("交易令牌时间是[" + sdf.format(new Date(tokenTs)) + "]，当前价格有效期是" + priceTtl + "秒");
		if(currentTs - tokenTs  > priceTtl * 1000){
			logger.error("交易令牌时间[" + sdf.format(new Date(tokenTs)) + "]已超过有效期" + priceTtl + "秒");
		}
		if(!data[2].equals(String.valueOf(uuid))){
			logger.error("交易令牌中的第3个数据[" + data[2] + "]与uuid[" + uuid + "]不一致");
			return null;
		}
		long priceId = 0;
		if(!NumericUtils.isNumeric(data[0])){
			logger.error("交易令牌数据异常，第一个数据不是数字");
		}
		priceId = Long.parseLong(data[0]);
		Price price = select(priceId);
		if(price == null){
			logger.error("根据交易令牌找不到指定的Price:" + priceId);
			return null;
		}	
		if(price.getIdentify() != null){

		}

		return price;


	}

}
