package com.maicard.core.ds;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * @author: iron
 * Date: 16-12-6
 * Time: 上午9:54
 * AOP根据类的注解设置数据源key
 */
public class DataSourceSwitcherAdapter extends MethodHandlerAspectInterceptorAdapter {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	static final String defaultSource = "default";

	@Override
	public Object doAround(ProceedingJoinPoint jp) throws Throwable {

		Class<?> target=jp.getTarget().getClass();
		try {
			DataSourceMapper dataSourceMapper=target.getAnnotation(DataSourceMapper.class);
			if(null!=dataSourceMapper && !StringUtils.isEmpty(dataSourceMapper.value())){
				DataSourceSwitcher.setDataSourceType(dataSourceMapper.value());
				logger.debug("Switching DataSource as annotation["+target.getName()+", type=" +(StringUtils.isEmpty(DataSourceSwitcher.getDataSourceType())? defaultSource :dataSourceMapper.value())+"]");
			} else {
				//否则应当执行到default
				logger.debug("Switching DataSource to default without annotation["+target.getName()+", type=" + defaultSource);
				DataSourceSwitcher.setDataSourceType(defaultSource);


			}
			return jp.proceed();
		} finally{//执行完成后将数据源标示清楚回复默认数据源

			logger.debug("Cleaned DataSource ["+target.getName()+", type=" +(StringUtils.isEmpty(DataSourceSwitcher.getDataSourceType())?"default":DataSourceSwitcher.getDataSourceType())+"]");
			DataSourceSwitcher.clear();
		}
	}
}
