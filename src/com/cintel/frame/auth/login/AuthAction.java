package com.cintel.frame.auth.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.auth.Constants;
import com.cintel.frame.auth.context.SecurityContext;
import com.cintel.frame.auth.context.SecurityContextImpl;
import com.cintel.frame.auth.menu.IMenuService;
import com.cintel.frame.auth.random.image.IRandomImageGenerator;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;
import com.cintel.frame.web.action.BaseAction;
import com.cintel.frame.web.action.ErrorInfo;

/**
 * 
 * @file    : AuthAction.java
 * @author  : WangShuDa
 * @date    : 2009-2-25
 * @corp    : CINtel
 * @version : 1.0
 */
public abstract class AuthAction extends BaseAction {

	protected Log log = LogFactory.getLog(this.getClass());
	
	private IRandomImageGenerator randomImageGenerator;
	
	private boolean needVerifyCode = true;

	private IMenuService menuService; 
	
	private boolean canLoginOneUserInOneSession = false;
	
    private boolean configJavaAwtHeadless = false;
    
	public void setRandomImageGenerator(IRandomImageGenerator randomImageGenerator) {
		this.randomImageGenerator = randomImageGenerator;
	}

	public void setNeedVerifyCode(boolean needVerifyCode) {
		this.needVerifyCode = needVerifyCode;
	}
	
	/**
	 * logout
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public final ActionForward logout(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserDetails anonymousUserDetails = this.getAnonymousUser();
		if(anonymousUserDetails != null) {
			this.saveUserInfo(request, anonymousUserDetails);
		}
		else {
			request.getSession().removeAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
			request.getSession().invalidate();
            
		}
		return mapping.findForward("logout");
	}
	
	/**
	 * getVerifyCode
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public final ActionForward getVerifyCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//
        if(configJavaAwtHeadless) {
            System.setProperty("java.awt.headless", "true");
        }
		//
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		HttpSession theSession = request.getSession();
		if (theSession != null) {
			String randomString = randomImageGenerator.generateRandomStr();
			theSession.setAttribute(Constants.VERIFY_CODE_SESSION_KEY, randomString);
			response.setContentType("image/jpeg");
			randomImageGenerator.render(randomString, response.getOutputStream());
		}
		return null;
	}

	protected final UserDetails getUserDetailsFromSession(HttpServletRequest request) {
		HttpSession theSession = request.getSession();
		UserDetails userDetails = null;
		if(theSession != null) {
			userDetails = (UserDetails)theSession.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);
		}
		return userDetails;
	}
	//
	private void saveUserInfo(HttpServletRequest request, Object userDetails) {
		HttpSession theSession = request.getSession();
		if (theSession != null) {
			SecurityContext securityContext = new SecurityContextImpl();
			securityContext.setObject(userDetails);
			theSession.setAttribute(SecurityContext.SECURITY_CONTEXT_KEY, userDetails);
			//
		}
	}
	
    private void removeVerifyCodeFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.VERIFY_CODE_SESSION_KEY);
    }
    
	private boolean checkVerifyCode(HttpServletRequest request) {
		String verifyCode = "cin_verifyCode";
		String requestRandomStr = request.getParameter(verifyCode);
		String savedRandomStr = (String) request.getSession().getAttribute(Constants.VERIFY_CODE_SESSION_KEY);
		if (!StringUtils.hasText(requestRandomStr) || !requestRandomStr.equalsIgnoreCase(savedRandomStr)) {
			log.error("checkVerifyCode Fail!");
			return false;
		}
		return true;
	}
	
	protected UserDetails getAnonymousUser() {
		return null;
	}
	
    /**
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
	protected abstract LoginResponse doLogin(ActionForm form, 
			HttpServletRequest request, HttpServletResponse response);
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	public final ActionForward login(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(needVerifyCode) {
			boolean verifyCodeIsOk = checkVerifyCode(request);
			if(!verifyCodeIsOk) {
				this.saveError(new ErrorInfo("error.verifyCode"));
				return this.reportError(mapping, request);
			}
		}
        //
        this.removeVerifyCodeFromSession(request);
		//
		errors.clearSavedErrors();
		//
		LoginResponse loginResponse = this.doLogin(form, request, response);
		if(errors.existError()) {
			return super.reportError(mapping, request);
		}
		else {
            UserDetails userDetails = loginResponse.getUserDetails();
            userDetails.setLoginRequestIp(WebUtils.parseRequestIp(request));
            //
            userDetails.setLoginSessionId(request.getSession().getId());
            //
			this.saveUserInfo(request, userDetails);
			//
			String mappingForwardKey = loginResponse.getForwardKey();
			//
			if(StringUtils.hasText(mappingForwardKey)) {
				return mapping.findForward(mappingForwardKey);
			}
			else {
				return null;
			}
		}
	}

	public IMenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public void setCanLoginOneUserInOneSession(boolean canLoginOneUserInOneSession) {
		this.canLoginOneUserInOneSession = canLoginOneUserInOneSession;
	}

	public boolean isCanLoginOneUserInOneSession() {
		return canLoginOneUserInOneSession;
	}

    public boolean isConfigJavaAwtHeadless() {
        return configJavaAwtHeadless;
    }

    public void setConfigJavaAwtHeadless(boolean configJavaAwtHeadless) {
        this.configJavaAwtHeadless = configJavaAwtHeadless;
    }
}
