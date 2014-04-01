package com.cintel.frame.shell;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 * 
 * @version $Id: Command.java 13460 2009-12-17 01:08:10Z wangshuda $
 * @history 
 *          1.0.0 2009-12-17 wangshuda created
 */
public class Command {
	private static Log log = LogFactory.getLog(Command.class);
	//
	protected String cmdTextPattern;
	
	protected String successResponsePattern;
	
	//
	protected Object[] cmdArgs;
	
	/**
	 * getCommand
	 */
	public String getCommandText() {
		return MessageFormat.format(cmdTextPattern, cmdArgs);
	}

	protected boolean resultStrParse(String resultStr) {
		String successStr = MessageFormat.format(successResponsePattern, cmdArgs);
		return resultStr.indexOf(successStr) >= 0;
	}
	/**
	 * assertIsOk
	 * 
	 * @param resultStr
	 * @return
	 */
	public boolean assertIsOk(String resultStr) {
		return this.assertIsOk(resultStr, true);
	}
	
	/**
	 * assertIsOk
	 * 
	 * @param resultStr
	 * @param throwException
	 * @return
	 */
	public boolean assertIsOk(String resultStr, boolean throwException) {
		boolean parseResult = this.resultStrParse(resultStr);
		if(parseResult) {
			return true;
		}
		else {
			if(throwException) {
				String commandTxt = this.getCommandText();
				
				String reportText = MessageFormat.format("Execute command \"{0}\" error. Reusult Text:\"{1}\"", commandTxt, resultStr);
				log.warn(reportText);
				//
				throw new ErrorReportException(new ErrorInfo("error.executeCmd", new String[]{commandTxt, resultStr}));
			}
		}
		return false;
	}
	// ----------------- get/set methods --------------------
	public String getSuccessResponsePattern() {
		return successResponsePattern;
	}

	public void setSuccessResponsePattern(String successResponsePattern) {
		this.successResponsePattern = successResponsePattern;
	}
	
	public String getCmdTextPattern() {
		return cmdTextPattern;
	}

	public void setCmdTextPattern(String cmdTextPattern) {
		this.cmdTextPattern = cmdTextPattern;
	}

	public Object[] getCmdArgs() {
		return cmdArgs;
	}

	public void setCmdArgs(Object[] cmdArgs) {
		this.cmdArgs = cmdArgs;
	}
}
