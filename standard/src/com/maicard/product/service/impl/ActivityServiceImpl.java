package com.maicard.product.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseService;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.DataDefineService;
import com.maicard.exception.DataInvalidException;
import com.maicard.exception.RequiredAttributeIsNullException;
import com.maicard.exception.RequiredObjectIsNullException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PriceCriteria;
import com.maicard.money.domain.Money;
import com.maicard.money.domain.Price;
import com.maicard.money.service.GiftCardService;
import com.maicard.money.service.MoneyService;
import com.maicard.money.service.PriceService;
import com.maicard.product.criteria.ActivityCriteria;
import com.maicard.product.criteria.ActivityLogCriteria;
import com.maicard.product.criteria.ItemCriteria;
import com.maicard.product.dao.ActivityDao;
import com.maicard.product.domain.Activity;
import com.maicard.product.domain.ActivityLog;
import com.maicard.product.domain.Product;
import com.maicard.product.service.ActivityLogService;
import com.maicard.product.service.ActivityProcessor;
import com.maicard.product.service.ActivityService;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.ProductService;
import com.maicard.security.domain.User;
import com.maicard.site.criteria.DocumentDataCriteria;
import com.maicard.site.criteria.DocumentTypeCriteria;
import com.maicard.site.domain.Document;
import com.maicard.site.domain.DocumentData;
import com.maicard.site.domain.DocumentType;
import com.maicard.site.service.DocumentDataService;
import com.maicard.site.service.DocumentService;
import com.maicard.site.service.DocumentTypeService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.MoneyType;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.PriceType;
import com.maicard.standard.SiteStandard.DocumentStatus;
import com.maicard.standard.TransactionStandard.TransactionStatus;


@Service
public class ActivityServiceImpl extends BaseService implements ActivityService{

	@Resource
	private ActivityDao activityDao;

	@Resource
	private ActivityLogService activityLogService;
	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CacheService cacheService;
	@Resource
	private DataDefineService dataDefineService;
	@Resource
	private DocumentTypeService documentTypeService;
	@Resource
	private DocumentDataService documentDataService;
	@Resource
	private DocumentService documentService;
	@Resource
	private GiftCardService giftCardService;
	@Resource
	private MessageService messageService;
	@Resource
	private MoneyService moneyService;
	@Resource
	private PriceService priceService;
	@Resource
	private ProductService productService;
	@Resource
	private ItemService itemService;

	private final String cacheName = CommonStandard.cacheNameProduct;
	private final long lockTimeOut = 1000; //1秒

	@Override
	public int insert(Activity activity) {		
		return activityDao.insert(activity);
	}

	@Override
	public int update(Activity activity) {
		if(activity == null){
			throw new RequiredObjectIsNullException("将要更新的activity为空");
		}
		if(activity.getActivityId() < 1){
			throw new RequiredAttributeIsNullException("将要更新的activity其activityId为空");
		}
		int actualRowsAffected = 0;
		long activityId = activity.getActivityId();
		Activity _oldActivity = activityDao.select(activityId);
		if (_oldActivity == null) {
			logger.warn("尝试更新产品，但找不到对应的旧产品");
			return 0;
		}
		if(logger.isDebugEnabled()){
			logger.debug("尝试更新产品[" + activity.getActivityCode() + "]");
		}
		actualRowsAffected = activityDao.update(activity);
		return actualRowsAffected;
	}
	@Override
	public int delete(long activityId) {
		return 	activityDao.delete(activityId);
	}

	@Override
	public Activity select(long activityId) {
		return activityDao.select(activityId);
	}
	
	@Override
	public Activity select(String activityCode, long ownerId) {
		Assert.isTrue(StringUtils.isNotBlank(activityCode),"尝试查找单个Activity时，activityCode不能为空");
		Assert.isTrue(ownerId > 0,"尝试查找单个Activity时，ownerId必须大于0");
		
		ActivityCriteria activityCriteria = new ActivityCriteria(ownerId);
		activityCriteria.setActivityCode(activityCode);
		List<Activity> activityList = list(activityCriteria);
		logger.debug("根据activityCode=" + activityCode + ",ownerId=" + ownerId + "]找到的活动数量是:" + (activityList == null ? "空" : activityList.size()));
		if(activityList == null || activityList.size() < 1){
			return null;
		}
		return activityList.get(0);
	}

	@Override
	public List<Activity> list(ActivityCriteria activityCriteria) {

		List<Long> pkList = activityDao.listPk(activityCriteria);
		List<Activity> activityList = new ArrayList<Activity>();
		if(pkList != null && pkList.size() > 0){
			for(int i =0; i < pkList.size(); i++){
				activityList.add(activityDao.select(pkList.get(i)));
				activityList.get(i).setIndex(i+1);
			}
		}
		return activityList;
	}

