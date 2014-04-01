package com.cintel.frame.auth.func.pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.web.action.BaseDispatchAction;


/**
 * 
 * @file    : FuncAuthUrlPatternAction.java
 * @author  : WangShuDa
 * @date    : 2008-11-14
 * @corp    : CINtel
 * @version : 1.0
 */
public class FuncAuthUrlPatternAction extends BaseDispatchAction {
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	
	//
	private IFuncAuthUrlPatternService funcAuthUrlPatternService;
	
	@Override
	public IFuncAuthUrlPatternService getService() {
		return this.funcAuthUrlPatternService;
	}

	public void setFuncAuthUrlPatternService(IFuncAuthUrlPatternService funcAuthUrlPatternService) {
		this.funcAuthUrlPatternService = funcAuthUrlPatternService;
	}
	
	private void saveFuncItemInfo(HttpServletRequest request) {
		String funcId = request.getParameter("command.funcId");
		//
		FuncItem funcItemInfo = funcAuthUrlPatternService.getFuncItemInfo(funcId);
		request.setAttribute("funcItemInfo", funcItemInfo);
	}
	
	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.saveFuncItemInfo(request);
		return super.delete(mapping, form, request, response);
	}
	
	@Override
	public ActionForward insert(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.saveFuncItemInfo(request);
		return super.insert(mapping, form, request, response);
	}
	
	@Override
	public ActionForward search(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.saveFuncItemInfo(request);
		return super.search(mapping, form, request, response);
	}
}
