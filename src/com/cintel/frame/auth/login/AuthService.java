package com.cintel.frame.auth.login;

import java.util.Map;

import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.auth.user.UserContext;

public interface AuthService {

	public RoleContext[] getRoleContextArr(Map<String, Object> reqParametersMap);
	
	@Deprecated
	/**
	 * replace the getRoleNameArr with getRoleContextArr
	 */
	public String[] getRoleNameArr(Map<String, Object> reqParametersMap);
	
	public UserContext getUserContext(Map<String, Object> reqParametersMap);
}
