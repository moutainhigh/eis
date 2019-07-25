package com.maicard.wpt.custom.youbao;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;
import com.maicard.captcha.service.CaptchaService;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;

import org.apache.commons.lang.exception.ExceptionUtils;
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
	private ConfigService configService;

	HashMap<Long, CaptchaCriteria> captchaCriteriaConfigCache = new HashMap<Long, CaptchaCriteria>();

	private final Color DEFAULT_FORE_COLOR = new Color(0,0,0);
	private final int DEFAULT_MAX_LENGTH = 4;
	private final int DEFAULT_MIN_LENGTH = 4;


	@RequestMapping(method=RequestMethod.GET)
	@IgnoreLoginCheck
	public void list(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws UnsupportedEncodingException {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return;		
		}
		CaptchaCriteria captchaCriteria = getCaptchaConfig(ownerId);
		Captcha captcha = captchaService.get(captchaCriteria);
		if(captcha == null){
			logger.error("无法生成验证码");
			return;
		}		
		try {
			captchaService.setToCookie(request, response, null,captcha.getWord());
			response.getOutputStream().write(captcha.getImage());
			//ImageIO.write(captcha.getImage(), "png", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@RequestMapping(value="/pgw",method=RequestMethod.GET)
	@IgnoreLoginCheck
	public void getForPgw(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws UnsupportedEncodingException {
		long ownerId = 0;
		try{
			ownerId = (long)map.get("ownerId");
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"找不到对应的地址","请尝试访问其他页面或返回首页"));
			return;		
		}
		CaptchaCriteria captchaCriteria = getCaptchaConfig(ownerId);
		Captcha captcha = captchaService.get(captchaCriteria);
		if(captcha == null){
			logger.error("无法生成验证码");
			return;
		}		
		
		String cookieName = null;
		try {
			captchaService.setToCookie(request, response, cookieName, captcha.getWord());
			response.getOutputStream().write(captcha.getImage());
			//ImageIO.write(captcha.getImage(), "png", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	private CaptchaCriteria getCaptchaConfig(long ownerId){

		CaptchaCriteria captchaCriteria = captchaCriteriaConfigCache.get(ownerId);
		if(captchaCriteria != null){
			logger.debug("从缓存中返回ownerId=" + ownerId + "的验证码配置[foreColor=" + captchaCriteria.getForeColor() + ",maxLength=" + captchaCriteria.getMaxLength() + ",minLength=" + captchaCriteria.getMinLength() + "]");
			return captchaCriteria;
		}

		int foreColor = 0;
		captchaCriteria = new CaptchaCriteria();
		String foreColrConfig = configService.getValue(DataName.frontCaptchaForeColor.toString(), ownerId);
		if(foreColrConfig != null){		
			logger.debug("[ownerId=" + ownerId + "]系统配置的验证码颜色定义是:" + foreColrConfig);

			if(NumericUtils.isNumeric(foreColrConfig)){
				foreColor = NumericUtils.getNumeric(foreColrConfig);
			} else {
				String[] data = foreColrConfig.split(",");
				if(data != null && data.length == 3){
					int r = NumericUtils.getNumeric(data[0]);
					int g = NumericUtils.getNumeric(data[1]);
					int b = NumericUtils.getNumeric(data[2]);
					foreColor = new Color(r,g,b).getRGB();
				}
			}
		} else {
			logger.debug("[ownerId=" + ownerId + "]系统没有配置验证码颜色定义，使用默认前景色");
			foreColor = DEFAULT_FORE_COLOR.getRGB();
		}

		int maxLength = configService.getIntValue(DataName.captchaMaxLength.toString(), ownerId);
		if(maxLength <= 0){
			maxLength = DEFAULT_MAX_LENGTH;
		}

		int minLength = configService.getIntValue(DataName.captchaMinLength.toString(), ownerId);
		if(minLength <= 0){
			minLength = DEFAULT_MIN_LENGTH;
		}

		boolean usingFilter = configService.getBooleanValue(DataName.captchaUsingFilter.toString(), ownerId);

		captchaCriteria.setForeColor(foreColor);
		captchaCriteria.setMinLength(minLength);
		captchaCriteria.setMaxLength(maxLength);
		if(usingFilter){
			captchaCriteria.setMode("1");
		} else {
			captchaCriteria.setMode("0");
		}
		captchaCriteria.setForeColor(foreColor);

		logger.debug("ownerId= " + ownerId + "的验证码配置是[foreColor=" + foreColor + ",maxLength=" + maxLength + ",minLength=" + minLength + "]，将其放入缓存");
		captchaCriteriaConfigCache.put(ownerId, captchaCriteria);
		return captchaCriteria;
	}
}
