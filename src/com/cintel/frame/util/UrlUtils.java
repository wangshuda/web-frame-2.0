/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cintel.frame.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Provides static methods for composing URLs.<p>Placed into a separate class for visibility, so that changes to
 * URL formatting conventions will affect all users.</p>
 *
 * @author Ben Alex
 * @version $Id: UrlUtils.java 42053 2014-01-06 22:18:57Z wangshuda $
 */
public final class UrlUtils {
	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(UrlUtils.class);
    //~ Constructors ===================================================================================================

    private UrlUtils() {
    }

    //~ Methods ========================================================================================================

    /**
     * Obtains the full URL the client used to make the request.<p>Note that the server port will not be shown
     * if it is the default server port for HTTP or HTTPS (ie 80 and 443 respectively).</p>
     *
     * @param scheme DOCUMENT ME!
     * @param serverName DOCUMENT ME!
     * @param serverPort DOCUMENT ME!
     * @param contextPath DOCUMENT ME!
     * @param requestUrl DOCUMENT ME!
     * @param servletPath DOCUMENT ME!
     * @param requestURI DOCUMENT ME!
     * @param pathInfo DOCUMENT ME!
     * @param queryString DOCUMENT ME!
     *
     * @return the full URL
     */
    private static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String contextPath,
        String requestUrl, String servletPath, String requestURI, String pathInfo, String queryString) {
        return buildContextUrl(scheme, serverName, serverPort, contextPath) + buildRequestUrl(servletPath, requestURI, contextPath, pathInfo, queryString);
    }

    private static String buildContextUrl(String scheme, String serverName, int serverPort, String contextPath) {
        boolean includePort = true;

        if ("http".equals(scheme.toLowerCase()) && (serverPort == 80)) {
            includePort = false;
        }

        if ("https".equals(scheme.toLowerCase()) && (serverPort == 443)) {
            includePort = false;
        }

        return scheme + "://" + serverName + ((includePort) ? (":" + serverPort) : "") + contextPath;
    }
    /**
     * Obtains the web application-specific fragment of the URL.
     *
     * @param servletPath DOCUMENT ME!
     * @param requestURI DOCUMENT ME!
     * @param contextPath DOCUMENT ME!
     * @param pathInfo DOCUMENT ME!
     * @param queryString DOCUMENT ME!
     *
     * @return the URL, excluding any server name, context path or servlet path
     */
    private static String buildRequestUrl(String servletPath, String requestURI, String contextPath, String pathInfo,
        String queryString) {

        String uri = servletPath;

        if (uri == null) {
            uri = requestURI;
            uri = uri.substring(contextPath.length());
        }

        return uri + ((pathInfo == null) ? "" : pathInfo) + ((queryString == null) ? "" : ("?" + queryString));
    }

    public static String getFullRequestUrl(HttpServletRequest request) {
        return buildFullRequestUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(),
        		request.getRequestURL().toString(), request.getServletPath(), request.getRequestURI(), request.getPathInfo(), request.getQueryString());
    }
    
    public static String getContextPath(PageContext pageContext) {
    	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    	return request.getContextPath();
    }

    public static String getContextUrl(PageContext pageContext) {
    	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    	return buildContextUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
    }
    
    public static String getRequestUrl(HttpServletRequest request) {
    	
    	return buildRequestUrl(request.getServletPath(), request.getRequestURI(), request.getContextPath(), request.getPathInfo(), request.getQueryString());
    }
    
    public static Map<String, String> parseReqParaMap(String requestUrl) {
        int indexOfQuestionMark = requestUrl.indexOf('?');
        Map<String, String> parameterMapInUrl = new HashMap<String, String>(0);
        if(indexOfQuestionMark > 0) {
            String parametersStrInRequestUrl = requestUrl.substring(indexOfQuestionMark + 1);
            String [] parametersArr = parametersStrInRequestUrl.split("&");
            for(String parameterInfo:parametersArr) {
                String [] parameterEntry = parameterInfo.split("=");
                parameterMapInUrl.put(parameterEntry[0], (parameterEntry.length > 1 ? parameterEntry[1] : ""));
            }
        }
        return parameterMapInUrl;
    }
    @SuppressWarnings("unchecked")
	public static String getRequestUrlWithParameters(HttpServletRequest request) {
    	StringBuffer result = new StringBuffer();
    	String requestUrl =  buildRequestUrl(request.getServletPath(), request.getRequestURI(), request.getContextPath(), request.getPathInfo(), request.getQueryString());
    	result.append(requestUrl);
    	
        Map<String, String> parameterMapInUrl = parseReqParaMap(requestUrl);
    	//
    	Map parameterMap = request.getParameterMap();
    	Set<Entry<String,String[]>> parameterMapSet = parameterMap.entrySet();

    	StringBuffer parameterBuff = new StringBuffer();
		for(Entry entry:parameterMapSet) {
			String[] valueArr = (String[]) entry.getValue();
			if(!parameterMapInUrl.containsKey(entry.getKey())) {
				parameterBuff.append(entry.getKey() + "=" + valueArr[0] + "&");
			}
		}
		int parameterBuffLen = parameterBuff.length();
		if(parameterBuffLen > 0) {
			parameterBuff.deleteCharAt(parameterBuffLen - 1);
			//
            int indexOfQuestionMark = requestUrl.indexOf('?');
            
			result.append(indexOfQuestionMark > 0 ? "&" : "?");
			result.append(parameterBuff);
		}

    	return result.toString();
    }
}
