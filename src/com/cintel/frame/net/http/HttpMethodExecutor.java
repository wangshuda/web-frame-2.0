package com.cintel.frame.net.http;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.net.http.auth.HttpAuthCtx;
import com.cintel.frame.util.FileUtils;
import com.cintel.frame.web.action.ErrorInfo;
import com.cintel.frame.web.action.ErrorReportException;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2010-12-8 wangshuda created
 */
public class HttpMethodExecutor {
	private static Log log = LogFactory.getLog(HttpMethodExecutor.class);
	
	// properties and be configed.
	private String targetURL;
	
	private int timeOut = 5000;
	
	private HttpAuthCtx authCtx;
	
	private Map<String, String> requestHeaderMap = new HashMap<String, String>();
	
	private List<Integer> enableStatusCodeList = new ArrayList<Integer>();
	
	// properties used in class.
	private HttpClient httpClient = new HttpClient();
		
	public HttpMethodExecutor() {
		this.initialClient();
		//
		enableStatusCodeList.add(HttpStatus.SC_OK);
	}

	public void addEnableStatusCode(int[] enableStatusCode) {
		for(int enableCode:enableStatusCode) {
			enableStatusCodeList.add(enableCode);
		}
	}
	
	public int executeMethod(HttpMethod httpMethod) {
		return this.executeMethod(httpMethod, true);
	}
			
	public int executeMethod(HttpMethod httpMethod, boolean releaseConnection) {
		for(Map.Entry<String, String> entry:requestHeaderMap.entrySet()) {
			httpMethod.setRequestHeader(entry.getKey(), entry.getValue());
		}
		if(authCtx != null) {
			httpMethod.setDoAuthentication(true);
		}
		//
		if(log.isDebugEnabled()) {
			try {
				log.debug(httpMethod.getName() + " With URI:" + httpMethod.getURI());
			}
			catch (URIException ex) {
				log.warn("", ex);
			}
		}
		//
		try {
			int statusCode = httpClient.executeMethod(httpMethod);
			//
			if (enableStatusCodeList.contains(statusCode)) {
				return statusCode;
			}
			else {
				log.warn(MessageFormat.format("Response Code :{0},{1}.", statusCode, HttpStatus.getStatusText(statusCode)));
				//
				StringBuffer buffer = new StringBuffer("Can only supported:");
				for(Integer enableCode:enableStatusCodeList) {
					buffer.append(enableCode);
					buffer.append(":");
					buffer.append(HttpStatus.getStatusText(enableCode));
					buffer.append(";");
				}
				log.warn(buffer.toString());
				//
				throw new ErrorReportException(new ErrorInfo("error.HttpUploadService" , new String[]{httpMethod.getName(), targetURL}));
			}
		}
		catch (Exception ex) {
			log.error(targetURL, ex);
			//
			throw new ErrorReportException(new ErrorInfo("error.HttpUploadService", new String[]{httpMethod.getName(), targetURL}));
		}
		finally {
			if(releaseConnection) {
				httpMethod.releaseConnection();
				//
				httpMethod = null;
			}
		}
	}

	private void initAuthConfig(HttpAuthCtx authCtx) {
		if(authCtx != null) {
			httpClient.getParams().setAuthenticationPreemptive(authCtx.isPreemptive());
			//
			Credentials credentials = new UsernamePasswordCredentials(authCtx.getUserName(), authCtx.getUserPwd());
			httpClient.getState().setCredentials(new AuthScope(authCtx.getHostAddr(), authCtx.getHostPort(), AuthScope.ANY_REALM), credentials);
			//
			httpClient.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authCtx.getAuthPrefsList());
		}
	}
	//
	private void initialClient() {
		if(requestHeaderMap.isEmpty()) {
			requestHeaderMap.put("Connection", "close");
		}
		//
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
	}
	
	public Map<String, String> getRequestHeaderMap() {
		return requestHeaderMap;
	}

	public void setRequestHeaderMap(Map<String, String> requestHeaderMap) {
		this.requestHeaderMap = requestHeaderMap;
	}

	public String getTargetURL() {
		return targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public List<Integer> getEnableStatusCodeList() {
		return enableStatusCodeList;
	}

	public void setEnableStatusCodeList(List<Integer> enableStatusCodeList) {
		this.enableStatusCodeList = enableStatusCodeList;
	}

	public HttpAuthCtx getAuthCtx() {
		return authCtx;
	}

	public void setAuthCtx(HttpAuthCtx authCtx) {
		this.authCtx = authCtx;
		//
		this.initAuthConfig(this.authCtx);
	}
	
	public static void main(String []args) {
		//
		HttpAuthCtx authCtx = new HttpAuthCtx();
		//
		authCtx.setUserName("admin");
		authCtx.setUserPwd("cintel1234!@#$");
		//
		HttpMethodExecutor httpMethodExecutor = new HttpMethodExecutor();
		httpMethodExecutor.setAuthCtx(authCtx);
		//
		String targetUrl = "http://192.168.2.89:8089/main.shp";
		httpMethodExecutor.setTargetURL(targetUrl);
		//
		HttpMethod httpMethod = new GetMethod(targetUrl);
		int statusCode = httpMethodExecutor.executeMethod(httpMethod, false);
		try {
			if (statusCode == HttpStatus.SC_OK) {
				FileUtils.copy(httpMethod.getResponseBodyAsStream(), System.out);
			}
		}
		catch (IOException ex) {
			log.error("httpMethod.getResponseBodyAsStream", ex);
			throw new ErrorReportException(new ErrorInfo("error.HttpUploadService", new String[]{httpMethod.getName(), targetUrl}));
		}
		finally {
			httpMethod.releaseConnection();
			//
			httpMethod = null;
		}
	}
}
