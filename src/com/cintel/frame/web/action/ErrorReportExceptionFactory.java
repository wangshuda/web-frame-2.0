package com.cintel.frame.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @file : $Id: ErrorReportExceptionFactory.java 13450 2009-12-17 00:30:18Z wangshuda $
 * @corp : CINtel
 */
public class ErrorReportExceptionFactory {
	private static Log log = LogFactory.getLog(ErrorReportExceptionFactory.class);
	
	public static ErrorReportException generate(Exception exception) {
		log.debug("Call generate with exception!");
		return new ErrorReportException(exception);
	}
	
	public static ErrorReportException generate(String errorCode, String errorMsg, String[] variable, Exception ex) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(errorCode);
		errorInfo.setErrorMsg(errorMsg);
		//
		if(variable != null) {
			errorInfo.addParamArr(variable);
		}
		
		return new ErrorReportException(errorInfo);

	}
	
	public static ErrorReportException generate(String errorCode, Exception ex) {
		return generate(errorCode, null, null, ex);

	}
}
