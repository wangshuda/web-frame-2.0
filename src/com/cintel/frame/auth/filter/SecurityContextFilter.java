package com.cintel.frame.auth.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.context.SecurityContextHolder;
import com.cintel.frame.auth.context.SecurityContextImpl;
import com.cintel.frame.auth.user.FuncItem;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.MyAntPathMatcher;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.UrlUtils;

public class SecurityContextFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = -7171814195269685571L;

	@SuppressWarnings("unused")
	protected final Log log = LogFactory.getLog(this.getClass());

	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;
	
	private String notLoginUrl = null;
	
	private Set<String> unAuthGrantedUrlList = null;
	
	private Set<String> onlyCheckAuthInfoUrlList = null;
	
	private Set<String> authGrantedUrlList = null;

	private boolean needCheckGrantedAuthInfo = true;
	
	public void destroy() {
        this.filterConfig = null;
	}

	/**
	 * 
	 * @method: match
	 * @return: boolean
	 * @author: WangShuDa
	 * @param requestUrl
	 * @param checkList
	 * @return
	 */
	private boolean match(String requestUrl, Set<String> checkList) {
		boolean isMatch = false;
		MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
		for (String  urlStr : checkList) {
			isMatch = pathMatcher.matchIgnoreCase(urlStr, requestUrl);
			if(isMatch) {
				return isMatch;
			}
		}
		return isMatch;
	}
	
	
	/**
	 * 
	 * @method: forwardToNotLoginPage
	 * @return: void
	 * @author: WangShuDa
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    private void forwardToNotLoginPage(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    	RequestDispatcher notLoginDispatcher = request.getRequestDispatcher(notLoginUrl);
    	notLoginDispatcher.forward(request,response);
    }
	/**
	 * 
	 * @method: doFilter
	 * @author: WangShuDa
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	
		if (!(request instanceof HttpServletRequest)) {
			throw new ServletException("Can only process HttpServletRequest");
		}
		//
		boolean securityCtxSaveSucceed = false;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String requestUrl = UrlUtils.getRequestUrlWithParameters(httpRequest);
		
		if(log.isDebugEnabled()) {
			log.debug(requestUrl);
		}
		//
		//if the requestUrl matches the unnecessary ulr list, then continue to the next filter in the chain.
		boolean isUnnecessary = this.match(requestUrl, unAuthGrantedUrlList);
		if(isUnnecessary) {
			log.debug("The request url matches the url pattern defined in the unAuthGrantedUrlList.");
			
			chain.doFilter(request, response);
			return;
		}
		
		//if the request url matches the auth info check list,
		// that is to say, if the userdetails can be accessed from the session, 
		// then the request will be regarded as valid request, otherwise is bad.
		// 
		boolean isCheckAuthInfo = this.match(requestUrl, onlyCheckAuthInfoUrlList);
		
		if(isCheckAuthInfo && log.isDebugEnabled()) {
			log.debug("The request url matches the url pattern defined in the onlyCheckAuthInfoUrlList.");
		}
		
		//
		HttpSession session = httpRequest.getSession(false);
		if(isCheckAuthInfo && session == null) {
			log.warn("session is null.");
			//
        	this.forwardToNotLoginPage(request, response);
        	return;
		}
		//
		UserDetails userDetails = null;
		if(session != null) {
			userDetails = (UserDetails)session.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
		}
		//
        if(isCheckAuthInfo && userDetails == null) {
        	log.warn("userDetails is null:" + requestUrl);
        	//
        	this.forwardToNotLoginPage(request, response);
        	return;
        }
    	//if needCheckGrantedAuthInfo is true, means the filter should check the url with granted with the logined user.
        // the urls info have been saved in the userdetails with funcItemList.
        // 
    	if(needCheckGrantedAuthInfo && !isCheckAuthInfo) {
    		log.debug("needCheckGrantedAuthInfo && !isCheckAuthInfo.");
    		//
    		if(userDetails == null) {
    			log.warn("userDetails saved in session is null.");
    			//
	        	this.forwardToNotLoginPage(request, response);
	        	return;
    		}
    		List<FuncItem> funcItmesList = userDetails.getFuncItemList();
    		boolean isCheckGrantedInfo = this.match(requestUrl, authGrantedUrlList);
    		//
    		if(isCheckGrantedInfo) {
    			log.debug("the request url matches the authGrantedUrlList.");
    			//
    			boolean grantedAuthInfoCheckOk = this.checkGrantedAuthInfo(requestUrl, funcItmesList);
    			if(!grantedAuthInfoCheckOk) {
    				log.warn("Check granted auth info failed.");
    				//
    	        	this.forwardToNotLoginPage(request, response);
    	        	return;
    			}
    		}
    	}
    	//
    	try {
			SecurityContext securityContext = new SecurityContextImpl();
			securityContext.setObject(userDetails);
            SecurityContextHolder.setContext(securityContext);
            //
            securityCtxSaveSucceed = true;
            //
            log.debug("Bind the user info with the current thread:" + Thread.currentThread().getId());
            //
            chain.doFilter(request, response);
        }
    	finally {
    		// if the securityContext has been saved, here should be clear.
    		if(securityCtxSaveSucceed) {
            	log.debug("Clear the user info from the current thread:" + Thread.currentThread().getId());
                SecurityContextHolder.clearContext();
    		}
        }
	}
    
    /**
     * 
     * @method: checkGrantedAuthInfo
     * @return: boolean
     * @author: WangShuDa
     * @param funcItmesList
     * @param requestUrl
     * @return
     */
    @SuppressWarnings("deprecation")
	private boolean checkGrantedAuthInfo(String requestUrl, List<FuncItem> funcItmesList) {
    	MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
    	String grantedUrlStr = null;
    	boolean match = false;
    	List<FuncItem> subList = null;
    	for(FuncItem item:funcItmesList) {
    		grantedUrlStr = item.getAuthUrlPattern();
    		if(StringUtils.hasText(grantedUrlStr)) {
    			match = pathMatcher.matchIgnoreCase(grantedUrlStr, requestUrl);
    		}
    		//
    		if(match) {
    			return match;
    		}
    		//
    		for(String grantedUrlStrInList:item.getAuthUrlPatternList()) {
        		if(StringUtils.hasText(grantedUrlStrInList)) {
        			match = pathMatcher.matchIgnoreCase(grantedUrlStrInList, requestUrl);
        		}
        		log.debug("Check grantedUrlStr : " + grantedUrlStrInList);
    			if(match) {
    				log.debug("Check Ok with grantedUrlStr : " + grantedUrlStrInList);
    				return match;
    			}
    		}
    		//
    		if(!match) {
    			subList = item.getSubItemsList();
    			if(subList != null && !subList.isEmpty()) {
    				match = checkGrantedAuthInfo(requestUrl, subList);
        			if(match) {
        				return match;
        			}
    			}
    		}
		}
    	//
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

	public void setNotLoginUrl(String notLoginUrl) {
		this.notLoginUrl = notLoginUrl;
	}

	public void setAuthGrantedUrlList(Set<String> authGrantedUrlList) {
		this.authGrantedUrlList = authGrantedUrlList;
	}

	public void setUnAuthGrantedUrlList(Set<String> unAuthGrantedUrlList) {
		this.unAuthGrantedUrlList = unAuthGrantedUrlList;
	}

	public void setNeedCheckGrantedAuthInfo(boolean needCheckGrantedAuthInfo) {
		this.needCheckGrantedAuthInfo = needCheckGrantedAuthInfo;
	}

	public void setOnlyCheckAuthInfoUrlList(Set<String> onlyCheckAuthInfoUrlList) {
		this.onlyCheckAuthInfoUrlList = onlyCheckAuthInfoUrlList;
	}        
}
