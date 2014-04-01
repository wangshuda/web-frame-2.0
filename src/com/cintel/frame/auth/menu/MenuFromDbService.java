package com.cintel.frame.auth.menu;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.webui.IDomainService;

/**
 * 
 * @file    : MenuFromDbService.java
 * @author  : WangShuDa
 * @date    : 2008-9-18
 * @corp    : CINtel
 * @version : 1.0
 */
public class MenuFromDbService extends MenuBaseService {
	
	private final static int TOP_PARENT_ID = -1;
	
	//private IDomainService funcInfoService;
	
	private IDomainService roleFuncInfoService;
	
/*	public void setFuncInfoService(IDomainService funcInfoService) {
		this.funcInfoService = funcInfoService;
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FuncItem> getItsMenuList(String roleId) {
		//return funcInfoService.list("listFuncItemByRoleId", roleId);
		
		return this.loadMenuTree(roleId, TOP_PARENT_ID);
	}
	
	@SuppressWarnings("unchecked")
	public List<FuncItem> loadMenuTree(String roleId, int parentId) {
		Map<String, String> queryMap = new HashMap<String, String>(2);
		queryMap.put("roleId", roleId);
		queryMap.put("parentId", String.valueOf(parentId));
		//
		List<FuncItem> funcItemList = roleFuncInfoService.list("listFuncItemByRoleIdAndParentId", queryMap);
		if(funcItemList != null && funcItemList.size() > 0) {
			//Collections.sort(funcItemList);
			//
			for(FuncItem funcItem:funcItemList) {
				List<FuncItem> subFuncItemList = funcItem.getSubItemsList(); 
				if(subFuncItemList != null && subFuncItemList.size() > 0) {
					for(FuncItem subFuncItem:subFuncItemList) {
						subFuncItem.setSubItemsList(loadMenuTree(roleId, subFuncItem.getId()));
					}
				}
			}
		}
		//
		return funcItemList;
	}
	
	@SuppressWarnings("unchecked")
	public List getAllFuncItemsList() {
		return Collections.EMPTY_LIST;
	}

	public IDomainService getRoleFuncInfoService() {
		return roleFuncInfoService;
	}

	public void setRoleFuncInfoService(IDomainService roleFuncInfoService) {
		this.roleFuncInfoService = roleFuncInfoService;
	}
	
}
