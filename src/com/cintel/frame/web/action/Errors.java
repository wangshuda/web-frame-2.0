package com.cintel.frame.web.action;

import java.util.LinkedList;
import java.util.List;

public class Errors {
	private List<ErrorInfo> errors = new LinkedList<ErrorInfo>();
	
	private boolean onlyWarn = true; 
	public boolean isOnlyWarn() {
		return onlyWarn;
	}
	public List<ErrorInfo> getErrors() {
		return errors;
	}
	
	public void clearSavedErrors() {
		errors.clear();
		onlyWarn = true;
	}
	
	public void saveError(ErrorInfo errorInfo) {
		if(errorInfo.isWrong()) {
			onlyWarn = false;
		}
		//
		errors.add(0, errorInfo);
	}
	
	public boolean existError() {
		return (errors.size() > 0);
	}
}