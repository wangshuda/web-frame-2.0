package com.cintel.frame.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cintel.frame.web.page.PageResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseAction extends DispatchAction {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected BaseValidator validator = null;
	
	protected Errors errors = new Errors();
	
	protected ActionForwardKey actionForwardKey;
	
	public ActionForwardKey getActionForwardKey() {
		return actionForwardKey;
	}
	public void setActionForwardKey(ActionForwardKey actionForwardKey) {
		this.actionForwardKey = actionForwardKey;
	}
	
	//
	protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.errors.clearSavedErrors();
		//
		String method = mapping.getParameter();
		String params = request.getParameter(method);
		//
		if (this.validator != null && this.validator.needValidate(params)) {
			boolean existError = this.validator.doValidate(params, form, request);
			if (existError) {
				this.errors = this.validator.getErrors();
				return this.reportError(mapping, request);
			}
		}
		return super.execute(mapping, form, request, response);
	}
	// 
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String isJsonReq = request.getParameter("isJsonReq");
		String ajax = request.getParameter("ajax");
		//
		try {
			return this.doExecute(mapping, form, request, response);
		}
		catch (ErrorReportException errorReportException) {
			this.errors.saveError(errorReportException.getErrorInfo());
			if(isJsonReq != null || ajax != null) {
				response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				this.writeWithGson(errorReportException.getErrorInfo(), response);
				return (ActionForward)null;
			}
			return this.reportError(mapping, request);
		}
		catch (Exception ex) {
			log.error("Error:", ex);
			this.errors.saveError(new ErrorInfo(ex));
			if(isJsonReq != null || ajax != null) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				this.writeWithGson(new ErrorInfo(ex), response);
				return (ActionForward)null;
			}
			return this.reportError(mapping, request);
		}
	}
	//
	/**
     * @method: writeWithGson
     */
	protected void writeWithGson(final Object obj, HttpServletResponse response) throws IOException {
		//
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		//
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		//
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(PageResult.class, new com.cintel.frame.web.page.GsonTypeAdapter4PageResult()).create();
		//
		PrintWriter out = response.getWriter();
		String jsonText = gson.toJson(obj);
		out.print(jsonText);
		out.close();
		//
		if (log.isDebugEnabled()) {
			log.debug(jsonText);
		}
	}
	//
	/**
     * @method: writeAjaxStr
     */
	protected void writeAjaxStr(final String str, HttpServletResponse response) throws IOException {
		//
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		//
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.print(str);
		out.close();
		//
		if (log.isDebugEnabled()) {
			log.debug(str);
		}
	}
	// ---------------Error------------------------
	protected void saveError(ErrorInfo errorInfo) {
		this.errors.saveError(errorInfo);
		return;
	}

	protected void saveError(ErrorInfo errorInfo, HttpServletRequest request) {
		this.errors.saveError(errorInfo);
		saveErrors(request);
		return;
	}
	
	protected boolean existError() {
		return errors.existError();
	}
	
	public void saveErrors(HttpServletRequest request) {
		request.setAttribute("errorsInfo", this.errors.getErrors());
	}
	/**
	 * reportError
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected ActionForward reportError(ActionMapping mapping, HttpServletRequest request) {
		//
		saveErrors(request);
		return mapping.findForward(this.getActionForwardKey().getError());
	}
	
	public BaseValidator getValidator() {
		return this.validator;
	}
	
	public void setValidator(BaseValidator validator) {
		this.validator = validator;
	}
}
