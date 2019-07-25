package com.maicard.wpt.constants;

/**
 * 微信公众号平台及第三方公众号平台的常量
 *
 *
 * @author NetSnake
 * @date 2017年2月4日
 *
 */
public interface WeixinConfig {
	public static final String API_HOST = "api.weixin.qq.com";
	public static final String OPEN_HOST = "open.weixin.qq.com";
	public static final int API_PORT = 443;
	public static final String API_PREFIX = 	"https://" + API_HOST;
	public static final String OPEN_HOST_PREFIX = 	"https://" + OPEN_HOST;
	
	public static final String WEIXIN_PLATFORM_CACHE_KEY = "WEIXIN_PLATFORM";
	
	/**
	 * 多少分钟多少秒时，检查acessToken是否有效
	 */
	public static final int CHECK_ACCESS_TOKEN_OFFSET = 5;


	/**
	 * 当不带任何后缀时为一个表，存放第三方平台模式下，所有客户与APPID的对应数据缓存
	 * 当加上#客户UUID的后缀时为另外的单独表，缓存客户的认证数据，如ACCESS_TOKEN、REFRESH_TOKEN
	 */
	public static final String WEIXIN_CLIENT_APPID_MAP = "WEIXIN_CLIENT_APPID_MAP";

	public static final String VERIFY_TICKET_CACHE_KEY = "VERIFY_TICKET";
	public static final String ACCESS_TOKEN_CACHE_KEY = "ACCESS_TOKEN";
	public static final String AUTH_ACCESS_TOKEN_CACHE_KEY = "AUTH_ACCESS_TOKEN";
	public static final String AUTH_REFRESH_TOKEN_CACHE_KEY = "AUTH_REFRESH_TOKEN";
	public static final String COUPON_TOKEN_CACHE_KEY = "COUPON_TOKEN";

	public static final String PREAUTH_CODE_CACHE_KEY = "PREAUTH_CODE";
	public static final String REFRESH_TOKEN_CACHE_KEY = "REFRESH_TOKEN";
	
	/**
	 * 当不确定客户消息如何返回时，默认返回的消息
	 */
	public static final String DEFAULT_NULL_REPLAY_MESSAGE = "对不起，我不知道您说的什么";
	

	//批量发送消息时的延迟等待时间
	public final int BATCH_SEND_DELAY_SEC = 3;


; 
}
