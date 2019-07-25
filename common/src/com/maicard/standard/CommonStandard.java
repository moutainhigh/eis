package com.maicard.standard;

import java.math.BigDecimal;



public interface CommonStandard {

	//原生field
	public static final String COLUMN_TYPE_NATIVE = "native";
	//扩展数据
	public static final String COLUMN_TYPE_EXTRA = "extra";


	//获取数据时的级别
	public static enum DataFetchMode {
		simple, normal, full, real
	}

	//可能有危险的字符串，禁止输入和提交
	//	public static final String BAD_CHAR_PATTERN = "<|>|'|%|\\(\\)|\\&|\\+|\\|'";
	public static final String BAD_CHAR_IGNORE = "\\|";
	public static final String BAD_CHAR_PATTERN = "<|>|\"|'|%|\\(|\\)|&|\\+|'";

	//默认theme
	public static final String DEFAULT_THEME_NAME = "basic";

	//默认编码
	public static final String DEFAULT_ENCODING = "UTF-8";

	//默认文件名后缀
	public static final String DEFAULT_PAGE_SUFFIX = ".shtml";

	//未指定查询时间的时候，默认查询天数
	public static final int DEFAULT_QUERY_DAY = 7;




	//Java堆栈数组下标
	public static final int jvmStackTraceOffset = 1;

	//Session相关
	public static final String sessionTokenName = "eis_passport";
	public static final String sessionUsername = "eis_username";
	public static final String sessionCaptchaName = "eis_captcha";
	public static final String sessionReturnUrlName = "eis_return_url";
	public static final String sessionCartName = "cart";
	public static final int sessionDefaultTtl = 36000; //Session默认的超时秒数

	public static final String bbsKey = "ALla2lllaF_2lFl8";
	public static final String previewToken = "eisPreviewToken";
	public static final String previewKey = "VenyecfokHufkaph";

	public static final String requestUserConfigPrefix = "eis_uc_";

	//用户校验相关
	public static final String mailActiveKey = "5fa_20023aAF!441";
	public static final String mailBindKey = "8AF2_21bnalo29Fh";
	public static final String mailFindPasswordKey = "uFao@ooF_xxsaaf1";

	public static final int accountPayTypeId = 5;

	public static final String apiMessageView = "message/apiMessage";
	public static final String backMessageView = "message/backMessage";
	public static final String frontMessageView = "message/frontMessage";
	public static final String partnerMessageView = "common/message/partnerMessage";


	public static final String bossPrefix = "/boss";
	public static final String partnerPrefix = "/partner";
	public static final String contentPrefix = "/content";

	/**
	 * 默认四舍五入
	 */
	public static final int moneyRoundType = BigDecimal.ROUND_HALF_UP;
	
	/**
	 * 默认小数点位数
	 */
	public static final int moneyRoundLength = 2;

	public static final int adminUuid = 2000001;
	//public static final String documentSplitTag = "<hr style=\"width: 100%; height: 2px;\">";
	public static final String documentSplitTag = "<hr>";

	//Cookie最长有效期1年
	public static final int COOKIE_MAX_TTL = 3600 * 24 * 365;
	//二次认证的Cookie名
	public static final String COOKIE_SEC_AUTH_COOKIE_NAME = "eis_sec_auth_data";
	//二次认证的默认有效期
	public static final int COOKIE_SEC_AUTH_COOKIE_DEFAULT_TTL = 1800;
	//重定向的COOKIE名称
	public static final String COOKIE_REDIRECT_COOKIE_NAME = "eis_redirect_url";




	//缓存名称
	public static final String cacheNameDocument = "eis";
	public static final String cacheNameSupport = "eis";
	public static final String cacheNameTransaction = "ts";
	public static final String cacheNameUser = "user";
	public static final String cacheNameProduct = "product";
	public static final String cacheNameValidate = "va";
	public static final String cacheNameLocal = "local";
	public static final String cacheNamePermannetCart = "permanetCart";


	public static final String payOrderSuffix = "0";
	public static final String chargeOrderSuffix = "1";

	public static final int transactionTtl = 300;
	public static final int transactionThreadSleepTime = 10;
	public static final int transactionMaxRetry = 30;

	public static final int itemMoveToHistoryDay = 2;  //多少天以前的item数据需要被移动到历史表
	public static final int payMoveToHistoryDay = 2; //多少天以前的pay数据需要被移动到历史表


	public static final int matchConditionStrict		= 100050;	//严格匹配条件;4;匹配方式
	public static final int matchConditionLoose		= 100051;	//宽松匹配条件;4;匹配方式
	public static final int matchConditionNone		= 100052;	//不检查条件;4;匹配方式

	public static final int operateStatusNew		= 102009;	//新;6;操作状态
	public static String tagSplit = "\\s+|\\pP+";
	public static final String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

	public static final String[] ignoreStaticizeNode = new String[]{"pay"};

	public static final String implBeanNameSuffix = "Impl";

	public static final long smsSendInterval = 60;

	public static final int messageWaitingMaxCount = 30; //等待消息的最大查询次数
	public static final int messageWaitingInterval = 5; //等待消息时的间隔时间秒
	public static final int messageWaitingMaxTime = 60; //等待消息时的最长时间秒

