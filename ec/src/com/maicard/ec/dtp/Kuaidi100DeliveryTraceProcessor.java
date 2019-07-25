package com.maicard.ec.dtp;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.common.service.ConfigService;
import com.maicard.ec.domain.DeliveryOrder;
import com.maicard.ec.iface.DeliveryTraceProcessor;
import com.maicard.http.HttpClientPoolV3;
import com.maicard.http.HttpUtilsV3;
import com.maicard.security.domain.User;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.DataName;

@Service
public class Kuaidi100DeliveryTraceProcessor extends BaseService implements DeliveryTraceProcessor {

	@Resource
	private ConfigService configService;
	@Resource
	private PartnerService partnerService;

	private final String apiHost = "api.kuaidi100.com";
	private final String htmlHost = "www.kuaidi100.com";
	private final String mHtmlHost = "m.kuaidi100.com";

	private final int port = 80;
	//private final String key = "7f69ce4e7967c4a7";
	private final String apiPrefix = "http://" + apiHost + ":" + port + "/api";
	private final String htmlPrefix = "http://" + htmlHost + ":" + port + "/applyurl";
	private final String mHtmlPrefix = "http://" + mHtmlHost + ":" + port + "/index_all.html";
	private final String defaultMode = "mhtml";
	//	private final String defaultMode = "api";

	private final int PAGE_TYPE_JSON = 0;
	@SuppressWarnings("unused")
	private final int PAGE_TYPE_TXT = 3;

	private static Map<String,String> keyCacheMap = new HashMap<String,String>();



	@Override
	public HashMap<String, String> trace(DeliveryOrder deliveryOrder) {
		if(deliveryOrder.getDeliveryCompany() == null){
			if(deliveryOrder.getDeliveryCompanyId() > 0){
				logger.debug("查询当前配送单[" + deliveryOrder + "]的配送公司:" + deliveryOrder.getDeliveryCompanyId());
				User partner = partnerService.select(deliveryOrder.getDeliveryCompanyId());
				if(partner == null){
					logger.error("系统中找不到配送单[" + deliveryOrder + "]的配送公司信息:" + deliveryOrder.getDeliveryCompanyId());
					return null;
				}
				deliveryOrder.setDeliveryCompany(partner.getUsername());

			}
		}
		String displayMode = null;
		if(StringUtils.isBlank(deliveryOrder.getDisplayType())){
			displayMode = defaultMode;
		} else {
			displayMode = deliveryOrder.getDisplayType();
		}
		if(displayMode.equals("api")){
			return _apiQuery(deliveryOrder);
		} else		if(displayMode.equals("mhtml")){
			return _mHtmlQuery(deliveryOrder);
		}
		return _htmlQuery(deliveryOrder);
	}





	private HashMap<String, String> _apiQuery(DeliveryOrder deliveryOrder) {

		String key = getKey(deliveryOrder.getOwnerId());




		String url = apiPrefix + "?id" +  "=" + key + "&com=" + deliveryOrder.getDeliveryCompany() + "&nu=" + deliveryOrder.getOutOrderId() + "&multi=" + PAGE_TYPE_JSON;
		logger.debug("尝试查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询地址:" + url);
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(htmlHost, port);
		String page = null;
		try {
			page = HttpUtilsV3.getData(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询结果:" + page);

		HashMap<String, String> traceData = new HashMap<String, String>();
		if(page == null){
			logger.error("无法查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询结果为空");
			return null;
		}
		if(page.indexOf("身份key认证失败") >= 0){
			return null;
		}
		traceData.put("trace", page);
		deliveryOrder.setTraceData(traceData);
		return traceData;
	}

	private String getKey(long ownerId) {

		String key = keyCacheMap.get(String.valueOf(ownerId));
		if(key != null){
			logger.debug("从本地Map中直接返回ownerId=" + ownerId + "的key:" + key);
			return key;
		}

		key = configService.getValue(DataName.kuaidi100Key.toString(), ownerId);
		if(key == null){
			logger.warn("无法从系统中读取快递100配置KEY:" + DataName.kuaidi100Key.toString());
			return null;
		}
		logger.debug("从系统中读取ownerId=" + ownerId + "的key:" + key + ",放入本地Map并返回该值");
		keyCacheMap.put(String.valueOf(ownerId), key);
		return key;
	}





	private HashMap<String, String> _htmlQuery(DeliveryOrder deliveryOrder) {
		String key = getKey(deliveryOrder.getOwnerId());

		String url = htmlPrefix + "?key" +  "=" + key + "&com=" + deliveryOrder.getDeliveryCompany() + "&nu=" + deliveryOrder.getOutOrderId();
		logger.debug("尝试查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询地址:" + url);
		HttpClient httpClient = HttpClientPoolV3.getHttpClient(apiHost, port);
		String page = null;
		try {
			page = HttpUtilsV3.getData(httpClient, url, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询结果:" + page);

		HashMap<String, String> traceData = new HashMap<String, String>();
		if(page == null){
			logger.error("无法查询配送公司[" + deliveryOrder.getDeliveryCompany() + "]的运单:" + deliveryOrder.getOutOrderId() + ",查询结果为空");
			return null;
		}
		if(page.indexOf("身份key认证失败") >= 0){
			return null;
		}
		traceData.put("traceUrl", page.trim());
		deliveryOrder.setTraceData(traceData);
		return traceData;
	}

	private HashMap<String, String> _mHtmlQuery(DeliveryOrder deliveryOrder) {
		String url = mHtmlPrefix + "?type=" + deliveryOrder.getDeliveryCompany() + "&postid=" + deliveryOrder.getOutOrderId();
		String returnUrl = deliveryOrder.getExtraValue("returnUrl");
		if(StringUtils.isNotBlank(returnUrl)){
			url += "&callbackurl=" + returnUrl;
		}
		logger.debug("组合快递单查询地址:" + url);

		HashMap<String, String> traceData = new HashMap<String, String>();


		traceData.put("traceUrl", url);
		deliveryOrder.setTraceData(traceData);
		return traceData;
	}

}
