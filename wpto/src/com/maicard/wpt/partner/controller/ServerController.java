package com.maicard.wpt.partner.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.domain.Server;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.ServerService;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.standard.OperateResult;
import com.maicard.standard.ServiceStatus;
import com.maicard.standard.SecurityStandard.UserTypes;

import static com.maicard.standard.CommonStandard.partnerMessageView;

import java.util.List;

@RequestMapping(value = "/server")
public class ServerController  extends BaseController{

	@Resource
	private AuthorizeService authorizeService;

	@Resource
	private ServerService serverService;

	@Resource
	private ConfigService configService;

	@Resource
	private CertifyService certifyService;

	@RequestMapping()
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		final String view = "common/server/index";
		User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());

		if(partner == null){
			//无权访问
			throw new UserNotFoundInRequestException("您可能尚未登录，或会话已过期，建议您刷新页面并重新登录。");
		}
		List<Server> serverList = serverService.list();
		if(serverList == null || serverList.size() < 0){
			logger.error("未找到任何服务器");
			return view;

		}
		authorizeService.writeOperate(partner, serverList);

		map.put("rows", serverList);
		return view;
	}



	@RequestMapping(value = "/update", method=RequestMethod.POST)
	@AllowJsonOutput
	public String call(HttpServletRequest request, HttpServletResponse response, ModelMap map, String mode) throws Exception {

		int serverId = ServletRequestUtils.getIntParameter(request, "serverId",0);
		
		Server server = serverService.select(configService.getSystemCode(), serverId);
		if(server == null){
			logger.error("找不到指定的服务器:" + configService.getSystemCode() + "#" + serverId);
		}
		if(server.getCurrentStatus() != ServiceStatus.opening.id){
			logger.warn("服务器:" + server.getSystemCode() + "#" + server.getSystemServerId() + "的状态为:" + server.getCurrentStatus() + ",不是出于正常状态:" + ServiceStatus.opening.id + ",暂停执行");
			map.put("message", new EisMessage(OperateResult.failed.id, server.getSystemCode() + "系统的" + server.getSystemServerId() + "#服务器当前不允许执行该操作"));
			return partnerMessageView;
		}
		logger.info("要求向服务器[" + server + "]执行指令:" + mode);
		server.setSyncFlag(0);
		if(mode.equals("restart")){
			serverService.restart(server);
		} else if(mode.equals("update")){
			serverService.updateSvn(server);
		} else if(mode.equals("install")){
			serverService.install(server);
		} else {
			logger.error("未知的指令:" + mode);
		}
		map.put("message", new EisMessage(OperateResult.accept.id,"已调用"));
		return partnerMessageView;
	}
}
