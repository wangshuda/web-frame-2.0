package com.cintel.frame.shell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.net.http.HttpMethodExecutor;
import com.cintel.frame.util.FileUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;
import com.google.gson.Gson;

/**
 * 
 * @version $Id: ShellCmdExecutorHttpImpl.java 33906 2012-04-20 01:30:37Z wangshuda $
 * @history 
 *          1.0.0 2009-12-17 wangshuda created
 */
@SuppressWarnings("unchecked")
public class ShellCmdExecutorHttpImpl implements ShellCmdExecutor {
	private static Log log = LogFactory.getLog(ShellCmdExecutorHttpImpl.class);
	
	public HttpMethodExecutor httpMethodExecutor;
	
	private Map<String, Command> commandMap = Collections.EMPTY_MAP;
	
	public boolean execute(Command command) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		this.execute(command, out);
		
		String resultStr = new String(out.toByteArray());
		
		return command.assertIsOk(resultStr);
	}
	
	public boolean execute(String cmdKey, String[] cmdArgs) {
		Command command = commandMap.get(cmdKey);
        if(command != null) {
            command.setCmdArgs(cmdArgs);
            return this.execute(command);
        }
        else {
            log.error(new StringBuffer("Can not find the cmd:").append(cmdKey).append("in ").append(new Gson().toJson(commandMap)));
            return false;
        }
	}
	
	public String execute(String[] cmdArgs, String cmdKey) {
		Command command = commandMap.get(cmdKey);
        //
        String resultStr = "";
        if(command != null) {
            command.setCmdArgs(cmdArgs);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //
            this.execute(command, out);
            resultStr = new String(out.toByteArray());
        }
        else {
            log.error(new StringBuffer("Can not find the cmd:").append(cmdKey).append("in ").append(new Gson().toJson(commandMap)));
        }
		//
		return resultStr;
	}
	
	public int execute(String cmdKey, String[] cmdArgs, OutputStream out) {
		int rtnValue = -1;
		Command command = commandMap.get(cmdKey);
		
		if(command != null) {
            command.setCmdArgs(cmdArgs);
			rtnValue = this.execute(command, out);
		}
        else {
            log.error(new StringBuffer("Can not find the cmd:").append(cmdKey).append("in ").append(new Gson().toJson(commandMap)));
        }
		return rtnValue;
	}
	
	public int execute(Command command, OutputStream out) {
		int rtnValue = -1;
		
		String cmdText = command.getCommandText();
		PostMethod postMethod = new PostMethod(httpMethodExecutor.getTargetURL());
		//
		postMethod.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(cmdText.getBytes())));
		
		int statusCode = httpMethodExecutor.executeMethod(postMethod, false);
		
		try {
			if (statusCode == HttpStatus.SC_OK) {
				rtnValue = FileUtils.copy(postMethod.getResponseBodyAsStream(), out);
			}
		}
		catch (Exception ex) {
			log.warn("", ex);
			//
			throw new ErrorReportException(new ErrorInfo("error.executeCmd", new String[]{cmdText, ""}));
		}
		finally {
			postMethod.releaseConnection();
			//
			postMethod = null;
		}
		return rtnValue;
	}

	public HttpMethodExecutor getHttpMethodExecutor() {
		return httpMethodExecutor;
	}

	public void setHttpMethodExecutor(HttpMethodExecutor httpMethodExecutor) {
		this.httpMethodExecutor = httpMethodExecutor;
	}

	public Map<String, Command> getCommandMap() {
		return commandMap;
	}

	public void setCommandMap(Map<String, Command> commandMap) {
		this.commandMap = commandMap;
	}
	
}
