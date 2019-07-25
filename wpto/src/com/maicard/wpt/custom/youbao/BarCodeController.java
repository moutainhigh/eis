package com.maicard.wpt.custom.youbao;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.security.service.CertifyService;


/**
 * 根据数字生成条形码
 *
 *
 * @author NetSnake
 * @date 2015年11月16日
 *
 */
@Controller
@RequestMapping("/barcode")
public class BarCodeController extends BaseController{


	@Resource
	private CertifyService certifyService;



	@ResponseBody
	@IgnoreLoginCheck
	@RequestMapping(value="/{code}",method=RequestMethod.GET )
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("code")String code) {
		/*User frontUser = null;
		try{
			frontUser = certifyService.getLoginedUser(request, response, UserTypes.frontUser.getId());
			//logger.info("从Session中得到用户:" + frontUser.getUuid());
		}catch(Exception e){
			e.printStackTrace();
		}

		if(frontUser == null){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}*/
		if(!StringUtils.isNumeric(code)){
			logger.warn("错误的条形码:" + code);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
		if(code.length() < 12){
			logger.warn("错误的条形码长度:" + code);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
		code = code.substring(0,12);
		logger.debug("尝试生成条形码:" + code);
		//Create the barcode bean
		EAN13Bean bean = new EAN13Bean();
		final int dpi = 300;
		try {
			ServletOutputStream out = response.getOutputStream();
			//Set up the canvas provider for monochrome JPEG output 
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(
					out, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			//Generate the barcode
			bean.generateBarcode(canvas, code);

			canvas.finish();
			out.flush();
			out.close();
			response.setStatus(HttpStatus.OK.value());
			return null;
		}catch(Exception e){
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return null;
	}





}
