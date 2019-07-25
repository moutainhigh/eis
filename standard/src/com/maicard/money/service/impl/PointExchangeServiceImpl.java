package com.maicard.money.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.ConfigService;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PointExchangeCriteria;
import com.maicard.money.dao.PointExchangeDao;
import com.maicard.money.domain.PointExchange;
import com.maicard.money.service.PointExchangeService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.PointExchangeProcessor;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.Operate;
import com.maicard.standard.OperateResult;

@Service
public class PointExchangeServiceImpl extends BaseService implements PointExchangeService {

	@Resource
	private PointExchangeDao pointExchangeDao;

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private ConfigService configService;
	@Resource
	private MessageService messageService;

	private boolean handlerPointExchange = false;
	private final String messageBusName = null;

	@PostConstruct
	public void init(){
		handlerPointExchange = configService.getBooleanValue(DataName.handlerMoney.toString(),0);
	}

	public int insert(PointExchange pointExchange) {
		try{
			return pointExchangeDao.insert(pointExchange);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'PointExchange#' + #pointExchange.pointExchangeId")
	public int update(PointExchange pointExchange) {
		try{
			return  pointExchangeDao.update(pointExchange);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@CacheEvict(value = CommonStandard.cacheNameSupport, key = "'PointExchange#' + #pointExchangeId")
	public int delete(int pointExchangeId) {
		try{
			return  pointExchangeDao.delete(pointExchangeId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Cacheable(value = CommonStandard.cacheNameSupport, key = "'PointExchange#' + #pointExchangeId")
	public PointExchange select(int pointExchangeId) {
		return pointExchangeDao.select(pointExchangeId);
	}

	public List<PointExchange> list(PointExchangeCriteria pointExchangeCriteria) {
		return pointExchangeDao.list(pointExchangeCriteria);
	}

	public List<PointExchange> listOnPage(PointExchangeCriteria pointExchangeCriteria) {
		return pointExchangeDao.listOnPage(pointExchangeCriteria);
	}

	public int count(PointExchangeCriteria pointExchangeCriteria) {
		return pointExchangeDao.count(pointExchangeCriteria);
	}

	@Override
	public int begin(PointExchange pointExchange) {
		if(handlerPointExchange){
			return _beginLocal(pointExchange);
		} else {
			return _beginRemote(pointExchange);
		}
	}

	private int _beginRemote(PointExchange pointExchange) {
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.create.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("pointExchange", pointExchange);	
		m.setObjectType(ObjectType.pointExchange.toString());
		try{
			messageService.send(messageBusName, m);
			m = null;
		}catch(Exception e){
			logger.error("消息总线异常:" + e.getMessage());
			return EisError.systemBusy.getId();
		}
		return OperateResult.accept.getId();
	}

	private int _beginLocal(PointExchange pointExchange) {
		if(pointExchange.getUuid() <= 0){
			logger.error("尝试兑换的积分用户UUID=0");
			return EisError.REQUIRED_PARAMETER.getId();
		}
		if(pointExchange.getMoney() <= 0f && pointExchange.getCoin() <= 0 && pointExchange.getPoint() <= 0 && pointExchange.getScore() <= 0){
			logger.error("尝试兑换的积分，其需要的money、coin、point和score都为0");
			return EisError.REQUIRED_PARAMETER.getId();
		}
		if(StringUtils.isBlank(pointExchange.getProcessClass())){
			logger.error("尝试兑换积分[" + pointExchange.getPointExchangeId() + "，其处理类为空");
			return EisError.OBJECT_IS_NULL.getId();
		}
		PointExchangeProcessor pointExchangeProcessor = applicationContextService.getBeanGeneric(pointExchange.getProcessClass());

		if(pointExchangeProcessor == null){
			logger.error("尝试兑换积分" + pointExchange.getPointExchangeId() + "，找不到处理类:" + pointExchange.getProcessClass());
			return EisError.OBJECT_IS_NULL.getId();
		}
		
		pointExchangeProcessor.begin(pointExchange);
		logger.info("将积分兑换交给处理器[" + pointExchangeProcessor + "]进行处理");
		return OperateResult.success.getId();
	}

	/*@Override
	public void onMessage(EisMessage eisMessage) {
		if(handlerPointExchange){

			logger.debug("积分兑换服务收到消息");
			if(eisMessage == null){
				logger.error("得到的消息是空");
				return;
			}
			if(eisMessage.getObjectType() == null || !eisMessage.getObjectType().equals(ObjectType.pointExchange.toString())){
				eisMessage = null;
				return;
			}
			if(eisMessage.getAttachment() == null){
				logger.debug("消息中没有附件");
				eisMessage = null;
				return;
			}
			PointExchange pointExchange = null;
			Object object = eisMessage.getAttachment().get("pointExchange");
			if(object instanceof PointExchange){
				pointExchange = (PointExchange)object;
			} else if(object instanceof LinkedHashMap){
				ObjectMapper om = new ObjectMapper();
				om.setDateFormat(new SimpleDateFormat(CommonStandard.defaultDateFormat));
				String textData = null;
				try{
					textData = om.writeValueAsString(object);
					pointExchange = om.readValue(textData, PointExchange.class);
				}catch(Exception e){}
			}
			if(pointExchange == null){
				logger.debug("消息中没有找到需要的对象pointExchange");
				eisMessage = null;
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("消息指定的操作是[" + eisMessage.getOperateCode() + "/" + Operate.unknown.findById(eisMessage.getOperateCode()).getName() + ",syncFlag=" + pointExchange.getSyncFlag() + "]");
			}
			if(eisMessage.getOperateCode() == Operate.create.getId()){
				int rs = _beginLocal(pointExchange);
				logger.debug("积分兑换结果:" + rs + "，创建并同步日志");
				messageService.sendJmsDataSyncMessage(messageBusName, "pointExchangeLogService", "insert", pointExchange);
			}

		}
		eisMessage = null;		
	}*/

	@Override
	public int applyPrice(Item item) {
		if(item == null){
			logger.error("尝试应用积分兑换的Item是空");
			return EisError.OBJECT_IS_NULL.getId();
		}
		if(item.getChargeFromAccount() <= 0){
			logger.error("尝试应用积分兑换的Item，未指定用户");
			return EisError.OBJECT_IS_NULL.getId();
		}
		if(item.getChargeFromAccount() <= 0){
			logger.error("尝试应用积分兑换的Item，未指定产品");
			return EisError.OBJECT_IS_NULL.getId();
		}
		PointExchangeCriteria pointExchangeCriteria = new PointExchangeCriteria();
		pointExchangeCriteria.setObjectType(ObjectType.product.name());
		pointExchangeCriteria.setObjectId(item.getProductId());
		pointExchangeCriteria.setCurrentStatus(BasicStatus.normal.getId());
		pointExchangeCriteria.setOwnerId(item.getOwnerId());
		List<PointExchange> pointExchangeList = list(pointExchangeCriteria);
		if(pointExchangeList == null || pointExchangeList.size() < 1){
			logger.warn("找不到产品ID=" + item.getProductId() + "的积分兑换规则");
			return EisError.REQUIRED_PARAMETER.getId();
		}
		PointExchange pointExchange = pointExchangeList.get(0);
		return applyPrice(item, pointExchange);
		
	}
	
	@Override
	public int applyPrice(Item item, PointExchange pointExchange){
		if(pointExchange.getMoney() <= 0 && pointExchange.getCoin() <= 0 && pointExchange.getPoint() <= 0 && pointExchange.getScore() <= 0){
			logger.warn("积分兑换规则中的所有资金都是0");
			return EisError.moneyRangeError.getId();
		}
		item.setRequestMoney(pointExchange.getMoney());
		item.setFrozenMoney(pointExchange.getCoin());
		item.setSuccessMoney(pointExchange.getPoint());
		item.setInMoney(pointExchange.getScore());
		item.setBillingStatus(pointExchange.getPointExchangeId());
		logger.info("经计算，产品[" + item.getProductId() + "]的积分兑换规则是[" + pointExchange + "]，把交易[" + item.getTransactionId() + "]的requestMoney=money:" + item.getRequestMoney() + ",frozenMoney=coin:" + item.getFrozenMoney() + ",successMoney=point:" + item.getSuccessMoney());
		return OperateResult.success.getId();
	}

}
