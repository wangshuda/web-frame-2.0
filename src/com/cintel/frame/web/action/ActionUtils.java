package com.cintel.frame.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ActionUtils {
	public static Map getReferenceDataMap(HttpServletRequest request, List<String> referenceData) {
		//convert from list to map
		HashMap<String, Object> referenceDataMap = new HashMap<String, Object>();
		if (referenceData == null)
			return referenceDataMap;
		for (int i = 0; i < referenceData.size(); i++) {
			String item = referenceData.get(i);
			String[] subitems = item.split(":");
			if (subitems.length != 3)
				throw new RuntimeException("Invalid item:" + item);
			String name = subitems[0];
			String type = subitems[1];
			if (type.equals("string")) {
				String value = subitems[2];
				referenceDataMap.put(name, value);
			} 
			else if (type.equals("array")) {
				String[] value = subitems[2].split(",");
				referenceDataMap.put(name, value);
			} else if (type.equals("map")) {
				String[] keyvalue = subitems[2].split(",");
				if (keyvalue.length % 2 != 0)
					throw new RuntimeException("Invalid map value:" + item);
				TreeMap<String, String> map = new TreeMap<String, String>();
				for (int j = 0; j < keyvalue.length; j += 2) {
					map.put(keyvalue[j], keyvalue[j + 1]);
				}
				referenceDataMap.put(name, map);
			} 
			else if (type.equals("bean")) {
				String beanId = subitems[2];
				WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
				Object bean = wac.getBean(beanId);
				referenceDataMap.put(name, bean);
			} 
			else if (type.equals("parameter")) {
				String parameterName = subitems[2];
				String para = request.getParameter(parameterName);
				referenceDataMap.put(name, para);
			} else
				throw new RuntimeException("Not supported type:" + type);
		}
		return referenceDataMap;
	}
}
