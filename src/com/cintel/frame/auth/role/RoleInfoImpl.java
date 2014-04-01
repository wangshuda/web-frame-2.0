package com.cintel.frame.auth.role;

import com.cintel.frame.auth.role.type.RoleType;
import com.cintel.frame.webui.IDomainVo;

public class RoleInfoImpl extends com.cintel.frame.auth.user.GrantedAuthorityImpl implements IDomainVo, com.cintel.frame.auth.user.GrantedAuthority {
	private static final long serialVersionUID = 1579127869668127504L;
	//
	private String roleId;

	private String roleTitle;
	
	private String roleTypeKey;
	
	private String description;
	
	private RoleType roleType;
	
	public String getOid() {
		return roleId;
	}

	public void setOid(String oid) {
		if (oid == null) {
			return;
		}
		String oidStr[] = oid.split(",");
		roleId = oidStr[0];
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}

	public String getRoleTypeKey() {
		return roleTypeKey;
	}

	public void setRoleTypeKey(String roleTypeKey) {
		this.roleTypeKey = roleTypeKey;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}