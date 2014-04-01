package com.cintel.frame.auth.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cintel.frame.auth.menu.IMenuService;

/**
 * 
 * @file    : MenuServiceFactory.java
 * @author  : WangShuDa
 * @date    : 2009-4-25
 * @corp    : CINtel
 * @version : 1.1
 */
public class MenuServiceFactory implements FactoryBean, ApplicationContextAware {
	@SuppressWarnings("unused")
	private final static Log log = LogFactory.getLog(MenuServiceFactory.class);
	
	private String menuServiceSpringBeanId = "MenuFromSpringService";
	
	// initial the menuService in method:setApplicationContext and return it at method :getObject
	private IMenuService menuService;
	
	public Object getObject() throws Exception {
		return menuService;
	}

	public Class getObjectType() {
		return IMenuService.class;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(log.isDebugEnabled()) {
			log.debug("Using the menu service with the spring bean id:" + menuServiceSpringBeanId);
		}
		menuService = (IMenuService)applicationContext.getBean(menuServiceSpringBeanId);
	}

	public boolean isSingleton() {
		return true;
	}
	
	public String getMenuServiceSpringBeanId() {
		return menuServiceSpringBeanId;
	}

	public void setMenuServiceSpringBeanId(String menuServiceSpringBeanId) {
		this.menuServiceSpringBeanId = menuServiceSpringBeanId;
	}

}
