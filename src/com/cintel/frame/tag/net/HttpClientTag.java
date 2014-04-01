package com.cintel.frame.tag.net;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.security.Security;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;

import com.cintel.frame.util.StringUtils;

public class HttpClientTag extends TagSupport{
	
	private static final long serialVersionUID = 6294032789411906955L;
	private Log log = LogFactory.getLog(HttpClientTag.class);
	
	private String targetUrl;
	private String bodyMessageStr;
	private String requestMethod = "GET";
	private int timeOut = 3;
	private boolean https = false;
	private String httpsUser;
	private String httpsPasswd;
	private boolean clearDnsCache = false;
	private Map<String,String> headerInfoMap = null;
	
	public void setTargetUrl(String targetUrl){
		this.targetUrl = targetUrl;
	}
	public String getTargetUrl(){
		return this.targetUrl;
	}
	
	public void setBodyMessageStr(String bodyMessageStr){
		this.bodyMessageStr = bodyMessageStr;
	}
	public String getBodyMessageStr(){
		return this.bodyMessageStr;
	}
	
	public void setRequestMethod(String requestMethod){
		this.requestMethod = requestMethod;
	}
	public String getRequestMethod(){
		return this.requestMethod;
	}
	
	public void setTimeOut(int timeOut){
		this.timeOut = timeOut;
	}
	public int getTimeOut(){
		return this.timeOut;
	}
	
	public void setHttps(boolean https){
		this.https = https;
	}
	public boolean getHttps(){
		return this.https;
	}
	
	public void setHttpsUser(String httpsUser){
		this.httpsUser = httpsUser;
	}
	public String getHttpsUser(){
		return this.httpsUser;
	}
	
	public void setHttpsPasswd(String httpsPasswd){
		this.httpsPasswd = httpsPasswd;
	}
	public String getHttpsPasswd(){
		return this.httpsPasswd;
	}
	
	public void setClearDnsCache(boolean clearDnsCache){
		this.clearDnsCache = clearDnsCache;
	}
	public boolean getClearDnsCache(){
		return this.clearDnsCache;
	}
	
	public int doStartTag()throws JspException{
		if(!StringUtils.hasText(this.targetUrl)){
			return SKIP_BODY;
		}else{
			HttpClient httpClient = new HttpClient();
			HttpMethod method = null;
			
			// identify the http method
			if("POST".equals(this.requestMethod)){
				method = new PostMethod(this.targetUrl);
			}else {
				method = new GetMethod(this.targetUrl);
			}
			
			// set the timeout value for process of get data from server
			httpClient.getParams().setSoTimeout(this.timeOut * 60 * 1000);
			
			try{
				// excute the request;
				int statusCode = httpClient.executeMethod(method);
				if(statusCode != HttpStatus.SC_OK){
					log.debug(method.getStatusLine());
				}
				
				// get the response header information
				Header[] headerArr = method.getResponseHeaders();
				this.headerInfoMap = new HashMap<String,String>();
				this.headerInfoMap.put("statusCode", String.valueOf(method.getStatusCode()));
				this.headerInfoMap.put("statusText", method.getStatusText());
				for(Header header : headerArr){
					this.headerInfoMap.put(header.getName(), header.getValue());
				}
				
				String responseBodyStr = method.getResponseBodyAsString();
				this.bodyMessageStr = responseBodyStr;
				
				// put the specific value into the context.
				pageContext.setAttribute("bodyContent", this.bodyMessageStr, PageContext.REQUEST_SCOPE);
				pageContext.setAttribute("headerInfoMap", this.headerInfoMap,PageContext.REQUEST_SCOPE);
				
				if(this.clearDnsCache){
					Security.setProperty("networkaddress.cache.ttl", "10");
				}
			}catch(HttpException he){
				
			}catch(IOException ie){
				
			}finally{
				// release the connection
				method.releaseConnection();
			}
			return SKIP_BODY;
		}
	}
	
	public void setHeaderInfoMap(Map<String,String> headerInfoMap){
		this.headerInfoMap = headerInfoMap;
	}
	public Map<String,String> getHeaderInfoMap(){
		return this.headerInfoMap;
	}
	
	/*public static void main(String[] args)throws Exception{
		HttpClientTag httpTag = new HttpClientTag();
		String targetUrl = "http://www.baidu.com";
		httpTag.setTargetUrl(targetUrl);
		httpTag.doTagStart();
		
		System.out.println("body :");
		System.out.println(httpTag.getBodyContent());
	}*/
}
