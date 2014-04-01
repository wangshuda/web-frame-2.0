package com.cintel.frame.properties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : PropertiesInfo.java
 * @author  : WangShuDa
 * @date    : 2008-12-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class PropertiesInfo {
	private static Log log = LogFactory.getLog(PropertiesInfo.class);
			
	private String bundleKey;
	
	private String fileLocation;
	
	private Properties properties = new Properties();
	//
	private void print() {
		if(log.isDebugEnabled()) {
			OutputStream out = new ByteArrayOutputStream();
			this.properties.list(new PrintStream(out));
			log.debug(out);
		}
	}
	// Construct functions.
	public PropertiesInfo(String fileLocation) throws IOException {
		File file = new File(fileLocation);
		//
		this.bundleKey = file.getName();
		this.fileLocation = fileLocation;
		this.properties.load(new FileInputStream(file));
		//
		this.print();
	}
	
	/**
	 * 
	 * @method: reload
	 * @return: void
	 * @author: WangShuDa
	 * @throws IOException
	 */
	public void reload() throws IOException {
		this.properties.load(new FileInputStream(new File(fileLocation)));
		//
		this.print();
	}
	
	// get & set methods.
	public String getBundleKey() {
		return bundleKey;
	}

	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	//
	public static void main(String args[]) throws IOException {
		@SuppressWarnings("unused")
		PropertiesInfo pro = new PropertiesInfo("D:/ProgramFiles/Java/jakarta-tomcat-5.0.28/webapp-crs/crs-manager/WEB-INF/classes/sys-conf-msg.properties");
		
	}
}
