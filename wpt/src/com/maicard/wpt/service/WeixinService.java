package com.maicard.wpt.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;

import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.security.domain.User;
import com.maicard.wpt.domain.WeixinGroup;
import com.maicard.wpt.domain.WeixinMenu;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinRichTextMessage;

public interface WeixinService {

	//String makeJsSignature(Long timeStamp, String currentUrl, String nonceStr);
	
	String getSign(Map<String, Object> map);
	/**
	 * 卡券签名cardSign
	 * @return
	 */
	String cardSign(long timeStamp, String currentUrl, String nonceStr, String cardId,long ownerId);


	void updateUserInfo(User frontUser, String openId);
	
	User createUser(String openId, String identify, long ownerId);

	String getAppId(long ownerId);

	String getAppSecret(long ownerId);

	String getAppToken(long ownerId);


	/**
	 * 发送图文消息到指定用户，异步模式
	 */
	@Async
	void sendMessage(WeixinRichTextMessage weixinRichTextMessage);

	String getWeixinPayMechId(long ownerId);

	String processTextMessage(User frontUser, HttpServletRequest request, WeixinMsg message, long ownerId);
	
	/**
	 * 处理收到的卡券消息
	 * @param weixinMsg
	 */
	@Async
	void processCouponMessage(User frontUser, WeixinMsg weixinMsg);

	/**
	 * 发送消息，更新微信的卡券状态，如核销某个卡券
	 * @param coupon
	 */
	void sendCouponNotifyMessage(Coupon coupon);

	int importCardCode(long ownerId, String cardId, String... card);

	/*
	 * 自动添加对应的卡号到微信卡券库存并发送给指定openId的用户
	 */
	int addStockAndSendCustomCouponToWeixin(String openId, Coupon coupon);

	//int addCardStock(long ownerId, String weixinCardId, int amount);

	/*
	 * 发送指定卡号的微信卡券，通过客服接口发送给指定openId的用户
	 * 但不会自动创建库存
	 */
	int sendWeixinCouponFromCustomInterface(long ownerId, String openId, String cardId, String code);

	void getCardCodeStatus(long ownerId, String cardId, String code);

	String getAccessToken(long ownerId);

	int createCouponModel(CouponModel couponModel, String accessToken);

	String getCouponToken(long ownerId);

	boolean isWeixinAccess(HttpServletRequest request);

	boolean isWeixinAccess(Map<String, String> requestDataMap);

	void makeCouponJsSignature(String timeStamp, String nonceStr, long ownerId, CouponModel couponModel);

	String generateCouponListString(List<Coupon> couponList) ;

	String sendSubscribeMsg(HttpServletRequest request, WeixinMsg weixinMsg, long ownerId);

	/**
	 * 创建微信分组
	 * @param weixinGroup
	 * @return
	 */
	WeixinGroup createGroup(WeixinGroup weixinGroup);

	/**
	 * 把用户设置为属于指定分组
	 * @param frontUser
	 * @param weixinGroup
	 * @return
	 */
	int setUserGroup(User frontUser, WeixinGroup weixinGroup);

	/**
	 * 从微信端列出所有分组
	 * @param ownerId
	 * @return
	 */
	List<WeixinGroup> listWeixinGroup(long ownerId);

	WeixinMenu createMenu(WeixinMenu weixinGroup);

	void processScanMessage(User frontUser, WeixinMsg weixinMsg);

	void setTag(User frontUser, long outGroupId);

	void convertToWeixinCoupon(Coupon coupon, String openId, String timeStamp);

	/**
	 * 获取一个微信用户的信息
	 * 放入User对象
	 * @param openId
	 * @param ownerId
	 * @return
	 */
	User getUserInfo(String openId, long ownerId);

	//String getOpenIdByCode(String weixinCode, long ownerId);

	/**
	 * 根据提交的一组微信菜单，更新公众号一侧的菜单，并把外部菜单ID更新到本地<br/>
	 * 如果提交了accessToken，则按照该accessToken进行更新，如果未提供，则按照单平台模式获取accessToken<br/>
	 * 以适用于第三方平台和单平台<br/>
	 * 如果提交了menuTag，则创建基于情景模式的附加菜单，即对不同的分组用户显示的不同菜单
	 */
	//int createWeixinButton(List<WeixinButton> weixinButtonList, String accessToken, String menuTag, long ownerId);

}
