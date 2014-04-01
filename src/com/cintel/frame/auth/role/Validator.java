package com.cintel.frame.auth.role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import com.cintel.frame.web.action.BaseValidator;

/**
 * @version : $Id: Validator.java 15613 2010-01-12 10:20:51Z wangshuda $
 */
public class Validator extends BaseValidator {
	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());
	//
	public void validate(String methodName, ActionForm form) {

		DynaActionForm domainForm = (DynaActionForm) form;
		RoleInfoImpl roleInfo = (RoleInfoImpl)domainForm.get("command");
		if ("insert".equalsIgnoreCase(methodName) || "update".equalsIgnoreCase(methodName)) {
			this.rejectNullOrEmpty(roleInfo.getRoleId(), "roleId");
			this.rejectNullOrEmpty(roleInfo.getRoleTitle(), "roleName");
		}
	}
}