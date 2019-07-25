package com.maicard.common.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.common.base.BaseService;
import com.maicard.common.domain.Server;
import com.maicard.common.processor.LocalCachedBean;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.ServerService;
import com.maicard.common.util.IpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.common.util.SystemUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.KeyConstants;
import com.maicard.standard.ServiceStatus;

public class ServerServiceImpl extends BaseService implements ServerService {

	@Resource
	private ApplicationContextService applicationContextService;
	@Resource
	private CenterDataService centerDataService;

	@Resource
	private MessageService messageService;

	@Resource
	private ConfigService configService;

	SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	String tableName = KeyConstants.SERVER_TABLE_NAME;

	static final String CENTER_CMD_TABLE = "CENTER_CMD";

	final int SERVER_FRESH = 120;
	final int SERVER_TIMEOUT = SERVER_FRESH * 2;

	private static boolean  firstRunning = true;


	ObjectMapper om = JsonUtils.getNoDefaultValueInstance();

	@Override
	public List<Server> list(){


		List<Server> list =  centerDataService.getHmValues(tableName);
		if(list == null || list.size() < 1){
			return null;
		}
		//排序
		Map<String, Server> serverMap = new HashMap<String,Server>();
		for(Server server : list){
			serverMap.put(server.getSystemCode() + "#" + server.getSystemServerId(), server);
		}
		List<String> keys = new ArrayList<String>(serverMap.keySet());
		Collections.sort(keys);
		list = new ArrayList<Server>();
		for(String key : keys){
			list.add(serverMap.get(key));
		}
		return list;

	}

	@Override
	public Server select(String systemCode, int serverId) {
		String key = systemCode + "#" + serverId;
		Server server =  centerDataService.getHmValue(tableName, key);
		if(server == null) {
			logger.error("系统缓存表:{}中找不到指定的服务器实例:{}", tableName, key);
		}
		return server;
	}


