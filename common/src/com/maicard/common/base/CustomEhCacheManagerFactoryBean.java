package com.maicard.common.base;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import java.lang.reflect.Method;

import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;


import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.maicard.standard.CommonStandard;

/**
 * 增加了对terracotta的配置支持
 * 可用于ehcache 2.7.4,但2.10.1还有待观察, NetSnake, 2016-04-26
 * 
 * 用于在ehcache.xml配置文件中加入三个配置参数<br/>
 * 以方便由外部注入这三个配置，这样可以对多个项目使用一个统一的ehcache.xml<br/>
 * 扩展自Spring 3.2.xEhCacheManagerFactoryBean<br/>
 * Spring 4.x工作方式有所变化，应当不能用于Spring 4.x
 *
 * @author NetSnake
 * @date 2015年12月31日
 *
 */
public class CustomEhCacheManagerFactoryBean extends EhCacheManagerFactoryBean {

	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// Check whether EhCache 2.1+ CacheManager.create(Configuration) method is available...
	private static final Method createWithConfiguration =
			ClassUtils.getMethodIfAvailable(CacheManager.class, "create", Configuration.class);



	private Resource configLocation;

	private boolean shared = false;

	private String cacheManagerName;

	private CacheManager cacheManager;
	
	private String multicastGroupAddress = "230.0.0.1";
	
	private String multicastGroupPort = "4444";
	
	private String timeToLive = "1";
	
	private String terracottaUrl;

	@Override
	public void afterPropertiesSet() throws CacheException {
		logger.info("初始化可配置Ehcache Manager");
		InputStream is = null;
		try {
			is = (this.configLocation != null ? this.configLocation.getInputStream() : null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(is != null){
			//读取传入的配置文件内容，并替换其中的参数模版
			StringBuffer sb = new StringBuffer();
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				} 
				br.close();

			}catch(IOException e){
				e.printStackTrace();
			}
			String content = sb.toString();
			
			if(content.indexOf("${multicastGroupAddress}") > 0){
				content = content.replaceAll("\\$\\{multicastGroupAddress\\}", this.multicastGroupAddress);
			}
			if(content.indexOf("${multicastGroupPort}") > 0){
				content = content.replaceAll("\\$\\{multicastGroupPort\\}", this.multicastGroupPort);
			}
			if(content.indexOf("${timeToLive}") > 0){
				content = content.replaceAll("\\$\\{timeToLive\\}", this.timeToLive);
			}
			//对terracotta的支持
			if(content.indexOf("${terracottaUrl}") > 0){
				content = content.replaceAll("\\$\\{terracottaUrl\\}", this.terracottaUrl);
			}
			//content = content.replaceAll("\\$\\{multicastGroupAddress\\}", this.multicastGroupAddress).replaceAll("\\$\\{multicastGroupPort\\}", this.multicastGroupPort).replaceAll("\\$\\{timeToLive\\}", this.timeToLive);
			logger.debug("Ehcache多播配置:multicastGroupAddress=" + this.multicastGroupAddress + ",multicastGroupPort=" + this.multicastGroupPort + ",timeToLive=" + this.timeToLive);
			
			//String terracottaPattern = "\\$\\{terracottaUrl\\}";
			//content = content.replaceAll(terracottaPattern, this.terracottaUrl);
			logger.debug("转换后的ehcache配置文件:" + content);
			try {
				is = new ByteArrayInputStream(content.getBytes(CommonStandard.DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}   
		}

		try {
			// A bit convoluted for EhCache 1.x/2.0 compatibility.
			// To be much simpler once we require EhCache 2.1+
			if (this.cacheManagerName != null) {
				if (this.shared && createWithConfiguration == null) {
					// No CacheManager.create(Configuration) method available before EhCache 2.1;
					// can only set CacheManager name after creation.
					this.cacheManager = (is != null ? CacheManager.create(is) : CacheManager.create());
					//this.cacheManager.(this.cacheManagerName);
				}
				else {
					Configuration configuration = (is != null ? ConfigurationFactory.parseConfiguration(is) :	ConfigurationFactory.parseConfiguration());
					configuration.setName(this.cacheManagerName);
					if (this.shared) {
						this.cacheManager = (CacheManager) ReflectionUtils.invokeMethod(createWithConfiguration, null, configuration);
					}
					else {
						this.cacheManager = new CacheManager(configuration);
					}
				}
			}
			// For strict backwards compatibility: use simplest possible constructors...
			else if (this.shared) {
				this.cacheManager = (is != null ? CacheManager.create(is) : CacheManager.create());
			}
			else {
				this.cacheManager = (is != null ? new CacheManager(is) : new CacheManager());
			}
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void destroy() {
		logger.info("Shutting down EhCache CacheManager");
		this.cacheManager.shutdown();	}

	@Override
	public CacheManager getObject() {
		return this.cacheManager;
	}

	@Override
	public Class<? extends CacheManager> getObjectType() {
		return (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}

	@Override
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	@Override
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public void setMulticastGroupAddress(String multicastGroupAddress){
		this.multicastGroupAddress = multicastGroupAddress;
	}

	public String getMulticastGroupAddress() {
		return multicastGroupAddress;
	}

	public void setMulticastGroupPort(String multicastGroupPort) {
		this.multicastGroupPort = multicastGroupPort;
	}

	public void setTimeToLive(String timeToLive) {
		this.timeToLive = timeToLive;
	}

	public void setTerracottaUrl(String terracottaUrl) {
		this.terracottaUrl = terracottaUrl;
	}

}
