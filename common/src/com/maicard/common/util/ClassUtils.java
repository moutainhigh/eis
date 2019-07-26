package com.maicard.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.maicard.annotation.InputLevel;
import com.maicard.annotation.QueryCondition;
import com.maicard.common.base.Criteria;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.EisObject;
import com.maicard.common.pe.IntArrayPropertyEditor;
import com.maicard.common.pe.StringDatePropertyEditor;
import com.maicard.method.ExtraValueAccess;
import com.maicard.standard.BasicStatus;
import com.maicard.standard.CommonStandard;
import com.maicard.views.JsonFilterView.Partner;


public class ClassUtils {

	protected static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	static {
		PropertyEditorManager.registerEditor(int[].class, IntArrayPropertyEditor.class);
		PropertyEditorManager.registerEditor(Date.class, StringDatePropertyEditor.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static void findAllClass(Object object, HashMap<String,Class> classMap){
		if(object == null){
			return;
		}
		//List<Class<Object>> allClass = new ArrayList<Class<Object>>();
		if(object.getClass().isPrimitive()){
			return;
		}
		if(object instanceof java.util.List){
			logger.debug("递归查找List[" + object.toString() + "]");
			for(Object subObj : (List<Object>)object){
				findAllClass(subObj, classMap);

			}

		} else if(object instanceof java.util.Collection){
			logger.debug("递归查找Collection[" + object.toString() + "]");
			Iterator it = ((Collection)object).iterator();
			while(it.hasNext()){
				findAllClass(it.next(), classMap);
			}
		} else {
			//查询对象中的属性
			try{
				BeanInfo bif = Introspector.getBeanInfo(object.getClass());
				PropertyDescriptor pds[] = bif.getPropertyDescriptors();
				if(pds != null){
					for(PropertyDescriptor pd:pds){
						if(pd.getPropertyType().isPrimitive()){
							continue;
						}

						if(pd.getPropertyType().getName().indexOf("List") >= 0){
							logger.debug("递归查找属性List[" + object.toString() + "]");
							Method method = pd.getReadMethod();
							List<Object> listObject = null;
							try{
								listObject = (List<Object>)method.invoke(object, new Object[0]);
							}catch(Exception e){}
							if(listObject != null && listObject.size() > 0){
								for(Object subObj : listObject){
									findAllClass(subObj, classMap);
								}
							}
						}
						if(pd.getPropertyType().getClass().getName().indexOf("Map") >= 0){
							logger.error("递归查找属性Map[" + object.toString() + "]");
							Method method = pd.getReadMethod();
							Map<String, Object> mapObject = null;
							try{
								mapObject = (Map<String,Object>)method.invoke(object, new Object[0]);
							}catch(Exception e){}
							if(mapObject != null && mapObject.size() > 0){
								for(Object subObj : mapObject.values()){
									findAllClass(subObj, classMap);
								}
							}
						}



						logger.debug("放入对象的属性:" + pd.getPropertyType().getName() );
						classMap.put(pd.getPropertyType().getName(), pd.getPropertyType());					
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			logger.debug("放入对象:" + object.getClass().getName() );
			classMap.put(object.getClass().getName(), object.getClass());
		}

		return;

	}
	public static void  bindBeanFromMap(Object object, Map<String,String> requestDataMap ){
		bindBeanFromMap(object, requestDataMap, null);		
	}

	public static void  bindBeanFromMap(Object object, Map<String,String> requestDataMap, String prefix){

		BeanInfo bif = null;
		try {
			bif = Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		if(bif == null){
			logger.error("无法获取[" + object.getClass().getName() + "]的类信息");
			return;
		}
		PropertyDescriptor pds[] = bif.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			for (String attributeName : requestDataMap.keySet()) {
				if(prefix != null) {
					attributeName = attributeName.replaceFirst("^" + prefix + "\\.", "");
				}
				if (pd.getName().equals(attributeName)) {
					Method method = pd.getWriteMethod();
					try {
						/*String parameterDesc = null;
						Class<?>[] paraTypes = method.getParameterTypes();
						if(paraTypes != null && paraTypes.length > 0){
							for(Class<?> para : paraTypes){
								parameterDesc += para.getName() + ",";
							}
						}
						logger.debug("尝试对对象[" + object.getClass().getName() + "]反射执行[" + method.getName() + ",参数:" + parameterDesc + "],提交参数:" + map.get(attributeName));
						 */
						PropertyEditor pe = PropertyEditorManager.findEditor(pd.getPropertyType());
						if(pe == null){
							logger.error("找不到对象[" + object.getClass().getName() + "]的反射方法:" + method.getName());
							continue;
						} 
						pe.setAsText(requestDataMap.get(attributeName));
						method.invoke(object, pe.getValue());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static Map<String, Map<String, String>> getQueryCondition(Criteria criteria, String name) {
		Map<String, Map<String, String>>  queryConditions = new HashMap<String, Map<String, String>>();
		Field[] fields = criteria.getClass().getDeclaredFields();
		for(Field f : fields){
			if(f.isAnnotationPresent(QueryCondition.class)){
				QueryCondition q = f.getAnnotation(QueryCondition.class);
				Map<String,String> valueMap = new HashMap<String,String>();
				if(f.getType() == Date.class){
					valueMap.put("date", null);
				} else {
					String[] values = q.value();
					if(values[0].equals("")){
						valueMap = null;
					} else {
						for(String v : values){
							valueMap.put(v, "Criteria." + v);
						}
					}
				}
				queryConditions.put(f.getName(), valueMap);
			}
		}
		//手工设置currentStatus作为查询条件
		Map<String,String>currentStatusQueryMap = new  HashMap<String,String>();

		for(BasicStatus basicStatus : BasicStatus.values()){
			currentStatusQueryMap.put(String.valueOf(basicStatus.getId()), "Status." + basicStatus.getId());
		}
		queryConditions.put("currentStatus", currentStatusQueryMap);
		return queryConditions;		
	}


	//为指定的类的某个属性设置指定的值
	public static boolean setAttribute(Object targetObject, String attributeName, String attributeValue, String columnType) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		if(columnType != null && columnType.equalsIgnoreCase(CommonStandard.COLUMN_TYPE_EXTRA)){
			if(targetObject instanceof ExtraValueAccess){
				ExtraValueAccess ea = (ExtraValueAccess)targetObject;
				ea.setExtraValue(attributeName, attributeValue);
				return true;
			} else {
				try {
					Method setMethod = targetObject.getClass().getMethod("setExtraValue", String.class, String.class);
					if(setMethod != null){
						setMethod.invoke(targetObject, attributeName, attributeValue);
						return true;
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
			logger.error("设置属性为扩展，但目标对象不是一个ExtraValueAccess");
			return false;
		}


		BeanInfo bif = Introspector.getBeanInfo(targetObject.getClass());
		if(bif == null){
			logger.error("找不到对象的类信息:" + targetObject.getClass().getName());
			return false;
		}
		PropertyDescriptor pds[] = bif.getPropertyDescriptors();
		if(pds == null || pds.length < 1){
			logger.error("找不到对象的类信息的属性描述:" + targetObject.getClass().getName());
			return false;
		}
		for(PropertyDescriptor pd:pds){
			if(pd.getName().equals(attributeName)){
				Method writeMethod = pd.getWriteMethod();
				PropertyEditor pe = PropertyEditorManager.findEditor(pd.getPropertyType());
				if(pe != null ){
					pe.setAsText(attributeValue);
					writeMethod.invoke(targetObject, pe.getValue());
					if(logger.isDebugEnabled()){
						logger.debug("完成对象的原始属性写入[" + attributeName + "=>" + attributeValue + "]");
					}
					return true;
				} else {
					logger.error("找不到属性[" + attributeName + "]对应的写入编辑器");
					return false;
				}
			}
		}
		return false;
	}

	public static Map<String,Attribute> getAttributeForInputLevel(Object targetObject, Class<Partner> class1) {
		LinkedHashMap<String,Attribute> map = new LinkedHashMap<String,Attribute>();
		for(Field field : targetObject.getClass().getDeclaredFields()){
			logger.debug("XXXXXX" + field.getName() + "==============" + field.getType().getName());
			if(field.isAnnotationPresent(InputLevel.class)){
				InputLevel a = field.getAnnotation(InputLevel.class);
				if(a.value().getName().equals(class1.getName())){
					if(field.getType().getName().indexOf("Date") > 0){
						map.put(field.getName(), new Attribute(CommonStandard.COLUMN_TYPE_NATIVE, field.getName(), "date", false, 0 ,false, new String[]{}));	

					} else {
						map.put(field.getName(), new Attribute(CommonStandard.COLUMN_TYPE_NATIVE, field.getName(), null, false, 0 ,false, new String[]{}));	
					}
				} else {
					continue;
				}
			} else {
				continue;
				//map.put(filed.getName(), new Attribute(CommonStandard.COLUMN_TYPE_NATIVE, filed.getName(), null, false, 0 ,false, new String[]{}));	

			}
		}		
		return map;
	}

	/**
	 * 返回对象中指定的属性值
	 */
	public static String getValue(Object object, String attributeName, String columnType) {
		if(columnType == null || columnType.equalsIgnoreCase(CommonStandard.COLUMN_TYPE_EXTRA)){
			if(object instanceof ExtraValueAccess){
				ExtraValueAccess ea = (ExtraValueAccess)object;
				String extraValue = ea.getExtraValue(attributeName);
				if(extraValue != null){
					return extraValue;
				}
			}
			if(columnType != null && columnType.equalsIgnoreCase(CommonStandard.COLUMN_TYPE_EXTRA)){
				logger.error("读取属性为扩展属性，但目标对象不是一个ExtraValueAccess");
				return null;
			}
		}
		BeanInfo bif = null;
		try {
			bif = Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		if(bif == null){
			logger.error("无法获取[" + object.getClass().getName() + "]的类信息");
			return null;
		}
		PropertyDescriptor pds[] = bif.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			if (pd.getName().equals(attributeName)) {
				Method method = pd.getReadMethod();
				try {
					Object result = method.invoke(object, new Object[]{});
					if(result != null){
						return result.toString();
					} 
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}

		}
		return null;
	}

	/**
	 * 尝试获取一个eis对象的类型
	 * @param eisObject
	 * @return
	 */
	public static String getObjectType(EisObject eisObject) {
		if(eisObject.getObjectType() != null){
			return eisObject.getObjectType();
		}
		return StringUtils.uncapitalize(eisObject.getClass().getSimpleName());
	}

	/**
	 * 尝试获取一个eis对象的id
	 * 
	 * @param eisObject
	 * @return
	 */
	public static long getObjectId(EisObject eisObject) {
		if(eisObject.getId() > 0){
			return eisObject.getId();
		}
		String value = getValue(eisObject, getObjectType(eisObject) + "Id", CommonStandard.COLUMN_TYPE_NATIVE);
		if(NumericUtils.isNumeric(value)){
			return Long.parseLong(value);
		}
		return 0;
	}

	public static void copyProperties(Object fromObject, Object toObject, Set<String> copyProperties) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		if(fromObject.getClass() != toObject.getClass()){
			return;
		}
		BeanInfo bif = Introspector.getBeanInfo(fromObject.getClass());
		if(bif == null){
			logger.error("找不到对象的类信息:" + fromObject.getClass().getName());
			return ;
		}
		PropertyDescriptor pds[] = bif.getPropertyDescriptors();
		if(pds == null || pds.length < 1){
			logger.error("找不到对象的类信息的属性描述:" + fromObject.getClass().getName());
			return;
		}
		for(PropertyDescriptor pd:pds){
			for(String attributeName : copyProperties){
				if(pd.getName().equals(attributeName)){
					Method writeMethod = pd.getWriteMethod();
					Method readMethod = pd.getReadMethod();
					PropertyEditor pe = PropertyEditorManager.findEditor(pd.getPropertyType());
					if(pe != null ){
						Object attributeValue = readMethod.invoke(fromObject, new Object[]{});
						if(attributeValue == null){
							logger.error("无法读取来源对象的:" + attributeName);
							break;
						}
						pe.setAsText(attributeValue.toString());
						writeMethod.invoke(toObject, pe.getValue());
						if(logger.isDebugEnabled()){
							logger.debug("完成对象的属性写入[" + attributeName + "=>" + attributeValue + "]");
						}
					} else {
						logger.error("找不到属性[" + attributeName + "]对应的写入编辑器");
					}
					break;
				}
			}
		}
	}

	/**
	 * 根据指定条件在一个对象列表中
	 * 根据查询条件返回符合条件的对象
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> search(List<?> targetList, Object params) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		BeanInfo paramBi = Introspector.getBeanInfo(params.getClass());
		BeanInfo elementBi = Introspector.getBeanInfo(targetList.get(0).getClass());
		PropertyDescriptor paramPd[] = paramBi.getPropertyDescriptors();
		if(paramPd == null || paramPd.length < 1){
			logger.error("找不到查询参数对象的类信息的属性描述:" + params.getClass().getName());
			return Collections.emptyList();
		}
		PropertyDescriptor elementPd[] = elementBi.getPropertyDescriptors();
		if(elementPd == null || elementPd.length < 1){
			logger.error("找不到查询列表对象的类信息的属性描述:" + targetList.get(0).getClass().getName());
			return Collections.emptyList();
		}
		List<Object> copyList = new ArrayList<Object>();
		copyList.addAll(targetList);
		//System.out.println("待处理:" + JSON.toJSONString(copyList));
		for(PropertyDescriptor pd:paramPd){
			for(PropertyDescriptor epd : elementPd){
				if(pd.getName().equalsIgnoreCase("class")) {
					continue;
				}
				if(pd.getName().equals(epd.getName())){
					//logger.info("比较相同的属性:" + pd.getName());
					Method paramRm = pd.getReadMethod();
					Method elementRm = epd.getReadMethod();
					Object paramV = paramRm.invoke(params, new Object[] {});
					int i = 0;
					for(Object element : targetList) {
						Object elementV = elementRm.invoke(element, new Object[] {});
						if(elementV == null) {
							//没有该属性
							continue;
						}
						if(paramV == null) {
							//没有设置该查询条件
							continue;
						}
						logger.debug("检查第:" + i + "个元素:" + JSON.toJSONString(element) + ",查询参数类型是:" + paramV.getClass().getName());
						if(paramV.getClass().isArray()) {
							logger.debug("检查数组型查询参数:" + pd.getDisplayName() + "=>" + paramV.getClass().getComponentType());
							int length = Array.getLength(paramV);
							for(int j = 0; j < length; j++) {
								Object paramElementV = Array.get(paramV, j);
								if(paramElementV instanceof String && !paramElementV.toString().equals(elementV.toString())) {
									logger.debug("1第" + i + "个元素的字符串属性:" + pd.getName() + "，值:" + elementV.toString() + "与参数的值:" + paramElementV.toString() + "不一致，去掉该元素");
									copyList.set(i, null);
									break;
								} else if(!paramElementV.equals(elementV)) {
									logger.debug("2第" + i + "个元素的属性:" + pd.getName() + "，值:" + elementV.toString() + "与参数的值:" + paramElementV.toString() + "不一致，去掉该元素");
									copyList.set(i, null);
									break;
								}
							}
						} else if(elementV instanceof String && !paramV.toString().equalsIgnoreCase(elementV.toString())) {
							//属性不一致，删除
							logger.debug("3第" + i + "个元素的字符串属性:" + pd.getName() + "，值:" + elementV.toString() + "与参数的值:" + paramV.toString() + "不一致，去掉该元素");
							copyList.set(i, null);
						} else if(paramV.toString().equals("0")){
							
						}
						else  if(!paramV.equals(elementV)) {
							logger.debug("4第" + i + "个元素的属性:" + pd.getName() + "，值:" + elementV.toString() + "与参数的值:" + paramV.toString() + "不一致，去掉该元素");
						copyList.set(i, null);
						}

						i++;
					}

				}
			}
		}
		List<T> returnList = new ArrayList<T>();
		if(copyList.size() < 1) {
			return returnList;
		}
		for(Object o : copyList) {
			if(o != null) {
				returnList.add((T)o);
			}
		}
		return returnList;

	}
}