package com.cintel.frame.auth.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.cintel.frame.auth.menu.init.IMenuInitService;
import com.cintel.frame.auth.role.init.IRoleInitService;
import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.util.StringUtils;


@SuppressWarnings("unchecked")
public class InitAllItemsServiceImpl implements InitAllItemsService {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(this.getClass());
	//
	
	private IMenuInitService menuInitService;
	
	private IRoleInitService roleInitService;
	
	private List<RoleContext> allRoleItemsList = Collections.EMPTY_LIST;
	
	/**
	 * To init all roles, funcs, roleFuncs at the same time.
	 * @exception Exception
	 * @see com.cintel.frame.auth.init.InitAllItemsService#initAllItems()
	 */
	@Transactional
	public void initAllItems() throws Exception{
		
		//(1) To init all func items
		menuInitService.insertAllFuncItems();
		
		//(2) To init all role items
		roleInitService.insertAllRoleItems();
		
		//(3) To init all roleFuncItems
		List<String> roleIdList = new ArrayList<String>();
		
		//(3.1) To get all roleId
		for(RoleContext roleContext : allRoleItemsList) {
			String roleId = roleContext.getRoleId();
			if(StringUtils.hasText(roleId)) {
				roleIdList.add(roleId);
			}
		}
		//(3.2) To init all roleId
		for(String roleId : roleIdList) {
			menuInitService.insertRoleFuncItems(roleId);
		}
	}
	
	
	/***************get and set methods******************/
	
	public IMenuInitService getMenuInitService() {
		return menuInitService;
	}
	public void setMenuInitService(IMenuInitService menuInitService) {
		this.menuInitService = menuInitService;
	}
	public IRoleInitService getRoleInitService() {
		return roleInitService;
	}
	public void setRoleInitService(IRoleInitService roleInitService) {
		this.roleInitService = roleInitService;
	}
	public List<RoleContext> getAllRoleItemsList() {
		return allRoleItemsList;
	}
	public void setAllRoleItemsList(List<RoleContext> allRoleItemsList) {
		this.allRoleItemsList = allRoleItemsList;
	}
}
