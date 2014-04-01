package com.cintel.frame.web.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @file    : ErrorInfo.java
 * @author  : WangShuDa
 * @date    : 2008-11-5
 * @corp    : CINtel
 * @version : 1.0
 */
public class ErrorInfo {
	
	public final static String WARN = "warn";
	public final static String WRONG = "wrong";
	
	private String errorLevel = WARN;
	
	private String errorMsg;
	private String errorCode;
	private String errorType;
	
	private List<String> paramList = new LinkedList<String>();
	
	//
	public ErrorInfo() {
	}

    public static ErrorInfo newInstance(String errCode, String errMsg) {
        ErrorInfo errorInfo = new ErrorInfo(errCode);
        errorInfo.setErrorMsg(errMsg);
        return errorInfo;
    }
    
	public ErrorInfo(Exception ex) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(out));
		errorLevel = WRONG;
		errorMsg = new String(out.toByteArray());
	}

	public ErrorInfo(String errorCode) {
		setErrorCode(errorCode);
	}
	
	public ErrorInfo(String errorCode, String param0) {
		setErrorCode(errorCode);
		addParam(param0);
	}
	
	public ErrorInfo(String errorCode, String param0, String param1) {
		setErrorCode(errorCode);
		addParam(param0);
		addParam(param1);
	}

	public ErrorInfo(String errorCode, String param0, String param1, String param2) {
		setErrorCode(errorCode);
		addParam(param0);
		addParam(param1);
		addParam(param2);
	}

	public ErrorInfo(String errorCode, String[] paramArr) {
		setErrorCode(errorCode);
		addParamArr(paramArr);
	}
	
	//
	public void printInfo(Writer writer) throws IOException {
		writer.write("errorCode:" + errorCode + "\t");
		writer.write("errorMsg:" + errorMsg);
	}
	//
	public boolean isWrong() {
		return WRONG.equalsIgnoreCase(this.errorLevel);
	}
	
	public boolean isWarn() {
		return WARN.equalsIgnoreCase(this.errorLevel);
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public List<String> getParamList() {
		return paramList;
	}
	
	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public void addParam(String param) {
		paramList.add(0, param);
	}
	
	public void addParamArr(String[] paramArr) {
		paramList.addAll(Arrays.asList(paramArr));
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}
