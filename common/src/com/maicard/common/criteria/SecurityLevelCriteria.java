package com.maicard.common.criteria;

import com.maicard.common.base.Criteria;
import com.maicard.standard.CommonStandard;

public class SecurityLevelCriteria extends Criteria {

	private static final long serialVersionUID = 8709281822687728255L;

	/*
	 * 测试开发环境，建议不做任何安全措施，登录次数也不做任何限制
	 */
	public static final int SECURITY_LEVEL_DEMO = 0;	//, "开发环境",-1,-1,-1),
	
	/*
	 * 正常级别，登录次数有限制，密码长度不限制
	 * 系统操作日志记录等级为POST
	 */
	public static final int  SECURITY_LEVEL_NORMAL = 1;		//,"一般环境",-1,-1,-1),
	
	/*
	 * 严格级别，
	 * 限制登录次数、密码长度和强度有要求，10分钟之内如果登录错误超过3次则拒绝登录登录失败后锁定一定时间
	 * properties文件加密
	 * 
	 */
	public static final int  SECURITY_LEVEL_STRICT = 2;		//"严格环境",600,3,600),
	
	/*
	 * 等保三级，除更低级别的要求以外，还要求：
	 * 后台登陆必须使用X509证书客户端双向认证
	 * 数据库中的系统配置config数据进行加密
	 * 启用二级密码
	 * 系统操作日志记录等级提高
	 * 禁止发布的内容中有外部链接
	 * 缩短用户离开状态的等待时间
	 * 
	 */
	public static final int SECURITY_LEVEL_DB3 = 3;	//"等保三级环境", 1800, 3, 3600);	
	
	public static final String cacheName = CommonStandard.cacheNameSupport;
	public static final String cachePrefix = "SecurityLevel";
	
	
}
