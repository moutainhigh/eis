package com.maicard.common.aspect;

import com.maicard.common.base.BaseService;

//@Aspect
public class RedisMonitorAspect extends BaseService{
	
	/*@Autowired(required=false)
	private StringRedisTemplate redisTemplate;
	
	
	@After("execution(* com.maicard.common.service.impl.CenterDataServiceImpl.*(..))")
	public void after(JoinPoint joinPoint){
		if(redisTemplate == null){
			logger.warn("未注入redisTemplate");
			return;
		}
		
		Jedis jedis = (Jedis)redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
		logger.info("执行redis服务,bean：" + joinPoint.getTarget().getClass().getSimpleName() + ", method:" +joinPoint.getSignature().getName() + ",redis信息:" + jedis.info("clients"));
	}*/

}
