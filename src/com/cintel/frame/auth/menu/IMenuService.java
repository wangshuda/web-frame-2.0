package com.cintel.frame.auth.menu;

import java.util.LinkedList;
import java.util.List;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.auth.user.UserDetails;

/**
 * 
 * @file    : IMenuService.java
 * @author  : WangShuDa
 * @date    : 2008-9-18
 * @corp    : CINtel
 * @version : 1.0
 */
public interface IMenuService {

	/**
	 * 
	 * @method: getAllFuncItemsList
	 * @return: List<FuncItem>
	 * @return
	 */
	public List getAllFuncItemsList();
	
	/**
	 * 
	 * @method: getItsMenuList
	 * @return: List
	 * @return
	 */
	public List<FuncItem> getItsMenuList();
	
	/**
	 * 
	 * @method: getItsMenuList
	 * @return: List<FuncItem>
	 * @param roleName
	 * @return
	 */
	public abstract List<FuncItem> getItsMenuList(String roleName);
	
	/**
	 * 
	 * @method: getFuncItemsLinedList
	 * @return: LinkedList<FuncItem>
	 * @param menuId
	 * @return
	 */
	public LinkedList<FuncItem> getFuncItemsLinedList(int menuId);
	
	public LinkedList<FuncItem> getFuncItemsLinedList(String menuKey);
	
	/**
	 * 
	 * @method: initUserMenuInfo
	 * @return: void
	 * @param userDetails
	 * @throws Exception
	 */
	public void initUserMenuInfo(UserDetails userDetails);
}
