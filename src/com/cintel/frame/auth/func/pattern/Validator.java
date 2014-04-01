package com.cintel.frame.auth.func.pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;

import com.cintel.frame.web.action.BaseValidator;

/**
 * 
 * @file : Validator.java
 * @author : WangShuDa
 * @date : 2009-1-14
 * @corp : CINtel
 * @version : 1.0
 */
public class Validator extends BaseValidator {
	protected final Log log = LogFactory.getLog(this.getClass());

	public void validate(String methodName, ActionForm form) {
		return;
	}
}
