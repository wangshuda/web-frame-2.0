package com.cintel.frame.auth.login;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.auth.user.RoleContext;
import com.cintel.frame.auth.user.UserContext;
import com.cintel.frame.util.StringUtils;
import com.cintel.frame.webui.IDomainService;

/**
 * 
 * @file    : AuthServiceImpl.java
 * @author  : WangShuDa
 * @date    : 2009-4-30
 * @corp    : CINtel
 * @version : 1.0
 * @desc    :
 *  2009/06/14 wangshuda replace the roleName with the role context.
 */
public class AuthServiceImpl implements AuthService {
	protected static Log log = LogFactory.getLog(AuthServiceImpl.class);
	
	private IDomainService userInfoService = null;
	
	private IDomainService roleInfoService = null;
	
	private RoleContext roleContext = null;
	
	private Map<String, RoleContext> roleCtxMap;

	private boolean loadRoleIdInUserInfoService = false;

	public UserContext getUserContext(Map<String, Object> reqParametersMap) {
		return (UserContext)this.getUserInfoService().get("getAuthUserContext", reqParametersMap);
	}
	
	protected RoleContext loadRoleCtxInUserInfo(Map<String, Object> reqParametersMap) {
		String roleId = (String)this.getUserInfoService().get("getAuthUserRoleId", reqParametersMap);
		//
		return (RoleContext)this.getRoleInfoService().get("loadRoleContextWithRoleId", roleId);
	}
	//
	public RoleContext[] getRoleContextArr(Map<String, Object> reqParametersMap) {
		if(loadRoleIdInUserInfoService) {
			roleContext = this.loadRoleCtxInUserInfo(reqParametersMap);
		}
		else {
			if(roleContext == null) {
				if(roleCtxMap != null) {
					String roleName = (String)this.getUserInfoService().get("getAuthUserRoleName", reqParametersMap);
					roleContext = roleCtxMap.get(roleName);
				}
				else {
					roleContext = (RoleContext)this.getUserInfoService().get("getAuthUserRoleContext", reqParametersMap);
				}
			}
		}

		//
		return new RoleContext[]{roleContext};
	}
	
	// -------------- get/set methods --------------
	public IDomainService getUserInfoService() {
		return userInfoService;
	}

	public void setUserInfoService(IDomainService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public RoleContext getRoleContext() {
		return roleContext;
	}
	
	public void setRoleContext(RoleContext roleContext) {
		this.roleContext = roleContext;
	}

	public Map<String, RoleContext> getRoleCtxMap() {
		return roleCtxMap;
	}

	public void setRoleCtxMap(Map<String, RoleContext> roleCtxMap) {
		this.roleCtxMap = roleCtxMap;
	}
	
	@Deprecated
	private List<String> constRoleNameList = null;
	
	@Deprecated
	public String[] getRoleNameArr(Map<String, Object> reqParametersMap) {
		String[] roleNameArr = null;
		//
		if(constRoleNameList != null && constRoleNameList.size() > 0) {
			roleNameArr = (String[])constRoleNameList.toArray(new String[constRoleNameList.size()]);
		}
		else {
			//
			String roleName = (String)this.getUserInfoService().get("getAuthUserRoleName", reqParametersMap);
			if(StringUtils.hasText(roleName)) {
				roleNameArr = roleName.split(",");
			}
		}
		return roleNameArr;
	}
	
	public List<String> getConstRoleNameList() {
		return constRoleNameList;
	}

	public void setConstRoleNameList(List<String> constRoleNameList) {
		this.constRoleNameList = constRoleNameList;
	}

	public boolean isLoadRoleIdInUserInfoService() {
		return loadRoleIdInUserInfoService;
	}

	public void setLoadRoleIdInUserInfoService(boolean loadRoleIdInUserInfoService) {
		this.loadRoleIdInUserInfoService = loadRoleIdInUserInfoService;
	}

	public IDomainService getRoleInfoService() {
		return roleInfoService;
	}

	public void setRoleInfoService(IDomainService roleInfoService) {
		this.roleInfoService = roleInfoService;
	}
}
