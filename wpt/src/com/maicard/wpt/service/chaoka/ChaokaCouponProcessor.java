package com.maicard.wpt.service.chaoka;

import static com.maicard.standard.KeyConstants.LOCKED_COUPON_PREFIX;
import static com.maicard.standard.KeyConstants.NEW_COUPON_PREFIX;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.CouponCriteria;
import com.maicard.money.criteria.CouponModelCriteria;
import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.money.service.CouponModelService;
import com.maicard.money.service.CouponProcessor;
import com.maicard.money.service.CouponService;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.standard.TransactionStandard.TransactionStatus;

/**
 * 内部随机串形式生成和发布优惠券
 *
 *
 * @author NetSnake
 * @date 2016年5月14日
 *
 */
public class ChaokaCouponProcessor extends BaseService implements CouponProcessor{

	private final int MIN_NUMBER_LENGTH = 4;

	private final int MAX_NUMBER_LENGTH = 32;




	//private final int LOCK_MAX_RETRY = 3;

	@Resource
	private CenterDataService centerDataService;

	@Resource
	private CouponModelService couponModelService;

	@Resource
	private CouponService couponService;


	/**
	 * 锁定一张卡券的最长时间
	 */
	private final int LOCK_TTL = 3600;	
	private final int FETCH_RETRY = 10;
	private final int MAX_LIST_COUNT = 10;

	private final String DEFAULT_COUPON_CODE = "rand";

	DecimalFormat df = new DecimalFormat("0");


	@Override
	public List<CouponModel> list(CouponModelCriteria couponModelCriteria, Object parameter) {
		return null;
	}

	@Override
	public EisMessage fetch(CouponCriteria couponCriteria) {
		Assert.notNull(couponCriteria,"准备获取卡券的条件不能为空");
		Assert.notNull(couponCriteria.getCouponCode(),"准备获取卡券的条件中卡券代码不能为空");
		Assert.isTrue(couponCriteria.getOwnerId() > 0,"准备获取卡券的条件中ownerId不能为0");
		Assert.isTrue(couponCriteria.getUuid() != 0,"准备获取卡券的条件中用户UUID不能为0");

		int count = 0;
		Paging paging = null;
		if(couponCriteria.getPaging() != null){
			paging = couponCriteria.getPaging();
			count = couponCriteria.getPaging().getMaxResults();
		} else {
			paging = new Paging(MAX_LIST_COUNT);
			paging.setCurrentPage(1);
		}
		if(count == 0){
			count = 1;
		}

		CouponModelCriteria couponModelCriteria = new CouponModelCriteria(couponCriteria.getOwnerId());
		couponModelCriteria.setCurrentStatus(BasicStatus.normal.id);
		if(StringUtils.isBlank(couponCriteria.getCouponCode())){
			couponModelCriteria.setCouponCode("rand");
		} else {
			couponModelCriteria.setCouponCode(couponCriteria.getCouponCode());
		}
		List<CouponModel> couponModelList = couponModelService.list(couponModelCriteria);
		logger.debug("查找couponCode=" + couponModelCriteria.getCouponCode() + "的卡券模型返回数量是:" + (couponModelList == null ? "空" : couponModelList.size()));
		if(couponModelList == null || couponModelList.size() < 1){
			return new EisMessage(EisError.productNotExist.id,"指定的卡券不存在");
		}
		CouponModel couponModel = couponModelList.get(0);
		List<Coupon> lockedCouponList = new ArrayList<Coupon>();

		int lockedCount = 0;
		for(int j = 0; j < FETCH_RETRY;){
			Coupon coupon = this.generate(couponModel);
			if(coupon == null){
				j++;
				continue;
			}
			int rs = couponService.insert(coupon);
			if(rs != 1){
				j++;
				continue;
			}
			coupon.setUuid(couponCriteria.getUuid());
			Coupon lockedCoupon = this.lock(coupon);
			logger.debug("第" + (j+1) + "次获取卡券结果:" + lockedCoupon);
			if(lockedCoupon != null){
				lockedCount++;
				logger.debug("第" + (j+1) + "次获取卡券成功，已成功获取" + lockedCount + "个，一共需要获取:" + count + "个");
				lockedCouponList.add(lockedCoupon);
				if(lockedCount >= count){
					break;
				}
			} else {
				j++;
			}
		}
		EisMessage resultMsg = new EisMessage(OperateResult.success.getId(),"获取成功");
		resultMsg.setAttachmentData("couponList", lockedCouponList);
		return resultMsg;

	}

