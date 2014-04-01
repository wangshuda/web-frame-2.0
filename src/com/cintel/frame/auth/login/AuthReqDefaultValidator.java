package com.cintel.frame.auth.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.cintel.frame.web.action.BaseValidator;

/**
 * @file : $Id: AuthReqDefaultValidator.java 40931 2013-08-01 07:54:56Z wangshuda $
 * @corp : CINtel
 */
public class AuthReqDefaultValidator extends BaseValidator {
	
	@Override
	public void validate(String methodName, ActionForm form, HttpServletRequest request) {
        String userName = request.getParameter(AuthConstants.USER_NAME);
		//
		this.rejectNullOrEmpty(userName, "login_user_name");
		this.rejectNullOrEmpty(request.getParameter(AuthConstants.USER_PASSWORD), "login_password");
        //
        if(log.isInfoEnabled()) {
            log.info(new StringBuffer("Login:").append(userName).append(" from ").append(request.getRemoteAddr()));
        }
	}
}
