package com.cintel.frame.auth.menu.init;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @file    : MenuInitAction.java
 * @author  : WangShuDa
 * @date    : 2008-9-25
 * @corp    : CINtel
 * @version : 1.0
 */
public class MenuInitAction extends DispatchAction {
	
	private IMenuInitService menuInitService;

	public void setMenuInitService(IMenuInitService menuInitService) {
		this.menuInitService = menuInitService;
	}
	
	/**
	 * 
	 * @method: initFuncItems
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initAllFuncItems(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		menuInitService.insertAllFuncItems();
		return mapping.findForward("success");
	}

	/**
	 * 
	 * @method: initRoleFuncItems
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initRoleFuncItems(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		String roleId = request.getParameter("roleId");
		menuInitService.insertRoleFuncItems(roleId);
		
		return mapping.findForward("success");
	}




	
}