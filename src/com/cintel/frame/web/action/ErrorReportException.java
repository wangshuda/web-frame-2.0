package com.cintel.frame.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @file    : ErrorReportException.java
 * @author  : WangShuDa
 * @date    : 2009-1-5
 * @corp    : CINtel
 * @version : 1.0
 */
public class ErrorReportException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	//
	private ErrorInfo errorInfo = new ErrorInfo();

	private static Log log = LogFactory.getLog(ErrorReportException.class);
	// Constructors
	public ErrorReportException() {
	}

	public ErrorReportException(Exception ex) {
		log.warn("", ex);
		this.setErrorInfo(new ErrorInfo(ex));
	}

    public ErrorReportException(String errorCode, String errorMsg) {
        this.errorInfo.setErrorCode(errorCode);
        this.errorInfo.setErrorMsg(errorMsg);
    }
    
	public ErrorReportException(String errorCode) {
		this.errorInfo.setErrorCode(errorCode);
	}
	
	public ErrorReportException(ErrorInfo errorInfo) {
		this.setErrorInfo(errorInfo);
	}
	// get and set methods.
	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ErrorInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
}
