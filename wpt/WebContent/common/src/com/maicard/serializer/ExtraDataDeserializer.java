package com.maicard.serializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.maicard.common.domain.CacheValue;
import com.maicard.standard.CommonStandard;



/**
 * 只输出ExtraData中的键和值
 *
 * @author NetSnake
 * @date 2015年12月11日
 * 
 */
public class ExtraDataDeserializer extends JsonDeserializer<CacheValue>{
	
	final static SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	@Override
	public CacheValue deserialize(JsonParser jp, DeserializationContext paramDeserializationContext)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		
		String objectType = node.path("objectType").asText();
		if(objectType == null){
			throw new IOException("反序列化CacheValue没有指定类型objectType");
		}
		
		CacheValue cv = new CacheValue();
		cv.key = node.path("key").asText();
		cv.objectType = objectType;
		try {
			cv.value = jp.getCodec().readValue(jp, Class.forName(objectType));
			cv.expireTime = sdf.parse(node.path("expireTime").asText());
		} catch (ClassNotFoundException | ParseException e) {
			throw new IOException("无法将数据反序列化为对象:" + objectType);
		}
     
		return cv;
	}

	


}
