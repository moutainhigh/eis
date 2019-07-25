package com.maicard.wpt.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.common.util.JsonUtils;
import com.maicard.standard.DataName;
import com.maicard.wpt.service.TulingRobotService;

@Service
public class TulingRobotServiceImpl extends BaseService implements TulingRobotService {
	
	@Resource
	private ConfigService configService;
	
	private int tulingUuid = 0;
	private String tulingKey = null;
	private String tulingUnknownAnswer = null;
	
	private String systemName = null;
	
	@PostConstruct
	public void init(){
	}
	
	
	@Override
	public String getAnswer(String content, long ownerId){  
		systemName = configService.getValue(DataName.systemName.toString(),ownerId);

		tulingUnknownAnswer  = configService.getValue("tulingUnknownAnswer",ownerId);
		if(StringUtils.isBlank(tulingUnknownAnswer)){
			tulingUnknownAnswer = "您好，欢迎访问" + systemName;
		}
		
		tulingUuid = configService.getIntValue("tulingUuid",ownerId);
		if(tulingUuid == 0){
			logger.warn("系统未定义图灵机器人API ID");
			return tulingUnknownAnswer;
		}

		tulingKey  = configService.getValue("tulingKey",ownerId);
		if(StringUtils.isBlank(tulingKey)){
			logger.warn("系统未定义图灵机器人API KEY");
			return tulingUnknownAnswer;
		}
		
		if(StringUtils.isBlank(content) || content.startsWith("http://")){
			return tulingUnknownAnswer;
		}
		String apiUrl = "http://www.tuling123.com/openapi/api?key=" + tulingKey + "&info=";  
		String param = "";  
		try {  
			param = apiUrl+URLEncoder.encode(content,"utf-8");  
		} catch (UnsupportedEncodingException e1) {  
			e1.printStackTrace();  
		} //将参数转为url编码  

		/** 发送httpget请求 */  
		String result = "";  
		try {  
			result=HttpUtils.sendData(param);
		} catch (Exception e) {  
			e.printStackTrace();  
		}  		

		try {  
			JsonNode jsonNode = JsonUtils.getInstance().readTree(result);
			int rs = jsonNode.path("code").asInt();
			if(rs == 100000 ){  
				result = jsonNode.path("text").asText();
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		if(null==result){  
			return tulingUnknownAnswer;
		}  
		return result;  
	}  
}
