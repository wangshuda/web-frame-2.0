package com.cintel.frame.auth.login;

import java.util.Map;

import com.cintel.frame.auth.user.UserContext;

/**
 * @file : $Id: SysAuthService.java 23795 2010-12-23 09:34:00Z anlina $
 * @corp : CINtel
 * @version: 1.0
 */
public class SysAuthService extends AuthServiceImpl implements AuthService {
	
	private String savedSystemAdminName = "admin";
	
	private String savedSystemAdminPwd = "111111";
	
	@Override
	public UserContext getUserContext(Map<String, Object> reqParametersMap) {
        String userName = (String)reqParametersMap.get(AuthConstants.USER_NAME);
        //
        if(this.savedSystemAdminName.equals(userName)) {
            return new DefaultUserContext(savedSystemAdminName, savedSystemAdminPwd);
        }
        else {
            return null;
        }
	}
	
	public String getSavedSystemAdminName() {
		return savedSystemAdminName;
	}

	public void setSavedSystemAdminName(String savedSystemAdminName) {
		this.savedSystemAdminName = savedSystemAdminName;
	}

	public String getSavedSystemAdminPwd() {
		return savedSystemAdminPwd;
	}

	public void setSavedSystemAdminPwd(String savedSystemAdminPwd) {
		this.savedSystemAdminPwd = savedSystemAdminPwd;
	}
}
