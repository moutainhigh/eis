package com.maicard.wpt.controller.shared;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.annotation.IgnorePrivilegeCheck;
import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseController;
import com.maicard.common.service.CenterDataService;
import com.maicard.common.util.NumericUtils;
import com.maicard.standard.DataName;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * 生成验证码图片
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends BaseController{
	
	@Resource
	private CaptchaService captchaService;
	
	@Resource
	private CenterDataService centerDataService;
	
	
	int foreColr = 0;
	
	
	@PostConstruct
	public void init(){
		String foreColrConfig = configService.getValue(DataName.partnerCaptchaForeColor.toString(), 0);
		if(foreColrConfig != null){		
			logger.debug("系统配置的验证码颜色定义是:" + foreColrConfig);

			if(NumericUtils.isNumeric(foreColrConfig)){
				foreColr = NumericUtils.getNumeric(foreColrConfig);
			} else {
				String[] data = foreColrConfig.split(",");
				if(data != null && data.length == 3){
					int r = NumericUtils.getNumeric(data[0]);
					int g = NumericUtils.getNumeric(data[0]);
					int b = NumericUtils.getNumeric(data[0]);
					foreColr = new Color(r,g,b).getRGB();
				}
			}
		} else {
			logger.debug("系统没有配置验证码颜色定义");
		}
	}

	@RequestMapping(method=RequestMethod.GET)
	@IgnorePrivilegeCheck
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws UnsupportedEncodingException {
		CaptchaCriteria captchaCriteria = new CaptchaCriteria();
		captchaCriteria.setForeColor(foreColr);
		Captcha captcha = captchaService.get(captchaCriteria);
		if(captcha == null){
			logger.error("无法生成验证码");
			return;
		}		
		try {
			captchaService.setToCookie(request, response, null, captcha.getWord());
			response.getOutputStream().write(captcha.getImage());
			//ImageIO.write(captcha.getImage(), "png", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@IgnorePrivilegeCheck
	@RequestMapping("/deleteRedis")
	public void deleteRedis(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws UnsupportedEncodingException {
		Set<String> keys = centerDataService.listKeys("GlobalUnique*");
		if(keys == null || keys.size() < 1){
			logger.warn("中央缓存中没有以GlobalUnique开头的缓存");
			return;
		}
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			logger.debug("删除键:" + key);
			centerDataService.delete(key);
		}
	}
}
