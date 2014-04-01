package com.cintel.frame.auth.menu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.util.StringUtils;

/**
 * @version : $Id: SpringMenuListFactory.java 13588 2009-12-18 00:56:22Z wangshuda $
 */
public class SpringMenuListFactory implements FactoryBean, ApplicationContextAware {
	private final static Log log = LogFactory.getLog(SpringMenuListFactory.class);
	//
	private String excludedMenuIdRegex;
	//
	private List<FuncItem> springMenuItemsList = new LinkedList<FuncItem>();
	//
	public Object getObject() throws Exception {
		log.debug("return springMenuItemsList with size:" + this.springMenuItemsList.size());
		//
		return this.springMenuItemsList;
	}

	public Class getObjectType() {
		return List.class;
	}

	@SuppressWarnings("unchecked")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		log.debug("ApplicationContextAware to SpringMenuListFactory");
		//
		boolean includePrototypes = true;
		boolean allowEagerInit = true;
		//
		Map<String, FuncItem> resultMap = applicationContext.getBeansOfType(FuncItem.class, includePrototypes, allowEagerInit);
		//
		String menuId = null;
		FuncItem funcItem = null;
		//
		Set<Entry<String, FuncItem>> menuItemsEntrySet = resultMap.entrySet();
		for(Entry<String, FuncItem> menuItemEntry:menuItemsEntrySet) {
			menuId = menuItemEntry.getKey();
			funcItem = menuItemEntry.getValue();
			//
			if(!(StringUtils.hasText(excludedMenuIdRegex) && menuId.matches(excludedMenuIdRegex))) {
                if(this.springMenuItemsList.contains(funcItem)) {
                    log.warn("The menu item from Spring confilct with the id or key !");
                    log.warn("Id:" + menuId + "; key:" + funcItem.getKey());
                }
                //
                this.springMenuItemsList.add(funcItem);
			}
		}
		
		log.debug("Loaded total menu items count is:" + this.springMenuItemsList.size());
		//
		Collections.sort(this.springMenuItemsList);
	}

	public boolean isSingleton() {
		return true;
	}

	public String getExcludedMenuIdRegex() {
		return excludedMenuIdRegex;
	}

	public void setExcludedMenuIdRegex(String excludedMenuIdRegex) {
		this.excludedMenuIdRegex = excludedMenuIdRegex;
	}

}
