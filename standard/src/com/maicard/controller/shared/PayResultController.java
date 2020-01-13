package com.maicard.controller.shared;

import java.io.BufferedReader;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.maicard.annotation.IgnoreLoginCheck;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.HttpUtils;
import com.maicard.mb.service.MessageService;
import com.maicard.money.domain.Pay;
import com.maicard.money.service.PayService;
import com.maicard.product.domain.Item;
import com.maicard.product.service.ItemService;
import com.maicard.product.service.NotifyService;
import com.maicard.standard.TransactionStandard.TransactionStatus;

@Controller
public class PayResultController  extends BaseController{

	@Resource
	protected ConfigService configService;
	@Resource
	protected MessageService messageService;
	@Resource
	protected ItemService itemService;
	@Resource
	protected NotifyService notifyService;
	@Resource
	protected PayService payService;

	protected String resultView = "payResult/detail";




	/*
	 * 充值完成后的回调结果处理
	 * 调用对应的payProcessor的onResult接口方法，并得到EisMessage结果
	 * 处理完成后显示相应的用户界面
	 */
	@RequestMapping("/payResult/{payMethodId}")
	public String onResult(HttpServletRequest request, HttpServletResponse response, ModelMap map,
			@PathVariable("payMethodId") Integer payMethodId) throws Exception {
		String resultString = HttpUtils.generateRequestString(request);
		logger.debug("充值完成后第三方传来的数据"+resultString);
//		String resultString = request.getQueryString();
		BufferedReader input = request.getReader();
		String rawContent = null;
		if(input != null){
            StringBuilder sb = new StringBuilder(); 
            String line = null;
			while( (line = input.readLine()) != null){
				sb.append(line);
			}
			rawContent = sb.toString();
			logger.debug("对方提交了数据流:" + rawContent);
			if(StringUtils.isNotBlank(rawContent)){
				resultString += "&rawContent=" + rawContent;
			}
		}
		EisMessage resultMessage = payService.end(payMethodId, resultString, request);
		Pay pay = null;
		if(resultMessage != null){
			try{
				pay = (Pay) resultMessage.getAttachment().get("pay");

			}catch(Exception e){
				logger.error("无法解析支付处理器返回的pay对象:" + e.getMessage());
				e.printStackTrace();
			}
		}
		if(pay == null){
			logger.error("支付处理器未返回pay对象");
		} else {
			if(pay.getRefBuyTransactionId() != null){
				Item item = itemService.select(pay.getRefBuyTransactionId());
				map.put("item", item);
				map.put("addressBook", null);

			}
		}
		logger.debug("onResult执行完毕");
		return resultView;

	}

	/*
	 * 充值完成后的回调通知处理
	 * 调用对应的payProcessor的onResult接口方法，并得到EisMessage结果
	 * 处理完成后仅向支付商返回success等字符
	 */
	@RequestMapping("/payNotify/{payMethodId}")
	@IgnoreLoginCheck
	public ResponseEntity<String>   onNotify(HttpServletRequest request, HttpServletResponse response, ModelMap map, 
			@PathVariable("payMethodId") Integer payMethodId) throws Exception {
		logger.debug("进入onNotify方法");
		String resultString = HttpUtils.generateRequestString(request);
		BufferedReader input = request.getReader();
		String rawContent = null;
		if(input != null){
            StringBuilder sb = new StringBuilder(); 
            String line = null;
			while( (line = input.readLine()) != null){
				sb.append(line);
			}
			rawContent = sb.toString();
			logger.debug("对方提交了数据流:" + rawContent);
			if(StringUtils.isNotBlank(rawContent)){
				resultString += "&rawContent=" + rawContent;
			}
		} else {
			logger.debug("对方没提交任何POST数据");
		}

		EisMessage notifyResult = payService.end(payMethodId, resultString, request);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "text/html; charset=UTF-8");
		Pay pay = null;
		logger.debug("notifyResult为"+notifyResult==null?"空":notifyResult+"");
		if(notifyResult != null){
			try{
				pay = (Pay) notifyResult.getAttachment().get("pay");
				logger.debug("Pay对象："+pay);
			}catch(Exception e){
				logger.error("无法解析支付处理器返回的pay对象:" + e.getMessage());
				e.printStackTrace();
			}
		}
		if(pay == null){
			logger.error("支付处理器未返回pay对象");
			return new ResponseEntity<String>(null, responseHeaders, HttpStatus.OK);	
		}
		Pay pay2 = payService.select(pay.getTransactionId());
		if(pay2 != null){
			if(pay2.getCurrentStatus() == TransactionStatus.success.id){
				String inNotifyUrl = pay2.getInNotifyUrl();
				if(StringUtils.isNotBlank(inNotifyUrl)){
					notifyService.sendNotify(pay2);
				}
			}
		}
		logger.info("支付订单[" + pay.getTransactionId() + "]响应字符串:" + pay.getPayResultMessage() );
		return new ResponseEntity<String>(pay.getPayResultMessage(), responseHeaders, HttpStatus.OK);	
	}

	@RequestMapping("/manualPaySuccess")
	public ResponseEntity<String>    test(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {

		Pay pay = payService.select("101112018113013102080508191");
		pay.setCurrentStatus(TransactionStatus.success.getId());
		payService.end(pay);
		/*
		EisMessage m = new EisMessage();
		m.setOperateCode(Operate.close.getId());
		m.setAttachment(new HashMap<String,Object>());
		m.getAttachment().put("pay", pay);	
		m.setObjectType(ObjectType.pay.toString());
		messageService.send(null, m);
		m = null;*/
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("11011201304101452525931", responseHeaders, HttpStatus.OK);	
	}


}
