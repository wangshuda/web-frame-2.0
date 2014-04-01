package com.cintel.frame.auth.func;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.web.action.BaseDispatchAction;
import com.cintel.frame.web.action.ErrorInfo;


/**
 * 
 * @file    : FuncItemAction.java
 * @author  : WangShuDa
 * @date    : 2008-9-22
 * @corp    : CINtel
 * @version : 1.0
 */
public class FuncItemAction extends BaseDispatchAction {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 
	 * @method: insert
	 * @author: WangShuDa
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward insert(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm domainForm = (DynaActionForm)form;
		FuncItem func = (FuncItem)(domainForm.get(commandStr));
		String funcId = func.getId() + "";
		Integer funcCountId = (Integer)getService().get("checkFuncId", funcId);
		if(funcCountId != null && funcCountId > 0) {
			ErrorInfo errorInfo = new ErrorInfo("error.funcIdAlreadyExist");
			saveError(errorInfo);
			return this.reportError(mapping, request);
		}
		
		if(func.getParentId() != -1) {
			Integer funcCountParentId = (Integer)getService().get("checkFuncId", func.getParentId() + "");
			if(funcCountParentId == null || funcCountParentId == 0) {
				ErrorInfo errorInfo = new ErrorInfo("error.funcParentIdError");
				saveError(errorInfo);
				return this.reportError(mapping, request);
			}
		}
		getService().insert(func);
		return mapping.findForward(actionForwardKey.getAfterInsert());
	}
}
