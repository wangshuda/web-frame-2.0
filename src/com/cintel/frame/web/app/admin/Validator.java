package com.cintel.frame.web.app.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;

import com.cintel.frame.web.action.BaseValidator;


public class Validator extends BaseValidator {
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(Validator.class);

	public void validate(String methodName, ActionForm form) {
		/*
		ErrorInfo errorInfo = null;
		DynaActionForm domainForm = (DynaActionForm)form;
		
		WebAdmin domain = (WebAdmin)(domainForm.get("command"));
		*/
		return;
	}

}
