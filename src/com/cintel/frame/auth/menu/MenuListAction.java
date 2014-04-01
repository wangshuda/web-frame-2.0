package com.cintel.frame.auth.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @file    : MenuListAction.java
 * @author  : WangShuDa
 * @date    : 2008-9-18
 * @corp    : CINtel
 * @version : 1.0
 */
public class MenuListAction extends DispatchAction {

	private IMenuService menuService;


	
	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}
	
	public IMenuService getMenuService() {
		return this.menuService;
	}
	
	/**
	 * 
	 * @method: list
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		List itsMenuList = menuService.getItsMenuList();
		request.setAttribute("menuList", itsMenuList);
		return mapping.findForward("list");
	}


}