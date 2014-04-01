package com.cintel.frame.auth.init;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @file    : InitAllItemsAction.java
 * @author  : WangShuDa
 * @date    : 2010-01-28
 * @corp    : CINtel
 * @version : 1.0
 */
public class InitAllItemsAction extends DispatchAction {
	
	private InitAllItemsService initAllItemsService;

	public InitAllItemsService getInitAllItemsService() {
		return initAllItemsService;
	}
	public void setInitAllItemsService(InitAllItemsService initAllItemsService) {
		this.initAllItemsService = initAllItemsService;
	}

	/**
	 * 
	 * @method: insertAllItems
	 * @return: ActionForward
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initAllItems(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		initAllItemsService.initAllItems();
		return mapping.findForward("success");
	}
}