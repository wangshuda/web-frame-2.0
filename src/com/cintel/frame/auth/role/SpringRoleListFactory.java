package com.cintel.frame.auth.role;

import java.text.MessageFormat;
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

import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.util.StringUtils;

/**
 * @file    : SpringMenuListFactory.java
 * @author  : WangShuDa
 * @date    : 2009-4-25
 * @corp    : CINtel
 * @version : 1.1
 */
public class SpringRoleListFactory implements FactoryBean, ApplicationContextAware {
	private final static Log log = LogFactory.getLog(SpringRoleListFactory.class);
	//
	private String excludedMenuIdRegex;
	//
	private List<RoleContext> springRoleItemsList = new LinkedList<RoleContext>();
	//
	public Object getObject() throws Exception {
		return springRoleItemsList;
	}

	public Class getObjectType() {
		return List.class;
	}

	@SuppressWarnings("unchecked")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		boolean includePrototypes = true;
		boolean allowEagerInit = true;
		//
		Map<String, RoleContext> resultMap = applicationContext.getBeansOfType(RoleContext.class, includePrototypes, allowEagerInit);
		//
		String menuId = null;
		RoleContext roleItem = null;
		//
		Set<Entry<String, RoleContext>> roleItemsEntrySet = resultMap.entrySet();
		for(Entry<String, RoleContext> roleItemEntry:roleItemsEntrySet) {
			menuId = roleItemEntry.getKey();
			//
			roleItem = roleItemEntry.getValue();
			//
			if(roleItem.getRoleLevel() < 0 || roleItem.getRoleGroup() < 0) {
				log.fatal(MessageFormat.format("Role level or group  Can not be negative! see spring id:{0}.", menuId));
			}
			//
			if(log.isDebugEnabled()) {
				log.debug(menuId);
			}
			//
			if(!(StringUtils.hasText(excludedMenuIdRegex) && menuId.matches(excludedMenuIdRegex))) {
                if(springRoleItemsList.contains(roleItem)) {
                    log.error("The role item from Spring confilct with the id or key !");
                    log.error("Id:" + menuId + "; key:" + roleItem.getKey());
                }
                //
                springRoleItemsList.add(roleItem);
			}
		}
		//
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
