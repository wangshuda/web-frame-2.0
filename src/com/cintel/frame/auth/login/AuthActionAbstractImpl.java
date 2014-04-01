package com.cintel.frame.auth.login;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.cintel.frame.auth.user.GrantedAuthority;
import com.cintel.frame.auth.user.GrantedAuthorityImpl;
import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.auth.user.UserContext;
import com.cintel.frame.auth.user.UserDetails;
import com.cintel.frame.auth.user.UserDetailsImpl;
import com.cintel.frame.encrypt.Md5Digest;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;
import com.cintel.frame.web.action.ErrorInfo;

/**
 * @file : $Id: AuthActionAbstractImpl.java 39782 2013-06-04 03:19:53Z wangshuda $
 * @corp : CINtel
 */
@SuppressWarnings("unchecked")
public class AuthActionAbstractImpl extends AuthAction {

	private boolean encryptPwdWithMd5 = false;
	
	private AuthService authService;
	
	private String userNameKey = AuthConstants.USER_NAME;
	
	private String passwordKey = AuthConstants.USER_PASSWORD;

	protected String requestIpAddrKey = AuthConstants.REQUEST_IP_ADDR;
	
    protected String reqSeesionIdKey = AuthConstants.REQUEST_SESSION_ID;
    
    protected String reqTimeKey = AuthConstants.REQUEST_TIME;
    
	private List<String> reqParametersKey = Collections.EMPTY_LIST;

	@SuppressWarnings("unchecked")
	private Map<String, Object> getReqParametersMap(HttpServletRequest request) {
		Map<String, Object> reqParametersMap = new HashMap<String, Object>();
		
		// Attention: here the value in the map is string array.
		Map allParametersMap = request.getParameterMap();
		//		
		Set<Entry<String, String[]>> entrySet = allParametersMap.entrySet();
		String key = null;
		String value = null;
		String valuesArr[] = null;
		for(Entry<String, String[]> entry:entrySet) {
			key = entry.getKey();
			valuesArr = (String[])entry.getValue();
			//
			if((valuesArr != null && valuesArr.length > 0) 
					&& (userNameKey.equals(key) || passwordKey.equals(passwordKey) || reqParametersKey.contains(key))) {
				//
				value = valuesArr[0];
				//
				if(passwordKey.equals(key) && encryptPwdWithMd5) {
                    if(encryptPwdWithMd5) {
                        reqParametersMap.put(key, Md5Digest.getInstance().getEncryptedPwd(value));
                    }
				}
				else {
					reqParametersMap.put(key, value);
				}
			}
		}
		//
        reqParametersMap.put(reqSeesionIdKey, request.getSession().getId());
        reqParametersMap.put(reqTimeKey, DateUtils.getCurrentTimeString());
		reqParametersMap.put(requestIpAddrKey, WebUtils.parseRequestIp(request));
		//
		return reqParametersMap;
	}
	
	protected LoginResponse doLogin(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter(userNameKey);
		String password = request.getParameter(passwordKey);
		
        if(encryptPwdWithMd5) {
            password = Md5Digest.getInstance().getEncryptedPwd(password);
        }
        //
		Map<String, Object> reqParametersMap = getReqParametersMap(request);
		
		LoginResponse loginResponse = new LoginResponse();
		//
		String errorCode = null;
		UserContext userContext = null;
		
		RoleContext[] roleContextArr = this.getAuthService().getRoleContextArr(reqParametersMap);
		//
		if(roleContextArr == null || roleContextArr.length == 0) {
			errorCode = "error.noAuthRoleInfo";
		}
		else {
			userContext = this.getAuthService().getUserContext(reqParametersMap);
			if(userContext == null) {
				errorCode = "error.noUserContext";
			}
			else if(!password.equals(userContext.getUserPassword())) {
				errorCode = "error.invalidUserPwd";
			}
		}
		//
		if(StringUtils.hasText(errorCode)) {
			saveError(new ErrorInfo(errorCode), request);
			//
			loginResponse.setForwardKey(this.getActionForwardKey().getError());
			return loginResponse;
		}
		else {
			GrantedAuthority[] authorityArr = this.getGrantedAuthorityArr(roleContextArr);
			//
			UserDetails userDetails = new UserDetailsImpl(userName, password, authorityArr);
			
			if(this.isCanLoginOneUserInOneSession()) {
				UserDetails oldUserDetailsInSession = this.getUserDetailsFromSession(request);
				if(oldUserDetailsInSession != null && !userDetails.equals(oldUserDetailsInSession)) {
					saveError(new ErrorInfo("error.multiUserLogin"), request);
					//
					loginResponse.setForwardKey(this.getActionForwardKey().getError());
					return loginResponse;
				}
			}
			//
			this.getMenuService().initUserMenuInfo(userDetails);
			//
			userDetails.setUserContext(userContext);
			//
			loginResponse.setUserDetails(userDetails);
			loginResponse.setForwardKey(this.getActionForwardKey().getSuccess());
		}
		//
		return loginResponse;
	}
	
	protected GrantedAuthority[] getGrantedAuthorityArr(RoleContext[] roleContextArr) {
		GrantedAuthority[] authorityArr = new GrantedAuthority[roleContextArr.length];
		int i = 0;
		for(RoleContext roleContext:roleContextArr) {
            if(roleContext != null) {
                authorityArr[i++] = new GrantedAuthorityImpl(roleContext);
            }
		}
        //
		return (i != 0) ? authorityArr : new GrantedAuthority[0];
	}
	
	// ------------------- get/set methods -------------------
	public String getPasswordKey() {
		return passwordKey;
	}
	public void setPasswordKey(String passwordKey) {
		this.passwordKey = passwordKey;
	}
	
	public String getUserNameKey() {
		return userNameKey;
	}
	public void setUserNameKey(String userNameKey) {
		this.userNameKey = userNameKey;
	}
	
	public AuthService getAuthService() {
		return authService;
	}
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	public boolean isEncryptPwdWithMd5() {
		return encryptPwdWithMd5;
	}

	public void setEncryptPwdWithMd5(boolean encryptPwdWithMd5) {
		this.encryptPwdWithMd5 = encryptPwdWithMd5;
	}

	public List<String> getReqParametersKey() {
		return reqParametersKey;
	}

	public void setReqParametersKey(List<String> reqParametersKey) {
		this.reqParametersKey = reqParametersKey;
	}
}
