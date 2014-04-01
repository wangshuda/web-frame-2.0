package com.cintel.frame.properties;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : PropertiesLoader.java
 * @author  : WangShuDa
 * @date    : 2008-12-14
 * @corp    : CINtel
 * @version : 1.0
 */
@SuppressWarnings("unchecked")
public class PropertiesLoader {
	private static Log log = LogFactory.getLog(PropertiesLoader.class);
	
	//
	private Properties mergedProperties = new Properties();
	private List<PropertiesInfo> propertiesInfoList = Collections.EMPTY_LIST;

	/**
	 * 
	 * @method: getMessage
	 * @return: String
	 * @author: WangShuDa
	 * @param key
	 * @return
	 */
	public String getMessage(String key){
		return getMessage(key, "");
	}
	
	/**
	 * 
	 * @method: getMessage
	 * @return: String
	 * @author: WangShuDa
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getMessage(String key, String defaultValue){
		String rtnVal = defaultValue;
		try {
			rtnVal = mergedProperties.getProperty(key, defaultValue);			
		}
		catch(Exception ex) {
			log.warn("getMessage with key = " + key + ";defaultValue=" + defaultValue + "; errro!", ex);
			rtnVal = defaultValue;
		}
		return rtnVal;
	}
	
	/**
	 * 
	 * @method: initial
	 * @return: void
	 * @author: WangShuDa
	 * @param fileLocations
	 * @throws IOException
	 */
	public void initial(String[] fileLocations) throws IOException {
		PropertiesInfo propertiesInfo;
		propertiesInfoList = new ArrayList<PropertiesInfo>();
		for(String fileLocation:fileLocations) {
			propertiesInfo = new PropertiesInfo(fileLocation);
			propertiesInfoList.add(propertiesInfo);
			//
			mergePropertiesIntoMap(propertiesInfo.getProperties(), mergedProperties);
		}
	}

	
	/**
	 * 
	 * @method: reload
	 * @return: void
	 * @author: WangShuDa
	 * @param fileLocation
	 * @throws IOException
	 */
	public void reload(String fileLocation) throws IOException {
		for(PropertiesInfo propertiesInfo:propertiesInfoList) {
			if(propertiesInfo.getFileLocation().equals(fileLocation)) {
				propertiesInfo.reload();
				//
				mergePropertiesIntoMap(propertiesInfo.getProperties(), mergedProperties);
				//
				break;
			}
		}
	}


	/**
	 * 
	 * @method: mergePropertiesIntoMap
	 * @return: void
	 * @author: WangShuDa
	 * @param props
	 * @param map
	 */
	private void mergePropertiesIntoMap(Properties props, Map map) {
		if(map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		//
		if(props != null) {
			String key;
			for(Enumeration en = props.propertyNames(); en.hasMoreElements(); map.put(key, props.getProperty(key))) {
				key = (String)en.nextElement();
			}
        }
    }
}
