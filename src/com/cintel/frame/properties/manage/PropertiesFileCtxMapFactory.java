package com.cintel.frame.properties.manage;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

/**
 * @file    : PropertiesFileCtxMapFactory.java
 * @author  : WangShuDa
 * @date    : 2009-12-12
 * @corp    : CINtel
 * @version : 1.1
 */
@SuppressWarnings("unchecked")
public class PropertiesFileCtxMapFactory implements FactoryBean, ApplicationContextAware, ServletContextAware {
	@SuppressWarnings("unused")
	private final static Log log = LogFactory.getLog(PropertiesFileCtxMapFactory.class);
	//
	private Map<String, PropertiesFileCtx> propertiesFileCtxMap = Collections.EMPTY_MAP;
	//
	public Object getObject() throws Exception {
		return this.propertiesFileCtxMap;
	}

	public Class getObjectType() {
		return Map.class;
	}

	@SuppressWarnings("unchecked")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		boolean includePrototypes = false;
		boolean allowEagerInit = true;
		//
		this.propertiesFileCtxMap = applicationContext.getBeansOfType(PropertiesFileCtx.class, includePrototypes, allowEagerInit);
		//
		log.debug("find PropertiesFileCtx count:" + (propertiesFileCtxMap == null ? 0 :propertiesFileCtxMap.size()));
	}

	public boolean isSingleton() {
		return true;
	}

	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}
}
