package com.cintel.frame.auth.menu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.util.StringUtils;

/**
 * 
 * @file    : MenuFromDbService.java
 * @author  : WangShuDa
 * @date    : 2008-9-18
 * @corp    : CINtel
 * @version : 1.0
 */
public class MenuFromSpringService extends MenuBaseService {
	private final static Log log = LogFactory.getLog(MenuFromSpringService.class);
	
	//
	private final static int TOP_PARENT_ID = -1;
	
	private List<FuncItemForSpring> allFuncItemsList = null;

	public void setAllFuncItemsList(List<FuncItemForSpring> allFuncItemsList) {
		this.allFuncItemsList = allFuncItemsList;
		int i = 1;
		for(FuncItemForSpring funcItem: allFuncItemsList) {
			// config status, 
			if(!StringUtils.hasText(funcItem.getStatus())) {
				funcItem.setStatus("A");
				//
				if(!StringUtils.hasText(funcItem.getRequestUrl())) {
					funcItem.setStatus("B");
					//
					log.debug("Set status with 'B' for Menu ID:" + funcItem.getId());
				}
			}
			// config menu order id, order by the sequence position in the list defined in the spring config file.
			if(funcItem.getMenuOrderId() == 0) {
				funcItem.setMenuOrderId(i * 10);
				i += 1;
			}
			// config key, if the key has no text, then set the key with the id value.
			if(!StringUtils.hasText(funcItem.getKey())) {
				funcItem.setKey(String.valueOf(funcItem.getId()));
			}
		}
	}
	
	@Override
	public List<FuncItem> getItsMenuList(String roleId) {
		return getMenuTree(TOP_PARENT_ID, roleId);
	}
	
	/**
	 * 
	 * @method: getMenuTree
	 * @return: List<FuncItem>
	 * @param parentId
	 * @param roleName
	 * @return
	 */
	private List<FuncItem> getMenuTree(int parentId, String roleId) {
		List<FuncItem> rolesFuncItemsList = new LinkedList<FuncItem>();
		for(FuncItemForSpring funcItem: allFuncItemsList) {
			if(funcItem.getParentId() == parentId && funcItem.doAuthenticate(roleId)) {
				rolesFuncItemsList.add(funcItem);
			}
		}
		Collections.sort(rolesFuncItemsList);
		//
		for(FuncItem parentFuncItem: rolesFuncItemsList) {
			parentFuncItem.setSubItemsList(getMenuTree(parentFuncItem.getId(), roleId));
		}

		return rolesFuncItemsList;
	}

	public List getAllFuncItemsList() {
		return allFuncItemsList;
	}
}