	@Override
	public int consume(Coupon coupon) {
		if(StringUtils.isBlank(coupon.getCouponCode())){
			coupon.setCouponCode(DEFAULT_COUPON_CODE);
		}
		//Coupon _oldCoupon = null;
		/*if(coupon.getCouponId() > 0){
			_oldCoupon = couponService.select(coupon.getCouponId());

		} else {
			CouponCriteria couponCriteria = new CouponCriteria();
			couponCriteria.setCouponCode(coupon.getCouponCode());
			couponCriteria.setCouponSerialNumber(coupon.getCouponSerialNumber());
			couponCriteria.setCouponPassword(coupon.getCouponPassword());
			couponCriteria.setUuid(coupon.getUuid());
			List<Coupon> couponList = couponService.list(couponCriteria);
			if(couponList != null && couponList.size() > 0){
				_oldCoupon = couponList.get(0);
			}
		}
		if(_oldCoupon == null){
			logger.error("找不到指定的卡券:" + coupon);
			return -EisError.objectIsNull.id;
		}
		if(_oldCoupon.getCurrentStatus() == Coupon.STATUS_USED){
			logger.debug("指定卡券[" + _oldCoupon + "]状态是:" + Coupon.STATUS_USED + ",返回已使用");
			return Coupon.STATUS_USED;
		}
		if(_oldCoupon.getCurrentStatus() != Coupon.STATUS_LOCKED){
			logger.debug("指定卡券[" + _oldCoupon + "]状态是:" + Coupon.STATUS_USED + ",不是锁定状态，返回错误");
			return -EisError.objectIsNull.id;
		}*/

		String key = LOCKED_COUPON_PREFIX + "#" + coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = centerDataService.getExclusive(key, true);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);
			return -EisError.OBJECT_IS_NULL.id;
		}
		Coupon cachedCoupon = null;
		try {
			cachedCoupon = JsonUtils.getInstance().readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.error("从REDIS中获取的卡券[" + key + "]数据异常:" + value);
			return -EisError.dataError.id;
		}


		if(cachedCoupon.getUuid() != coupon.getUuid()){
			logger.error("从REDIS中获取的卡券[" + key + "]，其拥有者[" + cachedCoupon.getUuid() + "]与请求提交的用户[" + coupon.getUuid() + "]不一致");
			return -EisError.dataVerifyFail.id;
		}

		/*_oldCoupon = cachedCoupon;
		_oldCoupon.setUseTime(new Date());
		_oldCoupon.setCurrentStatus(Coupon.STATUS_USED);
		_oldCoupon.setSyncFlag(0);*/
		cachedCoupon.setSyncFlag(0);
		couponService.update(cachedCoupon);
		BeanUtils.copyProperties(cachedCoupon, coupon);
		return OperateResult.success.getId();
	}

	@Override
	public String getProcessorDesc() {
		return "麦卡点券处理器" + this.getClass().getSimpleName();
	}



	/**
	 * 根据指定模型和卡号密码长度生成一个随机串卡券
	 * @param couponModel
	 * @param length
	 * @return
	 */
	public  synchronized Coupon generate(CouponModel couponModel) {
		int snLength = (int)couponModel.getLongExtraValue(DataName.serialNumberLength.toString());
		if(snLength < MIN_NUMBER_LENGTH){
			snLength = MIN_NUMBER_LENGTH;
		}
		if(snLength > MAX_NUMBER_LENGTH){
			snLength = MAX_NUMBER_LENGTH;
		}
		int passwordLength = (int)couponModel.getLongExtraValue(DataName.passwordLength.toString());
		if(passwordLength < MIN_NUMBER_LENGTH){
			passwordLength = MIN_NUMBER_LENGTH;
		}
		if(passwordLength > MAX_NUMBER_LENGTH){
			passwordLength = MAX_NUMBER_LENGTH;
		}		
		boolean numericOnly = couponModel.getBooleanExtraValue(DataName.NUMERIC_ONLY.toString());
		String sn = null;
		String password = null;
		if(numericOnly){
			int length = 0;
			int count = 1;
			if(snLength > 10){
				length = snLength / 2;
				count = 2;
			} else {
				length = snLength;
			}
			StringBuffer sb = new StringBuffer();
			for(int j = 0; j < count; j++){
				double i = Math.pow(10,length);
				String baseString = df.format(i);
				long base = Long.parseLong(baseString);
				int randLength = 0;
				if(base >= Integer.MAX_VALUE){
					randLength = 11;
				} else {
					randLength = baseString.length();
				}

				long rand1 = 10 * (1+ RandomUtils.nextInt(Integer.parseInt(df.format(0.9 * Math.pow(10, randLength-2)))));
				sb.append(df.format(i - rand1));
				logger.info("baseString=" + baseString + ",randLength=" + randLength + ",rand1=" + rand1 + ",sn=" + sb.toString());

			}
			sn = sb.toString();
			length = 0;
			count = 1;
			sb.setLength(0);
			if(passwordLength > 0){
				if(passwordLength > 10){
					length = passwordLength / 2;
					count = 2;
				} else {
					length = passwordLength;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				double i = Math.pow(10,length);
				String baseString = df.format(i);
				long base = Long.parseLong(baseString);
				int randLength = 0;
				if(base >= Integer.MAX_VALUE){
					randLength = 11;
				} else {
					randLength = baseString.length();
				}

				long rand1 = 10 * (1 + RandomUtils.nextInt(Integer.parseInt(df.format(0.9 * Math.pow(10, randLength-2)))));
				sb.append(df.format(i - rand1));
			}
			password = sb.toString();

		} else {
			int rand = RandomUtils.nextInt();
			//System.out.println(rand);
			String salt = String.valueOf(System.currentTimeMillis() + rand);

			String src = DigestUtils.md5Hex(salt);
			//System.out.println("src=" + src);
			if(src.length() >= (snLength + passwordLength)){
				sn = src.substring(0, snLength);
				if(passwordLength > 0){
					password = src.substring(snLength, snLength+passwordLength);
				}
			} else {
				sn = src.substring(0, snLength);
				if(passwordLength > 0){

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rand = RandomUtils.nextInt();
					salt = String.valueOf(System.currentTimeMillis() + rand);
					src = DigestUtils.md5Hex(String.valueOf(salt));
					password = src.substring(0,passwordLength);
				}

			}

		}
		Coupon coupon = new Coupon(couponModel);
		coupon.setCouponSerialNumber(sn.toUpperCase());
		if(passwordLength > 0){
			coupon.setCouponPassword(password.toUpperCase());
		}
		return coupon;

	}



	/**
	 * 把对应卡券设置为某个用户所有
	 * @param coupon
	 * @return
	 */
	@Override
	public Coupon lock(CouponCriteria couponCriteria) {
		Assert.notNull(couponCriteria, "尝试锁定的卡券条件为空");
		Assert.isTrue(couponCriteria.getUuid() > 0, "尝试锁定的卡券条件,UUID为0");
		Coupon _oldCoupon = null;

		CouponCriteria couponCriteria2 = couponCriteria.clone();
		//不使用用户和状态条件查询
		couponCriteria2.setUuid(0);
		couponCriteria2.setCurrentStatus(null);
		//couponCriteria.setUuid(coupon.getUuid());
		//couponCriteria.setCurrentStatus(Coupon.STATUS_NEW);
		List<Coupon> couponList = couponService.list(couponCriteria2);
		if(couponList != null && couponList.size() > 0){
			_oldCoupon = couponList.get(0);
		}

		if(_oldCoupon == null){
			logger.error("在本地系统中，找不到指定的卡券:" + couponCriteria);
			return null;
		}
		if(_oldCoupon.getCurrentStatus() == TransactionStatus.closed.id){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "已使用");
			return null;
		}
		/*if(_oldCoupon.getUuid() !=couponCriteria.getUuid()){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "不属于用户[" + couponCriteria.getUuid() + "],而属于:" + _oldCoupon.getUuid());
			return null;
		}
		 */

		int validateRs = validate(_oldCoupon);
		if(validateRs == OperateResult.success.getId()){
			logger.debug("卡券[" + _oldCoupon.getCouponId() + "]已经属于用户:" + _oldCoupon.getUuid());
			couponService.update(_oldCoupon);
			return _oldCoupon;
		}


		_oldCoupon.setVersion(_oldCoupon.getVersion()+1);

		String lockKey = "Lock#Coupon#" + _oldCoupon.getCouponId();
		boolean lockSuccess =  centerDataService.lock(lockKey);


		if(!lockSuccess){
			logger.error("无法锁定Coupon:" + _oldCoupon.getCouponId());
			return null;
		}

		Coupon cachedCoupon = null;


		String key = NEW_COUPON_PREFIX + "#" + _oldCoupon.getOwnerId() + "#" + _oldCoupon.getCouponCode() + "#" + _oldCoupon.getCouponSerialNumber() + "#" + _oldCoupon.getCouponPassword();
		String value = centerDataService.get(key);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);		
			return null;
		} 
		try {
			cachedCoupon = JsonUtils.getInstance().readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.error("从REDIS中获取的卡券[" + key + "]数据异常:" + value);
			centerDataService.delete(lockKey);
			return null;
		}

		long delRs = centerDataService.delete(key);
		if(delRs < 1){
			logger.error("无法从REDIS中删除新状态卡券:" + key);
			return null;
		}

		key = key.replaceAll(NEW_COUPON_PREFIX, LOCKED_COUPON_PREFIX);

		//	long couponId = _oldCoupon.getCouponId();

		//以中心缓存数据为准
		if(cachedCoupon != null){
			_oldCoupon = cachedCoupon;	
		}
		_oldCoupon.setCurrentStatus(Coupon.STATUS_LOCKED);
		_oldCoupon.setUuid(couponCriteria.getUuid());

		ObjectMapper om = JsonUtils.getInstance();
		om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

		value = null;
		try {
			value = om.writeValueAsString(_oldCoupon);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if(value == null){
			logger.error("无法序列化Coupon:" + _oldCoupon.getCouponId());
			centerDataService.delete(lockKey);
			return null;
		}


		boolean setSuccess = centerDataService.setIfNotExist(key, value, -1);		
		if(!setSuccess){
			logger.error("无法添加LOCK缓存[" + key + "]，无法设置卡券[" + _oldCoupon.getCouponId() + "]为用户[" +  _oldCoupon.getUuid() + "]所有");
		}
		centerDataService.delete(lockKey);

		logger.debug("卡券[" + _oldCoupon + "]已被设置为用户[" + _oldCoupon.getUuid() + "]所有");		
		couponService.update(_oldCoupon);
		return _oldCoupon;
	}


	@Override
	public int validate(Coupon coupon) {
		Coupon _oldCoupon = null;
		if(coupon.getCouponId() > 0){
			_oldCoupon = couponService.select(coupon.getCouponId());

		} else {
			CouponCriteria couponCriteria = new CouponCriteria(coupon.getOwnerId());
			couponCriteria.setCouponCode(coupon.getCouponCode());
			couponCriteria.setCouponSerialNumber(coupon.getCouponSerialNumber());
			couponCriteria.setCouponPassword(coupon.getCouponPassword());
			//	couponCriteria.setUuid(coupon.getUuid());
			//	couponCriteria.setCurrentStatus(Coupon.STATUS_LOCKED);
			List<Coupon> couponList = couponService.list(couponCriteria);
			if(couponList != null && couponList.size() > 0){
				_oldCoupon = couponList.get(0);
			} else {
				logger.error("在本地系统中，根据条件找不到指定的卡券:" + couponCriteria );

			}
		}
		if(_oldCoupon == null){
			logger.error("在本地系统中，找不到指定的卡券:" + coupon );
			return -EisError.OBJECT_IS_NULL.id;
		}
		if(_oldCoupon.getCurrentStatus() == TransactionStatus.closed.id){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "已使用");
			return -EisError.cardUsedBefore.id;
		}
		if(_oldCoupon.getUuid() != coupon.getUuid()){
			logger.debug("在本地系统中，指定的卡券:" + _oldCoupon.getCouponId() + "不属于用户[" + coupon.getUuid() + "],而属于:" + _oldCoupon.getUuid());
			return -EisError.userNotFoundInSystem.id;
		}

		String key = LOCKED_COUPON_PREFIX + "#" +  coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();
		String value = centerDataService.get(key);
		if(value == null){
			logger.error("无法从REDIS中获取卡券:" + key);
			return -EisError.OBJECT_IS_NULL.id;
		}
		logger.debug("读取REDIS数据[" + key + "]是:" + value);
		ObjectMapper om = JsonUtils.getNoDefaultValueInstance();
		Coupon cachedCoupon = null;
		try {
			cachedCoupon = om.readValue(value, Coupon.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(cachedCoupon == null){
			logger.warn("从REDIS中获取的卡券[" + key + "]数据异常:" + value + ",使用本地版本，并将本地数据放入中心缓存");
			try {
				value = om.writeValueAsString(_oldCoupon);
				boolean setSuccess = centerDataService.setIfNotExist(key, value, -1);
				logger.debug("将卡券[" + key + "]本地数据放入中心缓存，结果:" + setSuccess);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("缓存中已存在的Coupon:" + cachedCoupon);
			_oldCoupon = cachedCoupon;
		}

		if(_oldCoupon.getUuid() != coupon.getUuid()){
			logger.error("卡券[" + _oldCoupon.getCouponId() + "]，其拥有者[" + cachedCoupon.getUuid() + "]与请求提交的用户[" + coupon.getUuid() + "]不一致");
			return -EisError.dataVerifyFail.id;
		}
		if(cachedCoupon.getCurrentStatus() != Coupon.STATUS_LOCKED){
			logger.error("卡券[" + _oldCoupon.getCouponId() + "]状态不是锁定状态，而是:" + cachedCoupon.getCurrentStatus() + "，无效");
			return -EisError.dataVerifyFail.id;
		}
		logger.debug("卡券[" + _oldCoupon.getCouponId() + "]属于用户[" + cachedCoupon.getUuid() + "]并且有效");
		couponService.update(_oldCoupon);

		coupon = _oldCoupon;
		coupon.setCouponId(_oldCoupon.getCouponId());

		return OperateResult.success.getId();
	}


	@Override
	public Coupon lock(Coupon coupon){

		coupon.setCurrentStatus(TransactionStatus.auctionSuccess.id);


		String key = LOCKED_COUPON_PREFIX + "#" + coupon.getOwnerId() + "#" + coupon.getCouponCode() + "#" + coupon.getCouponSerialNumber() + "#" + coupon.getCouponPassword();


		boolean setSuccess = centerDataService.setIfNotExist(key, key, LOCK_TTL);		
		if(!setSuccess){
			logger.error("无法添加LOCK缓存[" + key + "]，无法设置卡券[" + coupon.getCouponId() + "]为用户[" +  coupon.getUuid() + "]所有");
			return null;
		}

		logger.debug("卡券[" + coupon + "]已被设置为用户[" + coupon.getUuid() + "]所有");		
		couponService.update(coupon);
		return coupon;
	}
	
	public static void main(String[] argv){
		ChaokaCouponProcessor r = new ChaokaCouponProcessor();
		CouponModel couponModel = new CouponModel();
		couponModel.setExtraValue("NUMERIC_ONLY","true");
		couponModel.setExtraValue("serialNumberLength","16");
		couponModel.setExtraValue("passwordLength","8");
	Coupon coupon = r.generate(couponModel);
		System.out.println(coupon.getCouponSerialNumber() + "," + coupon.getCouponPassword());
	}
}
