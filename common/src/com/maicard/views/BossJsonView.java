package com.maicard.views;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicard.standard.CommonStandard;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**

 * @author NetSnake
 * @date 2013-2-27
 */
public class BossJsonView extends MappingJackson2JsonView {

	private final ObjectMapper objectMapper = new ObjectMapper(); 
	private final SimpleDateFormat df = new SimpleDateFormat(CommonStandard.defaultDateFormat);
	protected final Logger logger = LoggerFactory.getLogger(getClass());


	@Override
	protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control",	"no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		Object value = 		super.filterModel(map);
		
		//忽略为空的数据
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setTimeZone(TimeZone.getDefault());

		//logger.debug("map数量2:" + map.size());
		boolean isTree = false;
		boolean jsonp = false;
		if(request.getRequestURI().endsWith(".tree")){
			isTree =true;
		}
		if(request.getRequestURI().endsWith(".jsonp")){
			jsonp =true;
		}

		objectMapper.setDateFormat(df);
		

		//标记view为Boss.class的属性才显示输出
		String responseString = objectMapper.writerWithView(JsonFilterView.Boss.class).writeValueAsString(value);
		if(isTree){
			//查找map中的tree对象
			if(map.get("tree") != null){
				//已经是JSON格式的字符串，不需要进行转换，直接输出
				if(map.get("tree").toString().startsWith("[") && map.get("tree").toString().endsWith("]")){
					response.getWriter().write(map.get("tree").toString());
					return;						
				}
				if(map.get("tree").toString().startsWith("{") && map.get("tree").toString().endsWith("}")){
					response.getWriter().write(map.get("tree").toString());
					return;						
				}
				
			} else {
				logger.debug("请求返回tree数据，但map中没有tree对象.");
			}
		} else if(jsonp){
			String jsonpString = "jsonpback(";
			jsonpString += responseString;
			jsonpString += ")";
			responseString = jsonpString;
		} 
		response.getWriter().write(responseString);


	}


}
