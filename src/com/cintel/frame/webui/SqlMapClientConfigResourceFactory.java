package com.cintel.frame.webui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import com.cintel.frame.util.xml.dom4j.XmlToolBox;
import com.cintel.frame.util.xml.dom4j.XmlUtils;

/**
 * 
 * @file    : SqlMapClientConfigResourceFactory.java
 * @author  : WangShuDa
 * @date    : 2009-5-1
 * @corp    : CINtel
 * @version : 1.0
 */
public class SqlMapClientConfigResourceFactory implements ServletContextAware, FactoryBean {
	//
	private Log log = LogFactory.getLog(SqlMapClientConfigResourceFactory.class);
	
	// return sqlMapClientXmlResource for FactoryBean
	private Resource sqlMapClientXmlResource = null;
	
	private Resource configLocation;
	
	private String sqlMapLocationConfig;
	
	// -------------- ServletContextAware methods ---------------------
	public void setServletContext(ServletContext servletContext) {
		//
		try {
			InputStream sqlMapConfigInputStream = configLocation.getInputStream();
			XmlToolBox xmlToolBox = new XmlToolBox(sqlMapConfigInputStream);
			//
			if(StringUtils.hasText(sqlMapLocationConfig)) {
				Resource[] resourcesArr = null;
				String[] sqlMapLocationPatternArr = StringUtils.tokenizeToStringArray(sqlMapLocationConfig, ",; \t\n");
				//
				ServletContextResourcePatternResolver resolve = new ServletContextResourcePatternResolver(servletContext);
					//
					String fileUrl = null;
					Map<String, String> attrMap = new HashMap<String, String>();
					for (String sqlMapLocationPattern : sqlMapLocationPatternArr) {
						resourcesArr = resolve.getResources(sqlMapLocationPattern.trim());
						for (Resource sqlMapFile : resourcesArr) {
							fileUrl = String.valueOf(sqlMapFile.getURL());
							//
							attrMap.put("url", fileUrl);
							xmlToolBox.addElement("sqlMapConfig", "sqlMap", attrMap, null);
						}
					}
					//<!DOCTYPE sqlMapConfig PUBLIC "-//iBATIS.com//DTD SQL Map Config 2.0//EN" "dtd/sql-map-config-2.dtd">
					xmlToolBox.setDocType("sqlMapConfig", "-//iBATIS.com//DTD SQL Map Config 2.0//EN", "dtd/sql-map-config-2.dtd");   
					//
					sqlMapClientXmlResource = new InputStreamResource(new ByteArrayInputStream(XmlUtils.doc2XmlString(xmlToolBox.getDoc()).getBytes()));
					//
					if(log.isDebugEnabled()) {
						log.debug(XmlUtils.doc2XmlString(xmlToolBox.getDoc()));
					}
			}
			log.debug("ibatis files load ok!");
			//
		}
		catch(IOException ex) {
			sqlMapClientXmlResource = null;
			log.fatal("Build iBatis file error!", ex);
		}
	}

	// -------------------------- FactoryBean methods --------------------------
	public Object getObject() throws Exception {
		return this.sqlMapClientXmlResource;
	}

	public Class getObjectType() {
		return Resource.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	// -------------------------- get/set methods --------------------------
	public Resource getSqlMapClientXmlResource() {
		return sqlMapClientXmlResource;
	}

	public void setSqlMapClientXmlResource(Resource sqlMapClientXmlResource) {
		this.sqlMapClientXmlResource = sqlMapClientXmlResource;
	}

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public String getSqlMapLocationConfig() {
		return sqlMapLocationConfig;
	}

	public void setSqlMapLocationConfig(String sqlMapLocationConfig) {
		this.sqlMapLocationConfig = sqlMapLocationConfig;
	}


}
