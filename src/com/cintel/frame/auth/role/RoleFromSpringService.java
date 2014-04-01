package com.cintel.frame.auth.role;

import java.util.List;

import com.cintel.frame.auth.user.RoleContext;

/**
 * 
 * @file    : MenuFromDbService.java
 * @author  : WangShuDa
 * @date    : 2008-9-18
 * @corp    : CINtel
 * @version : 1.0
 */
public class RoleFromSpringService  {

	@SuppressWarnings("unused")
	private final static int TOP_PARENT_ID = -1;
	
	private List<RoleContext> allRoleItemsList = null;

	public List<RoleContext> getAllRoleItemsList() {
		return allRoleItemsList;
	}

	public void setAllRoleItemsList(List<RoleContext> allRoleItemsList) {
		this.allRoleItemsList = allRoleItemsList;
	}
}
