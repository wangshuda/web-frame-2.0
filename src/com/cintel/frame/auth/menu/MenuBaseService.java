package com.cintel.frame.auth.menu;

import java.util.LinkedList;
import java.util.List;

import com.cintel.frame.auth.UserInfoUtils;
import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.auth.user.GrantedAuthority;
import com.cintel.frame.auth.user.UserDetails;

/**
 * 
 * @file    : MenuBaseService.java
 * @author  : WangShuDa
 * @date    : 2008-9-23
 * @corp    : CINtel
 * @version : 1.1
 */
public abstract class MenuBaseService implements IMenuService {
		
	/**
	 * 
	 * @method: getItsMenuList
	 * @return: List<FuncItem>
	 * @param roleName
	 * @return
	 * @see com.cintel.frame.auth.menu.MenuFromDbService
	 * 		com.cintel.frame.auth.menu.MenuFromSpringService
	 */
	public abstract List<FuncItem> getItsMenuList(String roleName);
	
	
	/**
	 * Get menu info from the logined user info.
	 * 
	 * @method: getItsMenuList
	 * @return
	 */
	public List<FuncItem> getItsMenuList() {
		return UserInfoUtils.getFuncItemsList();
	}

	/**
	 * Provide the method to the userDetails. It will set the func items of the role to the user details.
	 * 
	 * @method: initUserMenuInfo
	 * @param userDetails
	 * @throws Exception
	 * @see com.cintel.crm.auth.login.*AuthAction
	 */
	public void initUserMenuInfo(UserDetails userDetails) {
		GrantedAuthority[] authorityArr = userDetails.getAuthorities();
		//
		if(authorityArr != null) {
			String roleId = null;
			List<FuncItem> funcItemList = null;
			for(GrantedAuthority authority:authorityArr) {
				//roleName = authority.getAuthority();
				if(authority.getRoleContext() != null) {
					roleId = authority.getRoleContext().getRoleId();
				}
				else {
					roleId = authority.getAuthority();
				}
				//
				funcItemList = this.getItsMenuList(roleId);
				authority.setFuncItemList(funcItemList);
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @method: getFuncItemsLinedList
	 * @return: LinkedList
	 * @param menuId
	 * @return
	 * @see com.cintel.frame.auth.menu.MenuTitleTag
	 */
	public LinkedList<FuncItem> getFuncItemsLinedList(int menuId) {
		LinkedList<FuncItem> menuTitleLinedList = new LinkedList<FuncItem>();
		
		List<FuncItem> funcItemsList = this.getItsMenuList();
		// here the menuTitleLinedList is the reference result. All func item will be saved in the linked list.
		this.findItsFuncLink(menuId, funcItemsList, menuTitleLinedList);

		return menuTitleLinedList;
	}
	
	/**
	 * @method: getFuncItemsLinedList with menu key
	 */
	public LinkedList<FuncItem> getFuncItemsLinedList(String menuKey) {
		LinkedList<FuncItem> menuTitleLinedList = new LinkedList<FuncItem>();
		
		List<FuncItem> funcItemsList = this.getItsMenuList();
		// here the menuTitleLinedList is the reference result. All func item will be saved in the linked list.
		this.findItsFuncLink(menuKey, funcItemsList, menuTitleLinedList);

		return menuTitleLinedList;
	}

	/**
	 * 
	 * @method: findItsFuncLink
	 * @return: boolean
	 * @param menuKey
	 * @param funcItemsList
	 * @param resultList
	 * @return
	 */
	private boolean findItsFuncLink(String menuKey, List<FuncItem> funcItemsList, LinkedList<FuncItem> resultList) {
		boolean find = false;
		for(FuncItem item:funcItemsList) {
			if(menuKey.equals(item.getKey())) {
				resultList.add(item);
				return true;
			}
			else {
				if(item.isExistSubMenu()) {
					find = findItsFuncLink(menuKey, item.getSubItemsList(), resultList);
					//
					if(find) {
						resultList.addFirst(item);
						return true;
					}
				}
			}
		}
		//
		return false;
	}
	
	/**
	 * 
	 * @method: findItsFuncLink
	 * @return: boolean
	 * @param menuId
	 * @param funcItemsList
	 * @param resultList
	 * @return
	 */
	private boolean findItsFuncLink(int menuId, List<FuncItem> funcItemsList, LinkedList<FuncItem> resultList) {
		boolean find = false;
		for(FuncItem item:funcItemsList) {
			if(item.getId() == menuId) {
				resultList.add(item);
				return true;
			}
			else {
				if(item.isExistSubMenu()) {
					find = findItsFuncLink(menuId, item.getSubItemsList(), resultList);
					//
					if(find) {
						resultList.addFirst(item);
						return true;
					}
				}
			}
		}
		//
		return false;
	}
}
