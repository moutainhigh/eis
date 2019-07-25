package com.maicard.serializer;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.maicard.common.domain.EisObject;
import com.maicard.common.domain.ExtraData;



/**
 *	对对象中的data属性进行检查<br/>
 *	如果是Map，则把Map中的每个对象取出来作为对象的一个属性输出<br/>
 *	而不是作为data属性的一个子属性<br/>
 *	如果这些对象是ExtraData，那么只输出ExtraData的dataValue<br/>
 *	并且不再输出data这个Map
 *
 * @author NetSnake
 * @date 2015年12月11日
 * 
 */
public class ExtraDataSerializer_old extends JsonSerializer<Object>{
	protected static final Logger logger = LoggerFactory.getLogger(ExtraDataSerializer_old.class);
	private static Map<String, List<String>> cachedIgnoreProperties = new HashMap<String, List<String>>();

	@Override
	public void serialize(Object object, JsonGenerator jsonGenrator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		_serialize(object, jsonGenrator, provider);
	}

	@SuppressWarnings("unchecked")
	private void _serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException{
		List<String> ignorePropertes = null;
		if(provider.getActiveView() != null){
			ignorePropertes = getIgnoreProperties(object, provider.getActiveView());
		}
		jsonGenerator.writeStartObject();
		BeanInfo bif = null;
		try{
			bif = Introspector.getBeanInfo(object.getClass());
		}catch(Exception e){
			logger.error("无法获取对象[" + object.getClass() + "]的beanInfo");
			return;
		}
		PropertyDescriptor pds[] = bif.getPropertyDescriptors();
		for(PropertyDescriptor pd:pds){

			Method method = pd.getReadMethod();
			Object value = null;
			try {
				value = method.invoke(object, new Object[]{});
			} catch (Exception e) {
				logger.error("无法反射执行对象[" + object.getClass().getName() + "]的读取方法:" + method.getName());
			} 
			if(value == null){
				continue;
			}

			if(ignorePropertes != null && ignorePropertes.contains(pd.getName())){
				//logger.debug("属性[" + pd.getName() + "]被指定为忽略");
				continue;
			}
			if(value instanceof Map){
				//logger.debug("处理MAP元素:" + pd.getName() + "=>" + value.getClass().getName());
				boolean isExtraData = false;
				Map<Object,Object> map = (Map<Object,Object>)value;
				if(map == null || map.size() < 1){
					continue;
				}
				for(Object key : map.keySet()){
					if(map.get(key) == null){
						continue;
					}
					if(map.get(key) instanceof ExtraData){
						isExtraData = true;
						//只输出ExtraData中的value，并且是dataCode作为键
						ExtraData extraData = (ExtraData)map.get(key);
						if(extraData.getDataValue() != null){
							//只输出非空值
							jsonGenerator.writeStringField(extraData.getDataCode(), extraData.getDataValue());
						}
						//
					}
				}
				if(!isExtraData){
					//	_serialize(value, jsonGenerator, provider);
					jsonGenerator.writeObjectField(pd.getName(),value);

					//jsonGenerator.writeObjectFieldStart(pd.getName());
					//jsonGenrator.writeObjectFieldStart(pd.getName());
					//for(Object key : map.keySet()){
					//	jsonGenerator.writeObjectField(key.toString(), map.get(key));
					//	_serialize(map.get(key), jsonGenerator, provider);
					//	jsonGenrator.writeObjectField(key.toString(), map.get(key));
					//}
					//jsonGenerator.writeEndObject();
					/*jsonGenrator.writeArrayFieldStart(pd.getName());
						for(Object o : (List<?>)value){
							_serialize(o, jsonGenrator, provider);
						}
						jsonGenrator.writeEndArray();*/
				}
			} else {
				if(value instanceof List ){
					logger.debug("处理列表元素:" + pd.getName() + "=>" + value.getClass().getName());
					List<?>list = (List<?>)value;
					if(list == null || list.size() < 1){
						continue;
					}
					if(((List<?>)value).get(0) instanceof EisObject){
						if(logger.isDebugEnabled()){
							logger.debug("递归处理EisObject及其派生元素的列表数据:" + pd.getName());
						}
						jsonGenerator.writeArrayFieldStart(pd.getName());
						for(Object o : (List<?>)value){
							_serialize(o, jsonGenerator, provider);
						}
						jsonGenerator.writeEndArray();
					}
				} else {
					jsonGenerator.writeObjectField(pd.getName(),value);
				}
			}
		}
		jsonGenerator.writeEndObject(); 
	}

	/**
	 * 获得指定类及其所有祖先定义的属性，并检查是否定义了JsonView，如果定义了是否与对应的viewName一致，如果不一致则不进行序列化
	 * @param object
	 * @param viewName
	 * @return
	 */
	private List<String> getIgnoreProperties(Object object, Class<?> viewClass) {
		if(cachedIgnoreProperties != null && cachedIgnoreProperties.size() > 0){
			if(cachedIgnoreProperties.get(object.getClass().getName()) != null){
				if(logger.isDebugEnabled()){
					//logger.debug("从缓存中返回对象[" + object.getClass().getName() + "]的忽略属性列表");
				}
				return cachedIgnoreProperties.get(object.getClass().getName());
			}
		}
		if(logger.isDebugEnabled()){
			//logger.debug("分析对象[" + object.getClass().getName() + "]针对VIEW:" + viewClass.getName() + "]的属性过滤");
		}
		List<String> ignorePropertes = new ArrayList<String>();
		ignorePropertes.add("class");
		if(viewClass == null){
			if(logger.isDebugEnabled()){
				//logger.debug("当前序列化未指定viewClass");
			}
		} else {
			@SuppressWarnings("rawtypes")
			Class targetClass = object.getClass();
			for(; targetClass != Object.class ; targetClass = targetClass.getSuperclass()) {
				for(Field f : targetClass.getDeclaredFields()){
					if(logger.isDebugEnabled()){
						//logger.debug("检查类[" + targetClass.getName() + "]属性:" + f.getName());
					}
					Annotation annotation = f.getAnnotation(JsonView.class);
					if(annotation != null){
						JsonView view = (JsonView)annotation;
						Class<?>[] data = view.value();
						for(Class<?> clazz : data){
							if(logger.isDebugEnabled()){
								//logger.debug("属性[" + f.getName() + "]拥有的JsonView注解是[" + clazz.getName() + "]");
							}
							boolean addIgnore = true;
							//XXX 循环VIew的class及其父类，如果有与定义的JsonView父类同名的就认为不需要忽略
							for(; viewClass != Object.class ; viewClass = viewClass.getSuperclass()) {
								if(viewClass.getName().equals(clazz.getName())){
									if(logger.isDebugEnabled()){
										//logger.debug("属性[" + f.getName() + "]拥有的JsonView注解[" + clazz.getName() + "]与当前序列化指定的JsonView[" + viewClass.getName() + "]一致，应当输出");
									}
									addIgnore = false;
									break;
								}
							}
							if(addIgnore){
								ignorePropertes.add(f.getName());
							}
						}
					} else {
						//logger.debug("属性:" + f.getName() + "]没有@JsonView");
					}

				}
			}
		}
		
		cachedIgnoreProperties.put(object.getClass().getName(), ignorePropertes);
		return ignorePropertes;
	}

}