	@Override
	public List<Activity> listOnPage(ActivityCriteria activityCriteria) {
		List<Long> pkList = activityDao.listPkOnPage(activityCriteria);
		List<Activity> activityList = new ArrayList<Activity>();
		if(pkList != null && pkList.size() > 0){
			for(int i =0; i < pkList.size(); i++){
				activityList.add(activityDao.select(pkList.get(i)));
				activityList.get(i).setIndex(i+1);
			}
		}
		return activityList;
	}

	@Override
	public int count(ActivityCriteria activityCriteria) {
		return activityDao.count(activityCriteria);
	}

	@Override
	public EisMessage execute(Activity activity, User user, Map<String,String>requestData) {
		if(activity == null){
			if(logger.isDebugEnabled()){
				logger.error("活动对象为空");
			}
			return null;
		}
		if(user == null){
			if(logger.isDebugEnabled()){
				logger.error("活动用户对象为空");
			}
			return null;
		}

		Activity a1 = activity.clone();

		ActivityProcessor activityProcessor = null;
		try{
			activityProcessor = (ActivityProcessor)applicationContextService.getBean(a1.getProcessor());
		}catch(Exception e){}
		if(activityProcessor == null){
			logger.warn("找不到活动[" + a1.getActivityId() + "]指定的处理器[" + a1.getProcessor() + "]");
			return new EisMessage(EisError.activityClosed.getId(),"活动尚未开放或已结束");
		}

		//查看当前活动的奖品
		/*
		if(StringUtils.isBlank(a1.getPromotion())){
			logger.warn("活动" + a1.getActivityId() + "的奖励数据为空");
		} else {
			String[] data1 = a1.getPromotion().split(";");
			if(data1 == null || data1.length < 1){
				logger.error("活动" + a1.getActivityId() + "的奖励数据无法解析:" + a1.getPromotion());
				return new EisMessage(EisError.activityClosed.getId(),"活动尚未开放或已结束");
			}
			for(String data2 : data1){
				String[] data3 = data2.split("#");
				if(data3 == null || data3.length < 3){
					logger.warn("活动[" + a1.getActivityId() + "]奖励[" + data2 + "]无法解析");
					continue;
				}
			}
		}
		if(a1.getData() != null){
			Pattern p = Pattern.compile("gift_(\\d+)");
			HashMap<String,String> giftMap = new HashMap<String,String>();
			for(String key : a1.getData().keySet()){
				Matcher m = p.matcher(key);
				if(m.matches()){
					String giftId = m.group(1);
					logger.debug("得到奖品[" + giftId + "]:" + a1.getData().get(key));				
					giftMap.put(giftId, a1.getData().get(key));
				}

			}
		}*/


		if(a1.getAccountLimit() > 0){
			if(!verifyUserAccessLimit(a1, user)){
				logger.info("用户未通过帐号参与次数检查，返回参与受限");
				return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
			}
		}
		if(a1.getIpLimit() > 0){
			if(!verifyIpAccessLimit(a1, user)){
				logger.info("用户未通过IP地址参与次数检查，返回参与受限");
				return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
			}
		}
		boolean isPayGame = false;
		if(a1.getAccountFreeLimit() > 0 || a1.getAccountLimit() > 0){
			ActivityLogCriteria  activityLogCriteria = new ActivityLogCriteria();
			activityLogCriteria.setUuid(user.getUuid());
			activityLogCriteria.setActivityType(activity.getActivityType());
			activityLogCriteria.setBeginTime(DateUtils.truncate(new Date(), Calendar.HOUR));
			if(a1.getAccountLimit() > 0){
				int allExecuteCount = activityLogService.count(activityLogCriteria);
				if(allExecuteCount >= a1.getAccountLimit()){
					logger.info("用户[" + user.getUuid() + "]已参加活动[" + a1.getActivityId() + "]" + allExecuteCount + "]次，不能再参与");
					return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
				}
				logger.info("用户[" + user.getUuid() + "]已参加活动[" + a1.getActivityId() + "]" + allExecuteCount + "]次，一共可以参加" + a1.getAccountLimit() + "次");

			}

			if(a1.getAccountFreeLimit() > 0){
				activityLogCriteria.setPayFeeJoin(ActivityLogCriteria.PAY_FEE_JOIN_FREE);
				int freeExecuteCount = activityLogService.count(activityLogCriteria);
				if(freeExecuteCount < a1.getAccountFreeLimit()){
					int remainFreeCount = a1.getAccountFreeLimit() - freeExecuteCount;
					logger.info("用户[" + user.getUuid() + "]已免费参加" +a1.getActivityId() + "#活动" + freeExecuteCount + "次，该活动可免费参加" + a1.getAccountFreeLimit() + ",还剩余:" + remainFreeCount + "次");
					a1.setAccountFreeLimit(remainFreeCount);
				} else {
					logger.info("用户[" + user.getUuid() + "]已免费参加" + a1.getActivityId() + "#活动" + freeExecuteCount + "次，需要付费参与或不再参与");
					if(a1.getAccountFeePerCount() == null){
						logger.info("用户[" + user.getUuid() + "]已免费参加" + a1.getActivityId() + "#活动" + freeExecuteCount + "次，但活动未定义付费参与规则，用户不再参与");
						return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
					}

					Map<String,Float> payRules = new HashMap<String,Float>();

					//解析付费规则
					String[] data = a1.getAccountFeePerCount().split(";");
					if(data == null || data.length < 1){
						logger.error("无法解析" + a1.getActivityId() + "#活动的付费参与规则");
						return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
					}
					for(String data2 : data){
						String[] data3 = data2.split("#");
						if(data3 == null || data3.length < 2){
							logger.error("无法解析" + a1.getActivityId() + "#活动的付费参与规则:" + data2);
							continue;
						}
						if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.coin.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
							payRules.put("coin", Float.parseFloat(data3[1]));
							logger.info("解析到" + a1.getActivityId() + "#活动的金币coin付费规则:" + data3[1]);
						}
						if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.point.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
							payRules.put("point", Float.parseFloat(data3[1]));
							logger.info("解析到" + a1.getActivityId() + "#活动的积分付费规则:" + data3[1]);
						}
						if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.money.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
							payRules.put("point", Float.parseFloat(data3[1]));
							logger.info("解析到" + a1.getActivityId() + "#活动的代币付费规则:" + data3[1]);
						}
					}
					if(payRules.size() < 1){
						logger.error("无法解析" + a1.getActivityId() + "#活动的付费参与规则，没有生成任何规则数据:" + a1.getAccountFeePerCount());
						return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
					}
					Money money = moneyService.select(user.getUuid(), user.getOwnerId());
					String feeDesc = "";
					if(money == null){
						logger.error(a1.getActivityId() + "#活动需要用户[" + user.getUuid() + "]付费参与，但该用户还没有资金账户");
						return new EisMessage(EisError.moneyAccountNotExist.getId(),"您的资金或积分不足，不能参加此活动");

					}
					if(payRules.get(MoneyType.money.name()) != null){
						if(payRules.get(MoneyType.money.name()) > money.getCoin()){
							logger.error(a1.getActivityId() + "#活动需要用户[" + user.getUuid() + "]money" + payRules.get(MoneyType.point.name()) + "参与，但该用户giftMoney不足:" + money.getGiftMoney());
							return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
						}
						feeDesc = "" + payRules.get(payRules.get(MoneyType.money.name()));
					}
					if(payRules.get(MoneyType.coin.name()) != null){
						if(payRules.get(MoneyType.coin.name()) > money.getCoin()){
							logger.error(a1.getActivityId() + "#活动需要用户[" + user.getUuid() + "]coin" + payRules.get(MoneyType.coin.name()) + "参与，但该用户coin不足:" + money.getCoin());
							return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
						}
						feeDesc = "," + payRules.get(payRules.get(MoneyType.coin.name()));
					}
					if(payRules.get(MoneyType.point.name()) != null){
						if(payRules.get(MoneyType.point.name()) > money.getCoin()){
							logger.error(a1.getActivityId() + "#活动需要用户[" + user.getUuid() + "]point" + payRules.get(MoneyType.point.name()) + "参与，但该用户point不足:" + money.getPoint());
							return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
						}
						feeDesc += "," + payRules.get(payRules.get(MoneyType.point.name()));

					}
					//FIXME 扣除用户资金
					isPayGame = true;					
					return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
				}
			}


		}
		EisMessage result = activityProcessor.execute(null,a1, user, requestData);
		int executeResult = result == null ? EisError.UNKNOWN_ERROR.getId() :result.getOperateCode();
		ActivityLog activityLog = new ActivityLog(a1, user, executeResult );
		if(isPayGame){
			activityLog.setPayFeeJoin(ActivityLogCriteria.PAY_FEE_JOIN_PAY);
		} else {
			activityLog.setPayFeeJoin(ActivityLogCriteria.PAY_FEE_JOIN_FREE);
		}
		int rs = activityLogService.insert(activityLog);
		if(rs != 1){
			//IP或账户次数已超限
			return new EisMessage(EisError.activityLimited.getId(), null);
		}
		if(result == null || result.getOperateCode() != OperateResult.success.getId()){
			//未中奖
			return result;			
		}



		return result;
	}

	private boolean verifyUserAccessLimit(Activity activity, User user) {
		logger.debug("检查用户[" + user.getUuid() + "]参与活动[" + activity.getActivityId() + "]的限制");
		if(activity.getAccountLimit() < 1){
			//没有账号限制
			return true;
		}
		int payPromotionCount = 0;
		int loginPromotioncount = 0;
		if(StringUtils.isNotBlank(activity.getPayPromotion())){
			//检查充值优惠
			String[] payData = activity.getPayPromotion().split(",");
			if(payData == null || payData.length < 1){
				logger.warn("活动[" + activity.getActivityId() + "]配置了充值优惠但没有具体数据");
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("解析活动[" + activity.getActivityId() + "]的充值优惠数据:" + activity.getPayPromotion());
				}

				try{
					String promotionType = payData[0].trim();	
					int promotionCount = Integer.parseInt(payData[1].trim());
					boolean repeat = Boolean.parseBoolean(payData[2].trim());
					int promotionProduct = Integer.parseInt(payData[3].trim());
					int payCount = Integer.parseInt(payData[4].trim());
					int payTimeLimit = Integer.parseInt(payData[5].trim());
					ItemCriteria itemCriteria = new ItemCriteria();
					itemCriteria.setChargeToAccount(user.getUuid());
					itemCriteria.setCurrentStatus(TransactionStatus.success.getId());
					if(promotionProduct > 0){
						itemCriteria.setProductIds(promotionProduct);
					}
					if(payTimeLimit > 0){
						itemCriteria.setEnterTimeBegin(DateUtils.addSeconds(new Date(), -payTimeLimit));
					}
					int realPayCount = itemService.count(itemCriteria);

					if(promotionType.equalsIgnoreCase("count")){
						//增加抽奖次数
						if(realPayCount > payCount){
							if(repeat){
								payPromotionCount = realPayCount / payCount   * promotionCount;
							} else {
								payPromotionCount = promotionCount; 
							}
						}						
					}
					if(logger.isDebugEnabled()){
						logger.debug("用户[" + user.getUuid() + "]在" + payTimeLimit + "秒内充值次数是" + realPayCount + ",可获得附加充值次数:" + payCount + "/" + realPayCount + "*" + promotionCount + "=" + payPromotionCount + "次" );
					}
				}catch(Exception e){
					logger.error("无法解析活动[" + activity.getActivityId() + "]的充值优惠数据:" + activity.getPayPromotion());
				}
			}
		}

		if(StringUtils.isNotBlank(activity.getLoginPromotion())){
			//检查登录优惠
			String[] payData = activity.getPayPromotion().split(",");
			if(payData == null || payData.length < 1){
				logger.warn("活动[" + activity.getActivityId() + "]配置了充值优惠但没有具体数据");
			} else {
				if(logger.isDebugEnabled()){
					logger.debug("解析活动[" + activity.getActivityId() + "]的充值优惠数据:" + activity.getPayPromotion());
				}

				try{
					String promotionType = payData[0].trim();	
					int promotionCount = Integer.parseInt(payData[1].trim());
					boolean repeat = Boolean.parseBoolean(payData[2].trim());
					int promotionProduct = Integer.parseInt(payData[3].trim());
					int payCount = Integer.parseInt(payData[4].trim());
					int payTimeLimit = Integer.parseInt(payData[5].trim());
					ItemCriteria itemCriteria = new ItemCriteria();
					itemCriteria.setChargeToAccount(user.getUuid());
					itemCriteria.setCurrentStatus(TransactionStatus.success.getId());
					if(promotionProduct > 0){
						itemCriteria.setProductIds(promotionProduct);
					}
					if(payTimeLimit > 0){
						itemCriteria.setEnterTimeEnd(DateUtils.addSeconds(new Date(), -payTimeLimit));
					}
					int realPayCount = itemService.count(itemCriteria);

					if(promotionType.equalsIgnoreCase("count")){
						//增加抽奖次数
						if(realPayCount > payCount){
							if(repeat){
								payPromotionCount = realPayCount / payCount   * promotionCount;
							} else {
								payPromotionCount = promotionCount; 
							}
						}						
					}
					if(logger.isDebugEnabled()){
						logger.debug("用户[" + user.getUuid() + "]在" + payTimeLimit + "秒内充值次数是" + realPayCount + ",可获得附加充值次数:" + payCount + "/" + realPayCount + "*" + promotionCount + "=" + payPromotionCount + "次" );
					}
				}catch(Exception e){
					logger.error("无法解析活动[" + activity.getActivityId() + "]的充值优惠数据:" + activity.getPayPromotion());
				}
			}
		}
		String key = "Activiy#" + activity.getActivityId() + "#uuid#" + user.getUuid() + "#limit";
		/*
		try {
			if(cacheService.tryReadLockOnKey(cacheName, key, lockTimeOut)){
				int count = cacheService.get(cacheName, key);


				cacheService.releaseReadLockOnKey(cacheName, key);
				if(logger.isInfoEnabled()){
					logger.info("用户[" + user.getUuid() + "]参与活动[" + activity.getActivityId() + "]的次数是[" + count + "]次，可参与次数:" + activity.getAccountLimit() + "+" + payPromotionCount + "+" + loginPromotioncount +"]");
				}
				if(count >= (activity.getAccountLimit() + payPromotionCount + loginPromotioncount)){	
					if(logger.isInfoEnabled()){
						logger.info("用户[" + user.getUuid() + "]参与活动[" + activity.getActivityId() + "]的次数是[" + count + "]已超过限制[" + activity.getAccountLimit() + "+" + payPromotionCount + "+" + loginPromotioncount + "]");
					}
					return false;
				}
				//否则的话，加写锁并写入一次
				count++;
				logger.debug("尝试写入新的用户参与次数[" + key + "]=" + count);
				if(	cacheService.tryWriteLockOnKey(cacheName, key, lockTimeOut)){
					cacheService.put(cacheName, key, count);
					cacheService.releaseWriteLockOnKey(cacheName, key);
					return true;
				}				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return false;
	}

	private boolean verifyIpAccessLimit(Activity activity, User user) {
		logger.debug("检查IP地址[" + user.getLastLoginIp() + "]参与活动[" + activity.getActivityId() + "]的限制");
		if(activity.getIpLimit() < 1){
			//没有IP地址限制
			return true;
		}
		String key = "Activiy#" + activity.getActivityId() + "#ip#" + user.getLastLoginIp() + "#limit";
		/*try {
			if(cacheService.tryReadLockOnKey(cacheName, key, lockTimeOut)){
				int count = cacheService.get(cacheName, key);

				if(logger.isInfoEnabled()){
					logger.info("IP[" + user.getLastLoginIp() + "]参与活动[" + activity.getActivityId() + "]的次数是[" + count + "]");
				}
				if(count >= activity.getIpLimit()){	
					if(logger.isInfoEnabled()){
						logger.info("用户[" + user.getUuid() + "]参与活动[" + activity.getActivityId() + "]的IP[" + user.getLastLoginIp() + "]次数是[" + count + "]已超过限制[" + activity.getIpLimit() + "]");
					}
					return false;
				}
				//否则的话，加写锁并写入一次
				count++;
				logger.debug("尝试写入新的用户参与次数[" + key + "]=" + count);
				cacheService.put(cacheName, key, count);
				return true;


			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return false;
	}

	@Override
	public EisMessage prepare(Activity activity, User user, Object parameter) {
		if(activity == null){
			if(logger.isDebugEnabled()){
				logger.error("活动对象为空");
			}
			return new EisMessage(EisError.OBJECT_IS_NULL.getId(),"活动对象为空");
		}
		if(user == null){
			if(logger.isDebugEnabled()){
				logger.error("活动用户对象为空");
			}
			return new EisMessage(EisError.userNotFoundInRequest.getId(),"活动用户为空");
		}

		ActivityProcessor activityProcessor = null;
		try{
			activityProcessor = (ActivityProcessor)applicationContextService.getBean(activity.getProcessor());
		}catch(Exception e){}
		if(activityProcessor == null){
			logger.warn("找不到活动[" + activity.getActivityId() + "]指定的处理器[" + activity.getProcessor() + "]");
			return new EisMessage(EisError.activityClosed.getId(),"活动尚未开放或已结束");
		}
		//查看当前活动的奖品
		if(StringUtils.isBlank(activity.getPromotion())){
			logger.error("活动[" + activity.getActivityId() + "的奖励数据为空");
			return new EisMessage(EisError.activityClosed.getId(),"活动尚未开放或已结束");
		}
		String[] data1 = activity.getPromotion().split(";");
		if(data1 == null || data1.length < 1){
			logger.error("活动[" + activity.getActivityId() + "的奖励数据无法解析:" + activity.getPromotion());
			return new EisMessage(EisError.activityClosed.getId(),"活动尚未开放或已结束");
		}


		if(activity.getAccountLimit() > 0){
			if(!verifyUserAccessLimit(activity, user)){
				logger.info("用户未通过帐号参与次数检查，返回参与受限");
				return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
			}
		}
		if(activity.getIpLimit() > 0){
			if(!verifyIpAccessLimit(activity, user)){
				logger.info("用户未通过IP地址参与次数检查，返回参与受限");
				return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
			}
		}
		if(activity.getAccountFreeLimit() > 0 || activity.getAccountLimit() > 0){
			ActivityLogCriteria  activityLogCriteria = new ActivityLogCriteria();
			activityLogCriteria.setUuid(user.getUuid());
			activityLogCriteria.setActivityType(activity.getActivityType());
			activityLogCriteria.setBeginTime(DateUtils.truncate(new Date(), Calendar.HOUR));
			if(activity.getAccountLimit() > 0){
				int allExecuteCount = activityLogService.count(activityLogCriteria);
				if(allExecuteCount >= activity.getAccountLimit()){
					logger.info("用户[" + user.getUuid() + "]已参加活动[" + activity.getActivityId() + "]" + allExecuteCount + "]次，不能再参与");
					return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
				}
			}

			if(activity.getAccountFreeLimit() > 0){
				activityLogCriteria.setPayFeeJoin(ActivityLogCriteria.PAY_FEE_JOIN_FREE);
				int freeExecuteCount = activityLogService.count(activityLogCriteria);
				if(freeExecuteCount < activity.getAccountFreeLimit()){
					int remainFreeCount = activity.getAccountFreeLimit() - freeExecuteCount;
					logger.info("用户[" + user.getUuid() + "]已免费参加" +activity.getActivityId() + "#活动" + freeExecuteCount + "次，该活动可免费参加" + activity.getAccountFreeLimit() + ",还剩余:" + remainFreeCount + "次");
					activity.setAccountFreeLimit(remainFreeCount);
					return new EisMessage(OperateResult.success.getId(),"" + remainFreeCount);

				}
				logger.info("用户[" + user.getUuid() + "]已免费参加" + activity.getActivityId() + "#活动" + freeExecuteCount + "次，需要付费参与或不再参与");
				if(activity.getAccountFeePerCount() == null){
					logger.info("用户[" + user.getUuid() + "]已免费参加" + activity.getActivityId() + "#活动" + freeExecuteCount + "次，但活动未定义付费参与规则，用户不再参与");
					return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
				}
				Map<String,Float> payRules = new HashMap<String,Float>();

				//解析付费规则
				String[] data = activity.getAccountFeePerCount().split(";");
				if(data == null || data.length < 1){
					logger.error("无法解析" + activity.getActivityId() + "#活动的付费参与规则");
					return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
				}
				for(String data2 : data){
					String[] data3 = data2.split("#");
					if(data3 == null || data3.length < 2){
						logger.error("无法解析" + activity.getActivityId() + "#活动的付费参与规则:" + data2);
						continue;
					}
					if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.coin.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
						payRules.put("coin", Float.parseFloat(data3[1]));
						logger.info("解析到" + activity.getActivityId() + "#活动的金币coin付费规则:" + data3[1]);
					}
					if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.point.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
						payRules.put("point", Float.parseFloat(data3[1]));
						logger.info("解析到" + activity.getActivityId() + "#活动的积分付费规则:" + data3[1]);
					}
					if(data3[0] != null && data3[0].trim().equalsIgnoreCase(MoneyType.money.name()) && data3[1] != null && StringUtils.isNumeric(data3[1])){
						payRules.put("point", Float.parseFloat(data3[1]));
						logger.info("解析到" + activity.getActivityId() + "#活动的代币付费规则:" + data3[1]);
					}
				}
				if(payRules.size() < 1){
					logger.error("无法解析" + activity.getActivityId() + "#活动的付费参与规则，没有生成任何规则数据:" + activity.getAccountFeePerCount());
					return new EisMessage(EisError.activityLimited.getId(),"您今日已不能参加此活动");
				}
				Money money = moneyService.select(user.getUuid(), user.getOwnerId());
				String feeDesc = "";
				if(money == null){
					logger.error(activity.getActivityId() + "#活动需要用户[" + user.getUuid() + "]付费参与，但该用户还没有资金账户");
					return new EisMessage(EisError.moneyAccountNotExist.getId(),"您的资金或积分不足，不能参加此活动");

				}
				if(payRules.get(MoneyType.money.name()) != null){
					if(payRules.get(MoneyType.money.name()) > money.getCoin()){
						logger.error(activity.getActivityId() + "#活动需要用户[" + user.getUuid() + "]money" + payRules.get(MoneyType.point.name()) + "参与，但该用户giftMoney不足:" + money.getGiftMoney());
						return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
					}
					feeDesc = "" + payRules.get(payRules.get(MoneyType.money.name()));
				}
				if(payRules.get(MoneyType.coin.name()) != null){
					if(payRules.get(MoneyType.coin.name()) > money.getCoin()){
						logger.error(activity.getActivityId() + "#活动需要用户[" + user.getUuid() + "]coin" + payRules.get(MoneyType.coin.name()) + "参与，但该用户coin不足:" + money.getCoin());
						return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
					}
					feeDesc = "," + payRules.get(payRules.get(MoneyType.coin.name()));
				}
				if(payRules.get(MoneyType.point.name()) != null){
					if(payRules.get(MoneyType.point.name()) > money.getCoin()){
						logger.error(activity.getActivityId() + "#活动需要用户[" + user.getUuid() + "]point" + payRules.get(MoneyType.point.name()) + "参与，但该用户point不足:" + money.getPoint());
						return new EisMessage(EisError.moneyNotEnough.getId(),"您的资金或积分不足，不能参加此活动");
					}
					feeDesc += "," + payRules.get(payRules.get(MoneyType.point.name()));

				}
				return new EisMessage(OperateResult.accept.getId(),"" + feeDesc);
			}

			logger.info(activity.getActivityId() + "#活动没有免费参与次数限制，直接参与");
			return activityProcessor.prepare(activity, user, parameter);
		}
		logger.info(activity.getActivityId() + "#活动没有参与次数或免费参与次数限制，直接参与");
		return activityProcessor.prepare(activity, user, parameter);
	}

	@Override
	public String createActivityBeginUrl(long activityId, String inviteCode) {
		Activity activity = select(activityId);
		if(activity == null){
			logger.error("找不到尝试创建起始地址的" + activityId + "#活动");
			return null;
		}
		if(activity.getCurrentStatus() != BasicStatus.normal.getId()){
			logger.error("尝试创建起始地址的" + activityId + "#活动状态异常:" + activity.getCurrentStatus());
			return null;
		}
		if(activity.getUrl() == null){
			logger.error("尝试创建起始地址的" + activityId + "#活动没有起始URL");
			return null;
		}
		if(activity.getUrl().startsWith("http")){
			return "<a href=\"" + activity.getUrl() + "\">" + activity.getActivityDesc() + "</a>";
		} else {
			return activity.getUrl();
		}
	}

	@Override
	public Activity generateNewActivity(String activityType) {
		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.activity.name());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		Activity activity = new Activity();
		if(StringUtils.isNotBlank(activityType)){
			activity.setActivityType(activityType);
		}
		if(dataDefineList == null || dataDefineList.size() < 1){
			logger.info("系统中没有活动[activity]的数据定义");
			return activity;
		}
		for(DataDefine dataDefine : dataDefineList){
			logger.debug("为新建活动设置默认扩展数据项:" + dataDefine.getDataCode());
			activity.setExtraValue(dataDefine.getDataCode(), null);
		}

		return activity;
	}

	@Override
	public Document getRefDocument(Activity activity) {

		int udid = (int) activity.getLongExtraValue(DataName.refUdid.toString());

		if(udid > 0){
			Document document = documentService.select(udid);
			if(document != null){
				if(document.getCurrentStatus() != DocumentStatus.published.getId()){
					logger.warn(activity.getActivityId() + "#活动指定的关联文档:" + udid + "未发布，状态是:" + document.getCurrentStatus());
					return null;
				} else {
					return document;
				}
			} else {
				logger.warn("找不到" + activity.getActivityId() + "#活动指定的关联文档:" + udid);
			}
		}

		DocumentTypeCriteria documentTypeCriteria = new DocumentTypeCriteria();
		documentTypeCriteria.setDocumentTypeCode(ObjectType.activity.name());
		List<DocumentType> documentTypeList = documentTypeService.list(documentTypeCriteria);
		int documentTypeId = 0;
		if(documentTypeList == null || documentTypeList.size() < 1){
			logger.warn("系统中未定义活动类型的文档类型");
			return null;
		} else {
			documentTypeId = documentTypeList.get(0).getDocumentTypeId();
		}


		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.document.name());
		dataDefineCriteria.setObjectId(documentTypeId);
		dataDefineCriteria.setDataCode(DataName.activityCode.toString());
		DataDefine dataDefine = dataDefineService.select(dataDefineCriteria);
		if(dataDefine == null){
			logger.error("找不到文档类型[" + documentTypeId + "]对应的activity数据定义");
			return null;
		}
		DocumentDataCriteria documentDataCriteria = new DocumentDataCriteria();
		documentDataCriteria.setDataDefineId(dataDefine.getDataDefineId());
		documentDataCriteria.setDataValue(activity.getActivityCode());
		List<DocumentData> documentDataList = documentDataService.list(documentDataCriteria);
		if(documentDataList == null || documentDataList.size() < 1){
			logger.error("找不到文档类型[" + documentTypeId + "]对应的activityCode数据定义");
			return null;
		}
		udid = documentDataList.get(0).getUdid();	
		return documentService.select(udid);	
	}

	@Override
	public boolean syncActivityByDocument(Document document) throws ParseException {
		Assert.notNull(document,"尝试创建活动但传入文档为空");

		SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

		String activityCode = document.getExtraValue(DataName.activityCode.toString());
		if(StringUtils.isBlank(activityCode)){
			logger.error("尝试创建活动但没有指定活动代码");
			return false;
		}

		Activity activity = new Activity(document.getOwnerId());


		ActivityCriteria activityCriteria = new ActivityCriteria(document.getOwnerId());
		activityCriteria.setActivityCode(activityCode);
		List<Activity> activityList = list(activityCriteria);
		if(activityList != null && activityList.size() > 0){
			//已存在相同活动
			Activity _oldActivity = activityList.get(0);
			activity.setActivityId(_oldActivity.getActivityId());
		}
		activity.setActivityCode(activityCode);
		activity.setActivityName(document.getTitle());
		activity.setActivityDesc(activity.getActivityName());
		String activityType = document.getExtraValue(DataName.activityType.toString());
		if(activityType == null){
			activity.setActivityType(ActivityCriteria.ACTIVITY_TYPE_BUY);
		}
		String beginTime = document.getExtraValue(DataName.activityBeginTime.toString());
		if(beginTime != null){
			activity.setBeginTime(sdf.parse(beginTime));
		}
		String endTime = document.getExtraValue(DataName.activityEndTime.toString());
		if(endTime != null){
			activity.setEndTime(sdf.parse(endTime));
		}
		String processor = document.getExtraValue(DataName.activityProcessor.toString());
		if(processor == null){
			logger.error("尝试创建新活动但是对应文档没有提供活动处理器");
			return false;
		}
		activity.setProcessor(processor);

		String productCode = document.getExtraValue(DataName.productCode.toString());
		if(productCode == null){
			logger.error("尝试创建新活动但是对应文档没有提供产品代码");
			return false;
		}
		Product product = productService.select(productCode, document.getOwnerId());
		if(product == null){
			logger.error("尝试创建新活动但是找不到文档对应的产品:" + productCode);
			return false;
		}
		activity.setExtraValue(DataName.refProductId.toString(), String.valueOf(product.getProductId()));

		if(document.getCurrentStatus() == DocumentStatus.published.getId()){
			activity.setCurrentStatus(BasicStatus.normal.getId());
		} else {
			activity.setCurrentStatus(document.getCurrentStatus());
		}

		DataDefineCriteria dataDefineCriteria2 = new DataDefineCriteria();
		dataDefineCriteria2.setObjectType(ObjectType.document.toString());
		dataDefineCriteria2.setDataType("float");
		dataDefineCriteria2.setDisplayLevel("open");
		dataDefineCriteria2.setObjectId(document.getDocumentTypeId());
		List<DataDefine> dataDefineList2 = dataDefineService.list(dataDefineCriteria2);
		if (dataDefineList2 == null || dataDefineList2.size() == 0) {
			logger.info("当前文档类型[" + document.getDocumentTypeId() + "]没有自定义字段.");
		}
		String coinMoney = null; //文币
		String marketPrice = null; //市场价
		for (DataDefine dataDefine : dataDefineList2) {
			if (dataDefine.getDataCode().equals("coinMoney")) {
				coinMoney = document.getExtraValue(dataDefine.getDataCode());
			}
			if (dataDefine.getDataCode().equals("marketPrice")) {
				marketPrice = document.getExtraValue(dataDefine.getDataCode());
			}

		}

		String activityPriceMoney = document.getExtraValue(DataName.activityPrice.toString());	//活动价格
		if(StringUtils.isBlank(activityPriceMoney) && StringUtils.isBlank(coinMoney)){
			//活动的RMB价格和COIN价格不能全部为空
			logger.error("尝试创建活动但是其RMB价格和COIN价格全部为空");
			return false;
		}
		PriceCriteria priceCriteria = new PriceCriteria(document.getOwnerId());
		priceCriteria.setObjectType(ObjectType.product.name());
		priceCriteria.setObjectId(product.getProductId());
		priceCriteria.setPriceType(PriceType.PRICE_PROMOTION.toString());
		priceCriteria.setIdentify(activityCode);
		List<Price> priceList = priceService.list(priceCriteria);
		if(priceList == null || priceList.size() < 1){
			Price price = new Price(document.getOwnerId());
			price.setObjectType(ObjectType.product.name());
			price.setObjectId(product.getProductId());
			price.setPriceType(PriceType.PRICE_PROMOTION.toString());
			if(StringUtils.isNotBlank(activityPriceMoney)){
				price.setMoney(Float.parseFloat(activityPriceMoney.trim()));
			}
			if(StringUtils.isNotBlank(coinMoney)){
				price.setCoin(Float.parseFloat(coinMoney.trim()));
			}
			price.setMarketPrice(Float.parseFloat(marketPrice.trim()));
			price.setIdentify(activityCode);
			price.setCurrentStatus(activity.getCurrentStatus());
			int rs = priceService.insert(price);
			if(rs != 1){
				logger.error("无法插入新的价格:" + price);
				return false;
			}
			logger.debug("成功为活动[" + activity.getActivityCode() + "]插入新价格:" + price);
		} else {
			if(priceList.size() > 1){
				logger.error("根据条件[" + priceCriteria + "]查询到的价格不止一个");
				throw new DataInvalidException("根据条件返回的价格多于一个");
			}
			Price price = priceList.get(0);
			price.setMoney(Float.parseFloat(activityPriceMoney.trim()));
			int rs = priceService.update(price);
			if(rs != 1){
				logger.error("无法更新价格:" + price);
				return false;
			}
		}



		DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
		dataDefineCriteria.setObjectType(ObjectType.activity.name());
		List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
		if(dataDefineList == null || dataDefineList.size() < 1){
			logger.info("系统中没有活动[activity]的数据定义");
		} else {
			for(DataDefine dataDefine : dataDefineList){

				String extraValue = document.getExtraValue(dataDefine.getDataCode());
				if(StringUtils.isBlank(extraValue)){
					logger.debug("未提交活动的扩展数据:" + dataDefine.getDataCode());
					continue;
				}
				extraValue = extraValue.trim();
				logger.debug("为新建活动添加文档中的扩展数据:" + dataDefine.getDataCode() + "=>" + extraValue);
				activity.setExtraValue(dataDefine.getDataCode(), extraValue);
			}
		}

		int rs = 0;
		if(activity.getActivityId() > 0){
			rs = update(activity);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "activityService", "update", activity);
			}
		} else {
			rs = insert(activity);
			if(rs == 1){
				messageService.sendJmsDataSyncMessage(null, "activityService", "insert", activity);
			}
		}
		if(rs != 1){
			logger.error("无法插入或修改活动:" + activity);
			return false;
		}
		logger.debug("成功同步活动:" + activity);
		document.setRedirectTo("/activity/" + activityCode + "/index" + CommonStandard.DEFAULT_PAGE_SUFFIX);
		document.setExtraValue(DataName.activityCode.toString(), activityCode);
		return true;
	}





}
