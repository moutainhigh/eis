package com.maicard.wpt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maicard.common.base.BaseService;
import com.maicard.common.domain.EisMessage;
import com.maicard.mb.service.MessageService;
import com.maicard.product.service.ActivityService;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.OperateResult;
import com.maicard.wpt.criteria.WeixinButtonCriteria;
import com.maicard.wpt.dao.WeixinButtonDao;
import com.maicard.wpt.domain.WeixinButton;
import com.maicard.wpt.service.WeixinButtonService;

@Service
public class WeixinButtonServiceImpl extends BaseService implements WeixinButtonService {

	@Resource
	private WeixinButtonDao weixinButtonDao;

	@Resource
	private ActivityService activityService;
	
	@Resource
	private PartnerService partnerService;
	
	@Resource
	private MessageService messageService;



	@Override
	public int insert(WeixinButton weixinButton) {
		try{
			return weixinButtonDao.insert(weixinButton);
		}catch(Exception e){
			logger.error("插入数据失败:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public int update(WeixinButton weixinButton) {
		try{
			return  weixinButtonDao.update(weixinButton);
		}catch(Exception e){
			logger.error("更新数据失败:" + e.getMessage());
		}
		return -1;

	}

	@Override
	public int delete(long weixinButtonId) {
		try{
			return  weixinButtonDao.delete(weixinButtonId);
		}catch(Exception e){
			logger.error("删除数据失败:" + e.getMessage());
		}
		return -1;	
	}

	@Override
	public WeixinButton select(long weixinButtonId) {
		WeixinButton weixinButton =  weixinButtonDao.select(weixinButtonId);
		if(weixinButton == null){
			weixinButton = new WeixinButton();
		}
		return weixinButton;
	}




	@Override
	public List<WeixinButton> list(WeixinButtonCriteria weixinButtonCriteria) {
		return weixinButtonDao.list(weixinButtonCriteria);

	}

	@Override
	public List<WeixinButton> listOnPage(WeixinButtonCriteria weixinButtonCriteria) {
		return weixinButtonDao.listOnPage(weixinButtonCriteria);

	}

	@Override
	public int count(WeixinButtonCriteria weixinButtonCriteria) {
		return weixinButtonDao.count(weixinButtonCriteria);
	}

	@Override
	@Transactional
	public EisMessage clone(WeixinButtonCriteria weixinButtonCriteria) {
		WeixinButtonCriteria weixinButtonCriteria2 = weixinButtonCriteria.clone();
		weixinButtonCriteria2.setUuid(weixinButtonCriteria2.getOwnerId());
		List<WeixinButton> buttonList = this.list(weixinButtonCriteria2);
		if(buttonList == null || buttonList.size() < 1){
			logger.error("无法克隆微信菜单，因为获取[uuid=ownerId=" + weixinButtonCriteria2.getUuid() + "]的微信菜单为空");
			return new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到系统微信菜单");
		}
		HashMap<Long, Long> menuIdMap = new HashMap<Long,Long>();

		List<WeixinButton> newWeixinButtonList = new ArrayList<WeixinButton>();
		User partner = partnerService.select(weixinButtonCriteria.getUuid());
		if(partner == null){
			logger.error("找不到新克隆微信菜单的对应用户:" + weixinButtonCriteria.getUuid());
			return new EisMessage(EisError.userNotFoundInSystem.id,"找不到指定的用户");
		}
		logger.info("uuid=ownerId=" + weixinButtonCriteria2.getUuid() + "的微信菜单数量有" + buttonList.size() + "条");
		String inviteCode = partner.getExtraValue(DataName.userInviteCode.toString());
		if(StringUtils.isBlank(inviteCode)){
			inviteCode = "";
		}
		//为确保更新，先删除该用户所有菜单
		this.deleteByUuid(weixinButtonCriteria.getUuid());
		for(WeixinButton oldButton : buttonList){
			if(oldButton.getParentButtonId() == 0){
				logger.info("1级微信菜单:" + oldButton);
				WeixinButton newButton = oldButton.clone();

				newButton.setWeixinButtonId(0);
				newButton.setUuid(weixinButtonCriteria.getUuid());
				
				if(newButton.getUrl() != null){
					newButton.setUrl(newButton.getUrl().replaceAll("\\$\\{username\\}", partner.getAuthKey()).replaceAll("\\$\\{inviteCode\\}", inviteCode));
				}

				int rs = this.insert(newButton);
				logger.info("从微信菜单[" + oldButton + "]新克隆了菜单[" + newButton + "],数据插入结果:" + rs);
				if(rs != 1){
					logger.error("无法创建新菜单[" + newButton + "]，返回是:" + rs);
					return new EisMessage(EisError.DATA_UPDATE_FAIL.id,"无法新增菜单");
				} else {
					messageService.sendJmsDataSyncMessage(null, "weixinButtonService", "insert", newButton);

				}

				//把新旧一级按钮的ID放入MAP供后面克隆二级菜单使用
				menuIdMap.put(oldButton.getWeixinButtonId(), newButton.getWeixinButtonId());
				newWeixinButtonList.add(newButton);
			}

		}
		//处理好所有一级菜单后，再处理二级菜单
		for(WeixinButton oldButton : buttonList){
			if(oldButton.getParentButtonId() > 0){
				logger.info("2级微信菜单:" + oldButton);
				WeixinButton newButton = oldButton.clone();

				newButton.setWeixinButtonId(0);
				newButton.setUuid(weixinButtonCriteria.getUuid());

				long newParentId = menuIdMap.get(oldButton.getParentButtonId());
				if(newParentId > 0){
					newButton.setParentButtonId(newParentId);
					logger.info("将新菜单[" + newButton + "]的父菜单设置为[" + newParentId + "]");
				} else {
					logger.error("没找到源菜单的parentId[" + oldButton.getParentButtonId() + "]对应的新parentId");
					return new EisMessage(EisError.REQUIRED_PARAMETER.id,"找不到新的父菜单");

				}
				if(newButton.getUrl() != null){
					newButton.setUrl(newButton.getUrl().replaceAll("\\$\\{username\\}", partner.getAuthKey()).replaceAll("\\$\\{inviteCode\\}", inviteCode));
			}
				int rs = this.insert(newButton);
				if(rs != 1){
					logger.error("无法创建新菜单[" + newButton + "]，返回是:" + rs);
					return new EisMessage(EisError.DATA_UPDATE_FAIL.id,"无法新增菜单");
				} else {
					messageService.sendJmsDataSyncMessage(null, "weixinButtonService", "insert", newButton);

				}
				logger.info("从权限[" + oldButton + "]新克隆了系统权限[" + newButton + "]");


				menuIdMap.put(oldButton.getWeixinButtonId(), newButton.getWeixinButtonId());
				newWeixinButtonList.add(newButton);

			}
		}
		EisMessage resultMsg = new EisMessage(OperateResult.success.id,"克隆完成");
		resultMsg.setAttachmentData("weixinButtonList", newWeixinButtonList);
		return resultMsg;
	}

	private void deleteByUuid(long uuid) {
		this.weixinButtonDao.deleteByUuid(uuid);
	}

	@Override
	public List<WeixinButton> generateTree(List<WeixinButton> weixinButtonList) {
		if(weixinButtonList == null){
			return null;
		}
		for(int i = 0; i< weixinButtonList.size(); i++){		
			for(int j = 0; j< weixinButtonList.size(); j++){
				if(weixinButtonList.get(j).getParentButtonId() == weixinButtonList.get(i).getWeixinButtonId()){
					//logger.info("Add sub node: " + plateNodeList.get(j).getName() +"["+ plateNodeList.get(j).getNodeId() +"] to parent node:" + plateNodeList.get(i).getName() +"["+ plateNodeList.get(i).getNodeId() + "].");
					if(weixinButtonList.get(i).getSubButton() == null){
						weixinButtonList.get(i).setSubButton(new ArrayList<WeixinButton>());
					}
					logger.debug("把二级菜单[" + weixinButtonList.get(j) + "]放入一级菜单:" + weixinButtonList.get(i));
					weixinButtonList.get(i).getSubButton().add(weixinButtonList.get(j));
				}
			}

		}
		ArrayList<WeixinButton> treeList = new ArrayList<WeixinButton>();

		for(WeixinButton weixinButton : weixinButtonList){	
			if(weixinButton.getParentButtonId() == 0){
				logger.debug("把一级菜单[" + weixinButton + "]放入tree");
				treeList.add(weixinButton);
			}
		}
		return treeList;
	}
}
