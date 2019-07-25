package com.maicard.wpt.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.maicard.money.domain.Coupon;
import com.maicard.money.domain.CouponModel;
import com.maicard.security.domain.User;
import com.maicard.wpt.domain.WeixinButton;
import com.maicard.wpt.domain.WeixinGroup;
import com.maicard.wpt.domain.WeixinMsg;
import com.maicard.wpt.domain.WeixinPlatformInfo;

public interface WeixinPlatformService {

	String getPreAuthCode(long ownerId);

	String decrypt(String message, long ownerId) throws Exception;

	void receiveAuthMessage(String cryptText, long ownerId) throws Exception;

	String getVerifyTicket(long ownerId);

	String getPlatformAccessToken(long ownerId);

	String getPlatformAppId(long ownerId);

	int updateClientInfo(User partner, String authCode, int expire);

	int updateClientAccessToken(User partner, String authCode, int expire);

	String getClientAccessToken(long sitePartnerId);

	/**
	 * 根据主机名来确定是哪个合作方的公众号<br/>
	 * 并以此来获取公众号的accessToken
	 * @param host
	 * @param ownerId
	 * @return
	 */
	String getClientAccessTokenByHost(String host, long ownerId);

	User getPartnerByHost(String host, long ownerId);

	/**
	 * 页面生成JS签名
	 * @param timeStamp
	 * @param currentUrl
	 * @param nonceStr
	 * @param ownerId
	 * @param callCount 方法内部递归调用时使用，外部调用永远应设置为0
	 * @return
	 */
	String makeJsSignature(long timeStamp, String currentUrl, String nonceStr, long ownerId, int callCount);

	/**
	 * 获取单平台模式（非第三方平台）下的系统公众号的accessToken
	 * @param ownerId
	 * @return
	 */
	String getSingleAccessToken(long ownerId);

	String getOpenIdByCode(String weixinCode, long uuid, long ownerId);

	User createUser(String openId, String identify,  long parnterUuid, long ownerId);

	WeixinGroup createGroup(WeixinGroup weixinGroup);

	/**
	 * 从微信端更新用户信息
	 * @param openId
	 * @param partnerUuid
	 * @param ownerId
	 * @param callCount 方法内部递归调用时使用，外部调用永远应设置为0
	 * @return
	 */
	User getUserInfo(String openId, long partnerUuid, long ownerId, int callCount);

	void updateUserInfo(User frontUser, String openId, long sitePartnerId);

	int setUserGroup(User frontUser, WeixinGroup weixinGroup);

	void setTag(User frontUser, long tagId);

	int addCardStock(long partnerId, long ownerId, String weixinCardId, int amount);

	String getCouponToken(String clientAppId, long partnerId, long ownerId);

	void convertToWeixinCoupon(Coupon coupon, long partnerId, String openId, String timeStamp);

	int createCouponModel(CouponModel dynamicCouponModel, String couponAccessToken);

	int importCardCode(long sitePartnerId, long ownerId, String cardId, String... card);

	/**
	 * 返回指定云ID即单用户模式下的微信平台配置
	 * @param ownerId
	 * @return
	 */
	WeixinPlatformInfo getSingleWeixinPlatformInfo(long ownerId);

	/**
	 * 返回指定合作商户的微信平台配置
	 * @param sitePartnerId
	 * @return
	 */
	String getClientAppId(long sitePartnerId);

	/**
	 * 为卡券签名
	 * @param callCount 方法内部递归调用时使用，外部调用永远应设置为0
	 */
	String cardSign(long timeStamp, String currentUrl, String nonceStr, String couponCode, long sitePartnerId, long ownerId, int callCount);

	/**
	 * 读取指定商户的公众号菜单
	 * @param menuTag
	 * @param sitePartnerId
	 * @param ownerId
	 * @return
	 */
	List<WeixinButton> getWeixinButton(String menuTag, long sitePartnerId, long ownerId);

	/**
	 * 创建公众号菜单
	 * @param weixinButtonList
	 * @param menuTag
	 * @param sitePartnerId
	 * @param ownerId
	 * @return
	 */
	int createWeixinButton(List<WeixinButton> weixinButtonList, String menuTag, long sitePartnerId, long ownerId);

	/**
	 * 响应用户发送的消息或点击事件
	 * @param encrypt 
	 * @throws Exception 
	 */
	String processTextMessage(User frontUser, HttpServletRequest request, WeixinMsg message, long sitePartnerId,	long ownerId);

	/**
	 * 响应用户关注的消息
	 * @param frontUser 
	 * 
	 * 
	 * @param request
	 * @param weixinMsg
	 * @param sitePartnerId
	 * @param ownerId
	 * @return
	 */
	String processSubscribe(User frontUser, HttpServletRequest request, WeixinMsg weixinMsg, long sitePartnerId, long ownerId);

	String processScanMessage(User frontUser, HttpServletRequest request, WeixinMsg weixinMsg, long sitePartnerId,			long ownerId);

	void processCouponMessage(User frontUser, WeixinMsg weixinMsg);

	String encryptMsg(String message, String timeStamp, String nonce, long ownerId) throws Exception;

	long getSitePartnerId(String clientAppId, long ownerId);

	int sendCustomServiceMessage(String message, int messageType, long sitePartnerId, long ownerId);

	void deleteAccessToken(long sitePartnerId, long ownerId);

	/**
	 * 获取微信公众号的客服列表
	 * @param accessToken
	 * @return
	 */
	String getCsList(String accessToken);

	/**
	 * 添加一个公众号客服帐号
	 * @param accessToken
	 * @return
	 */
	int addCs(String csName, String csPassword, String nickName, String accessToken, long ownerId);





}
