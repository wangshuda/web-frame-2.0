package com.cintel.frame.properties.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.cintel.frame.web.action.BaseValidator;


public class Validator extends BaseValidator {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(Validator.class);
    
	@SuppressWarnings("unused")
	public void validate(String methodName, ActionForm form) {
		//
		DynaActionForm domainForm = (DynaActionForm)form;
		DbProperty domain = (DbProperty)(domainForm.get("command"));
		
		this.rejectNullOrEmpty(domain.getV_key(), "v_key");
		this.rejectNullOrEmpty(domain.getValue(), "value");
		
	}

}