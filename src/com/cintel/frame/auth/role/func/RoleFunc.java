package com.cintel.frame.auth.role.func;

import com.cintel.frame.webui.IDomainVo;

public class RoleFunc implements IDomainVo {
	private String id;

	private String roleId;

	private String funcId;

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getOid() {
		return id;
	}

	public void setOid(String oid) {
		this.id = oid;
	}
}