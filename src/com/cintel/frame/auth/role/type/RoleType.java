package com.cintel.frame.auth.role.type;

import com.cintel.frame.webui.IDomainVo;

/**
 * @file : $Id: RoleType.java 15613 2010-01-12 10:20:51Z wangshuda $
 * @corp : CINtel
 * @version : 1.0
 */
public class RoleType implements IDomainVo {

	private String roleKey;

	private String roleTitle;

	private String loginUrl;

	private String logoutUrl;

	private String welcomeUrl;

	private int roleLevel;

	private int roleGroup;

	private String description;

	public String getOid() {
		return String.valueOf(roleKey);
	}

	public void setOid(String oid) {
		if (oid == null) {
			return;
		}
		String oidStr[] = oid.split(",");
		roleKey = oidStr[0];
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getWelcomeUrl() {
		return welcomeUrl;
	}

	public void setWelcomeUrl(String welcomeUrl) {
		this.welcomeUrl = welcomeUrl;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public int getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(int roleGroup) {
		this.roleGroup = roleGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}