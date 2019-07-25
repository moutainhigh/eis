package com.maicard.site.converter;


import java.util.HashMap;  
import java.util.Hashtable;  
import java.util.Iterator;  
import java.util.List;
import java.util.Map;  
import java.util.Map.Entry;  

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;  
import com.thoughtworks.xstream.converters.UnmarshallingContext;  
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;  
import com.thoughtworks.xstream.io.HierarchicalStreamReader;  
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;  
import com.thoughtworks.xstream.mapper.DefaultMapper;


public class XStreamMapConverter extends AbstractCollectionConverter {  


	public XStreamMapConverter() {  
		super(new DefaultMapper(XStream.class.getClassLoader()))  ;
	}  

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {  
		//System.out.println("Test Convert:" + type.getName());
		return type.equals(HashMap.class)  
				|| type.equals(Hashtable.class)  
				|| type.getName().equals("java.util.LinkedHashMap")  
				|| type.getName().equals("sun.font.AttributeMap") // Used by java.awt.Font in JDK 6  
				;  
	}  

	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {  
		//System.out.println("xxxxxxxxxConvert:" + source.getClass().getName() + "/" + source);
		map2xml(source, writer, context, null);
		/*Map map = (Map) source;  
		for(Object key : map.keySet()){
			System.out.println("Yyyyyyyy" + key.toString() + "/" + map.get(key).getClass().getName());
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, key.toString(), Entry.class); 
			if(map.get(key).getClass().equals(ArrayList.class)){
				map2xml(map.get(key), writer, context);
			} else {
				writer.addAttribute("key",  key.toString());  
				writer.addAttribute("value", map.get(key).toString()); 
			}
			writer.endNode();  
		}
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {  
			Entry entry = (Entry) iterator.next();  
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, "property", Entry.class);  

			writer.addAttribute("key",  entry.getKey().toString());  
			writer.addAttribute("value", entry.getValue().toString());  
			writer.endNode();  
		} */
		
	}  
	@SuppressWarnings("unchecked")
	protected void map2xml(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context, String name) {
		Map<String, Object> map;
		List<Object> list;
		String key;
		Object subvalue;
		if (value.getClass().getName().indexOf("Map") >= 0) {
			map = (Map<String, Object>) value;
			if(name == null){
				name = "map";
			}
			//System.out.println("XXXXXXXXXXXX Name=" + name);
			//writer.startNode(name);
			for (Iterator<Entry<String, Object>> iterator = map.entrySet()
					.iterator(); iterator.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) iterator
						.next();
				key = (String) entry.getKey();
				subvalue = entry.getValue();
				if (subvalue.getClass().getName().indexOf("String") >= 0) {
					writer.startNode(key);
					writer.setValue((String) subvalue);
					writer.endNode();
				} else {
					map2xml(subvalue, writer, context, key);
				}
			}
			//writer.endNode();

		} else 	if (value.getClass().getName().indexOf("List") >= 0) {

			list = (List<Object>) value;
			if(name == null){
				name = list.get(0).getClass().getSimpleName() + "List";
			}
			//System.out.println("YYYYYYYYYY Name=" + name);
			writer.startNode(name);
			for (Object subval : list) {
				subvalue = subval;
				if (subvalue.getClass().getName().indexOf("String") >= 0) {
					writer.startNode(list.get(0).getClass().getSimpleName());
					writer.setValue((String) subvalue);
					writer.endNode();
				} else {
					map2xml(subvalue, writer, context, "subList");
				}
			}
			writer.endNode();
		} else {
			writer.startNode(StringUtils.uncapitalize(value.getClass().getSimpleName()));
			context.convertAnother(value);
			writer.endNode();
		}
	}

	@SuppressWarnings("rawtypes")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {  
		Map map = (Map) createCollection(context.getRequiredType());  
		populateMap(reader, context, map);  
		return map;  
	}  

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {  
		while (reader.hasMoreChildren()) {  
			reader.moveDown();  
			Object key = reader.getAttribute("key");  
			Object value = reader.getAttribute("value");  
			map.put(key, value);  
			reader.moveUp();  
		}  
	}  
}  