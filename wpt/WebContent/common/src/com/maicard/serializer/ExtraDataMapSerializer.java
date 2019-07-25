package com.maicard.serializer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.maicard.common.domain.ExtraData;

/**
 * 把ExtraData或其子类的Map输出为更简洁的格式
 * 
 * 
 * @author GHOST
 * @date 2018-10-22
 *
 */
public class ExtraDataMapSerializer extends JsonSerializer<Map<String,ExtraData>>{



	@Override
	public void serialize(Map<String, ExtraData> map, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeStartObject();
		if(map != null && map.size() > 0) {
			for(ExtraData dd : map.values()) {
				if(StringUtils.isBlank(dd.getDataValue())) {
					continue;
				}
				jsonGenerator.writeStringField(dd.getDataCode(), dd.getDataValue());
			}
		}
		jsonGenerator.writeEndObject();

	}
}
