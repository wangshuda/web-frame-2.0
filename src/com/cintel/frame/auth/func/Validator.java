package com.cintel.frame.auth.func;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.web.action.BaseValidator;

public class Validator extends BaseValidator {
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	
	//
	public void validate(String methodName, ActionForm form) {

		DynaActionForm domainForm = (DynaActionForm) form;
		FuncItem func = (FuncItem)domainForm.get("command");
		if ("insert".equalsIgnoreCase(methodName) || "update".equalsIgnoreCase(methodName)) {
			this.rejectNullOrEmpty(func.getTitle(), "funcTitle");
			this.rejectNullOrEmpty(func.getKey(), "funcKey");
		}
	}
}