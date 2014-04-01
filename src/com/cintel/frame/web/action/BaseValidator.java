package com.cintel.frame.web.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.cintel.frame.util.StringUtils;

public class BaseValidator {
	@SuppressWarnings("unused")
	protected Log log = LogFactory.getLog(BaseValidator.class);

	protected static String commandStr = "command";

	private String[] needValidateNamesArr = null;

	private String needValidateNames;

	protected Errors errors = new Errors();

	public Errors getErrors() {
		return errors;
	}

	protected void saveError(ErrorInfo errorInfo) {
		errors.saveError(errorInfo);
	}

	public String getNeedValidateNames() {
		return needValidateNames;
	}

	protected void validate(String params, ActionForm form) {
		
	}

	protected void validate(String params, ActionForm form, HttpServletRequest request) {
		this.validate(params, form);
	}
	
	public void setNeedValidateNames(String needValidateNames) {
		if (needValidateNames == null) {
			return;
		}
		//
		this.needValidateNames = needValidateNames.trim();
		this.needValidateNamesArr = needValidateNames.split(",");
	}

	public boolean needValidate(String methodName) {
		boolean rtnVal = false;
        if ("*".equals(needValidateNames)) {
            return true;
        }
        //
        if (needValidateNamesArr != null && StringUtils.hasLength(methodName)) {
            for (int i = 0; i < needValidateNamesArr.length; i++) {
                if (methodName.equalsIgnoreCase(needValidateNamesArr[i])) {
                    rtnVal = true;
                    break;
                }
            }
        }
        //
		return rtnVal;
	}

	public boolean doValidate(String params, ActionForm form, HttpServletRequest request) {
		errors.clearSavedErrors();
		this.validate(params, form, request);
		return errors.existError();
	}

	//
	protected void rejectNullOrEmpty(Object obj, String labelCode) {
		boolean checkOk = false;
		if (obj instanceof String) {
			checkOk = StringUtils.hasText(String.valueOf(obj));
		}
		if (!checkOk) {
			ErrorInfo errorInfo = new ErrorInfo("valid.rejectNullOrEmpty");
			//
			String label = "label." + labelCode;
			errorInfo.addParam(label);

			errors.saveError(errorInfo);
		}
	}

	protected void checkStr(String str, String patt, String errorStr) {
		Pattern pattern = Pattern.compile(patt, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			ErrorInfo errorInfo = new ErrorInfo(errorStr);
			saveError(errorInfo);
		}
	}

	/**
	 * 
	 * @param file
	 * @param maxSize
	 * @param minSize
	 * @throws Exception
	 */
	protected void checkFormFileSize(FormFile file, int maxSize, int minSize) throws Exception {
		ErrorInfo errorInfo = null;
		int fileSize = file.getFileData().length;
		long unit = 1024 * 1024;
		if (fileSize > (maxSize * unit) || fileSize <= (minSize * unit)) {
			errorInfo = new ErrorInfo("error.fileSize", new String[] { file.getFileName(), String.valueOf(maxSize), String.valueOf(minSize) });
			saveError(errorInfo);
		}
	}

	protected void checkFormFileType(FormFile file, String type) throws Exception {
		ErrorInfo errorInfo = null;
		String fileType = "";
		String contentType = file.getContentType();
		fileType = contentType.substring(0, contentType.indexOf("/"));

		if (!fileType.equalsIgnoreCase(type)) {
			errorInfo = new ErrorInfo("error.fileType", new String[] { file.getFileName(), contentType, type });
			saveError(errorInfo);
		}
	}
}
