package com.cintel.frame.auth.role.init;

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
public class RoleInitAction extends DispatchAction {
	
	private IRoleInitService roleInitService;

	public IRoleInitService getRoleInitService() {
		return roleInitService;
	}

	public void setRoleInitService(IRoleInitService roleInitService) {
		this.roleInitService = roleInitService;
	}

	/**
	 * 
	 * @method: initAllRolesItems
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initAllRolesItems(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		roleInitService.insertAllRoleItems();
		return mapping.findForward("success");
	}
}