package com.cintel.frame.properties;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @file    : PropertiesUtils.java
 * @author  : WangShuDa
 * @date    : 2008-11-24
 * @corp    : CINtel
 * @version : 1.0
 */
public class PropertiesUtils {
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(PropertiesUtils.class);

	private static PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	public static void initial(String[] fileLocations) {
		try {
			propertiesLoader.initial(fileLocations);
			
			log.debug("Properties Loaded!");
		}
		catch(IOException ex) {
			log.error("Failed for properties loading!", ex);
		}
	}
	
	//
	public static void reload(String fileLocation) throws IOException {
		propertiesLoader.reload(fileLocation);
	}
	
	public static String getMessage(String key) {
		return propertiesLoader.getMessage(key, "");
	}
	
	public static String getMessage(String key, String defaultValue){
		return propertiesLoader.getMessage(key, defaultValue);
	}
}