	public static final String defaultUserRegisterWelcomeMailContent = "欢迎注册，请点击以下链接将您的帐号绑定到邮箱<a href=\"${bindMailUrl}\">${bindMailUrl}</a>";


	public static final String statHourFormat = "yyyyMMddHH";
	public static final String statDayFormat = "yyyyMMdd";


	public static final String orderIdDateFormat = "yyyyMMddHHmmss";

	public static final String defaultCookiePolicy = "domain";
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	public static final long batchQueryMaxPeriodSecond = 2592000;//批量查询允许的最长时间间隔秒数，为30天


	public static final String EXTRA_DATA_OPEN_PATH = "open";			//文档上传的附件保存目录
	public static final String EXTRA_DATA_LOGIN_PATH = "login";	//登录用户专用附件保存目录
	public static final String EXTRA_DATA_SUBSCRIBE_PATH = "subscribe";		//订阅用户专用附件保存目录
	public static final String EXTRA_DATA_TEMP = "temp";	//临时上传文件目录
	

	public static final String signDateFormat = "yyyyMMdd";

	public static final String[] partnerViewConfigCategory = new String[]{
			ConfigCategory.business.getCode(),ConfigCategory.security.getCode(),ConfigCategory.site.getCode(),ConfigCategory.transaction.getCode()
	};

	//如需改变该配置，需要在catalina.sh中配置对应的JAVA_OPTS
	public static final String EIS_SECURITY_ENV_NAME = "eis.security_level";
	public static final String PARTNER_LOGIN_URL = "/user/login" + DEFAULT_PAGE_SUFFIX;
	public static final String BOSS_LOGIN_URL = "/user/login" + DEFAULT_PAGE_SUFFIX;
	public static final String FRONT_LOGIN_URL = "/content/user/login" + DEFAULT_PAGE_SUFFIX;
	public static final String DEFAULT_PAY_NOTIFY_TEMPLATE = "${hostUrl}/payNotify/${payMethodId}.json";
	public static final long SSO_TIMESTAMP_DEFAULT_TTL = 600;


	public static final int DEFAULT_PARTNER_ROWS_PER_PAGE = 10;
	public static final int DEFAULT_FRONT_ROWS_PER_PAGE = 10;

	public static final int staticizeWaitingSec = 10;		//静态化之前等待多少秒

	public static final int JMS_MAX_RETRY = 3;	//进行JMS等待的时候，最大重试次数
	public static final int defaultLanguageId = 170001;
	public static final long DISTRIBUTED_LOCK_WAIT_MS = 50;		//分布式加锁等待时间,毫秒
	public static final long DISTRIBUTED_DEFAULT_LOCK_SEC = 3;		//分布式锁的默认存活秒数
	public static final long WAIT_DATA_SYNC_SEC = 10;				//等待前后端数据进行同步的秒数
	public static final long DISTRIBUTED_DEFAULT_SET_SEC = 120;		//分布式存放数据的默认存活描述
	public static final int DISTRIBUTED_LOCK_RETRY_TIME = 30;			//分布式锁的重试次数
	public static final long UPDATE_LOCK_DEFAULT_LOCK_SEC = 3600;	//编辑锁定的默认锁定时长
	public static final int documentDefaultStatus = 130005;
	public static final String EIS_STATICIZE_TOKEN = "EIS_STATICIZE_TIME";
	public static final int TOKEN_RENEW_INTERVAL = 60 * 5; //	用户令牌的最短刷新
	public static final String DEFAULT_MESSAGE_PREFIX = "DataName";
	public static final long MAX_WORLD_MESSAGE_COUNT = 100000000;	//每天默认能发送的世界消息条数
	public static final long USER_MQ_READ_INTERVAL = 300;		//用户队列的消息获取间隔时间为300毫秒
	
	/**
	 * 自动产生文件时，文件名中的日期部分格式
	 */
	public static final String fileNameDatePattern = "yyyyMMdd";


	/**
	 * 默认数值显示格式,二位小数
	 */
	public static final String DEFAULT_DECIMAL_FORMAT = "#.##";
	
	/**
	 * 存放EisSocket的缓存名称
	 */

	public static final String ES_SESSION_MAP_NAME = "ES_SESSION_MAP";
	
	/**
	 * 存放用户UUID与EisSession ID的对应关系
	 */
	public static final String ES_UUID_SESSION_MAP = "ES_UUID_SESSION_MAP";
	
	public static final String DEFAULT_STRICT_AUTH_MODE = "payPassword";
	public static final long SESSION_SAVE_INTERVAL = 5;
	
	/**
	 * 缓存的最长有效期，1年
	 */
	public static final long CACHE_MAX_TTL =  31536000;
	
	/**
	 * 这个毫秒数内没有任何动作并且没有登录的Session应当被清除
	 */
	public static final long ES_SESSION_NO_USER_IDLE_MS = 300 * 1000;
	
	
	/**
	 * 日志中写入消息时的最大长度，防止一个日志过长
	 */
	public static final int MESSAGE_BRIEF_LENGTH = 200;
	

	






}