package com.maicard.ec.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.service.DeliveryCompanyService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.DataName;
import com.maicard.standard.SecurityStandard.UserStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

@Service
public class DeliveryCompanyServiceImpl extends BaseService implements DeliveryCompanyService{

	final int deliveryCompanyTypeId = 9;
	@Resource
	private PartnerService partnerService;
	
	@Resource
	private ConfigService configService;
	
	
	@Override
	public List<User> list() {
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setUserTypeId(UserTypes.partner.getId());
		userCriteria.setUserExtraTypeId(deliveryCompanyTypeId);
		userCriteria.setCurrentStatus(UserStatus.normal.getId());
		List<User> deliveryCompanyList =  partnerService.list(userCriteria);
		logger.debug("当前系统合作快递公司数量:" + (deliveryCompanyList == null ? "空" : deliveryCompanyList.size()));
		return deliveryCompanyList;
	}
	@Override
	public void checkInfo(DeliveryOrder deliveryOrder) {
		if(deliveryOrder.getDeliveryCompanyId() > 0 && deliveryOrder.getDeliveryCompany() == null){
			User company = partnerService.select(deliveryOrder.getDeliveryCompanyId());
			if(company == null){
				logger.debug("在系统中找不到指定的配送商:" + deliveryOrder.getDeliveryCompanyId());
				return;
			}
			if(company.getOwnerId() != deliveryOrder.getOwnerId()){
				logger.debug("在系统中找到的配送商:" + company.getUuid() + ",其ownerId[" + company.getOwnerId() + "]与【】配送单[" + deliveryOrder.getDeliveryOrderId() + "]的ownerId[" + deliveryOrder.getOwnerId() + "]不一致");
				return;
			}
			
			deliveryOrder.setDeliveryCompany(company.getUsername());
			
		} else if(deliveryOrder.getDeliveryCompanyId() < 1 && deliveryOrder.getDeliveryCompany() != null){
			UserCriteria userCriteria = new UserCriteria(deliveryOrder.getOwnerId());
			userCriteria.setUsername(deliveryOrder.getDeliveryCompany());
			List<User> companyList = partnerService.list(userCriteria);
			if(companyList == null || companyList.size() < 1){
				logger.debug("在系统中找不到指定的配送商:" + deliveryOrder.getDeliveryCompany());
				return;
			}
			User company = companyList.get(0);
			if(company.getOwnerId() != deliveryOrder.getOwnerId()){
				logger.debug("在系统中找到的配送商:" + company.getUuid() + ",其ownerId[" + company.getOwnerId() + "]与【】配送单[" + deliveryOrder.getDeliveryOrderId() + "]的ownerId[" + deliveryOrder.getOwnerId() + "]不一致");
				return;
			}			
			deliveryOrder.setDeliveryCompanyId(company.getUuid());
			
		}
		
	}
	
	/**
	 * 根据快递单，确定最佳快递公司
	 * 目前仅返回系统定义的默认快递公司
	 */
	@Override
	public long getBestDeliveryCompanyId(DeliveryOrder deliveryOrder){
		return configService.getLongValue(DataName.deliveryCompanyId.toString(), deliveryOrder.getOwnerId());

	}

}