	private String getBaseDir(Server server){
		String baseDir = null;
		if(server.getContextPath() == null){
			baseDir =  applicationContextService.getServletContext().getRealPath("/").replaceAll("/$", "");
		} else {
			baseDir = server.getContextPath();
		}
		String[] data = baseDir.split("/");
		int offset = data.length - 2;
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < offset; i++){
			sb.append(data[i]).append(File.separator);
		}
		baseDir = sb.toString();
		return baseDir;
	}

	@Override
	@Async
	public void updateSvn(Server server) {
		String baseDir = this.getBaseDir(server);
		if(StringUtils.isBlank(baseDir)){
			logger.error("无法解析服务器[" + server + "]的目录");
			return;
		}


		String cmd = baseDir + "/../update.sh " + baseDir;
		server.setCmd(cmd);
		server.setKeepOldStatusAfterExec(true);
		this.exec(server);
		server.setCmd(null);

	}

	@Override
	@Async
	public void install(Server server) {
		String baseDir = this.getBaseDir(server);
		if(StringUtils.isBlank(baseDir)){
			logger.error("无法解析服务器[" + server + "]的目录");
			return;
		}
		String runningUser = System.getProperty("user.name");
		logger.debug("当前运行程序的用户是:" + runningUser);
		String cmdContent = "cd /home/" + runningUser + "/svn/DEVELOP/;./target/script/install.sh " + baseDir;


		server.setCmd(cmdContent);
		server.setKeepOldStatusAfterExec(true);
		this.exec(server);
		server.setCmd(null);


	}


	@Override
	@Async
	public void restart(Server server) {
		String baseDir = this.getBaseDir(server);
		if(StringUtils.isBlank(baseDir)){
			logger.error("无法解析服务器[" + server + "]的目录");
			return;
		}
		String cmd = "nohup 2>&1 " + baseDir + "/restart.sh > ~/eis.log &";


		server.setCmd(cmd);
		server.setKeepOldStatusAfterExec(false);
		this.exec(server);
		server.setCmd(null);


	}

	private void delete(Server server) {
		String key = server.getSystemCode() + "#" + server.getSystemServerId();
		try {
			centerDataService.setHmValue(tableName, key, null, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int refresh(Server server){

		server.setUpdateTime(new Date());
		server.setIp(IpUtils.getComputerIp());
		String key = server.getSystemCode() + "#" + server.getSystemServerId();
		try {
			if(firstRunning){
				server.setBootTime(new Date());
				firstRunning = false;
			} else {
				Server _oldServer = centerDataService.getHmValue(tableName, key);
				if(_oldServer != null){
					server.setBootTime(_oldServer.getBootTime());
					if(server.getCurrentStatus() == 0){
						server.setCurrentStatus(_oldServer.getCurrentStatus());
					}
				} else {
					server.setBootTime(new Date());
				}
			}
			if(server.getCurrentStatus() == 0){
				server.setCurrentStatus(ServiceStatus.opening.id);
			}
			logger.info("刷新系统服务器:" + server + ",启动时间:" + sdf.format(server.getBootTime()));
			centerDataService.setHmValue(tableName, key, server, SERVER_TIMEOUT);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int checkCenterCmd(Server server){
		final String key = "Server#" + server.getSystemCode() + "#" + server.getSystemServerId();
		try {
			String cmd = centerDataService.getHmPlainValue(CENTER_CMD_TABLE, key);
			if(cmd == null) {
				logger.debug("没有新的中心命令:{}", key);
				return 0;
			}
			logger.debug("执行中心命令:{}", cmd);
			centerDataService.setHmPlainValue(CENTER_CMD_TABLE, key, null, 0);
			String[] cmds = cmd.split(",");
			for(String command : cmds) {
				if(command.equalsIgnoreCase("RELOAD_LOCAL_CACHE")) {
					String[] names = applicationContextService.getBeanNamesForType(LocalCachedBean.class);
					if(names == null || names.length < 1) {
						return 0;
					}
					for(String beanName : names) {
						LocalCachedBean bean = applicationContextService.getBeanGeneric(beanName);
						if(bean == null) {
							logger.error("找不到bean:{}", beanName);
						} else {
							bean.reloadCache();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return 0;
	}


	/**
	 * 定时刷新服务器信息
	 */
	@Override
	@Scheduled(initialDelay=5,fixedRate=SERVER_FRESH * 1000)  
	public void run(){
		Server server = new Server();
		server.setSystemCode(configService.getSystemCode());
		server.setSystemServerId(configService.getServerId());
		String contextPath = null;
		for(int i = 0; i < 10; i++){
			if(applicationContextService == null || applicationContextService.getServletContext() == null || applicationContextService.getServletContext().getRealPath("/") == null){
				logger.warn("应用环境还未就绪");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				contextPath = applicationContextService.getServletContext().getRealPath("/").replaceAll("/$", "");
				break;
			}
		}
		server.setContextPath(contextPath);
		this.refresh(server);
		this.checkCenterCmd(server);
		//		this.restart(server);

	}

	@Override
	public void exec(Server server){
		String baseDir = this.getBaseDir(server);
		if(StringUtils.isBlank(baseDir)){
			logger.error("无法解析服务器[" + server + "]的目录");
			return;
		}
		String cmd = server.getCmd();
		if(StringUtils.isBlank(cmd)){
			logger.error("未指定要执行的命令");
		}
		logger.info("3当前服务器[" + server + "]基础目录是:" + baseDir);

		File file = new File(baseDir);
		if(!file.exists() || !file.isDirectory()){
			if(server.getSyncFlag() == 0){
				logger.debug("服务器上找不到指定的目录:" + baseDir + ",发送远程命令");
				messageService.sendJmsDataSyncMessage(null,"serverService","exec", server);
			} else {
				logger.error("服务器上找不到指定的目录:" + baseDir + ",且当前是远程更新命令");
			}
			return;
		}
		int serverId = configService.getServerId();

		logger.info("本地服务器ID是:" + serverId + ",请求执行服务器[" + server + "]的ID是:" + server.getSystemServerId());

		/*		if(serverId != server.getSystemServerId()){
			logger.info("本地服务器ID是:" + serverId + ",请求执行服务器[" + server + "]的ID是:" + server.getSystemServerId() + ",忽略");
			return;
		}*/
		logger.info("4在本地执行服务器[" + server + "]的命令:" + cmd);

		int oldStatus = server.getCurrentStatus() == 0 ? ServiceStatus.opening.id : server.getCurrentStatus();

		server.setCurrentStatus(ServiceStatus.updating.id);
		this.refresh(server);



		SystemUtils.execCommand(cmd, true);
		if(server.isKeepOldStatusAfterExec()){
			server.setCurrentStatus(oldStatus);
		}
		server.setCmd(null);
		this.refresh(server);


		return;
	}

	/**
	 * Tomcat关闭时尝试删除服务器信息
	 */
	@PreDestroy
	public void onDestroy(){
		Server server = new Server();
		server.setSystemCode(configService.getSystemCode());
		server.setSystemServerId(configService.getServerId());
		server.setContextPath(applicationContextService.getServletContext().getRealPath("/").replaceAll("/$", ""));
		this.delete(server);
	}


}
