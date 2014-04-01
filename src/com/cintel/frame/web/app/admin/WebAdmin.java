package com.cintel.frame.web.app.admin;

import com.cintel.frame.auth.user.UserContext;
import com.cintel.frame.webui.IDomainVo;

public class WebAdmin implements IDomainVo, UserContext {
    private static final long serialVersionUID = 3464101359000974879L;

    private String id;

	private String loginName;

	private String roleId;

	private String authAreaCode;

	private String chineseName;

	private String password;

	private String status = "A";

	private String telephone;

	private String email;

	private String description;

	private String createDateTime;

	private String lastLoignDateTime;

	private String lastLoignIp;

	public String getOid() {
		return id;
	}

	public void setOid(String oid) {
		if (oid == null) {
			return;
		}
		String oidStr[] = oid.split(",");
		id = oidStr[0];
	}
    
	// implement the UserContext method.
	public String getUserPassword() {
		return this.password;
	}
	
	public String getAreaCode() {
		return this.authAreaCode;
	}
	
	// ----------------- get/set methods -----------
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getLastLoignDateTime() {
		return lastLoignDateTime;
	}

	public void setLastLoignDateTime(String lastLoignDateTime) {
		this.lastLoignDateTime = lastLoignDateTime;
	}

	public String getLastLoignIp() {
		return lastLoignIp;
	}

	public void setLastLoignIp(String lastLoignIp) {
		this.lastLoignIp = lastLoignIp;
	}

	public String getAuthAreaCode() {
		return authAreaCode;
	}

	public void setAuthAreaCode(String authAreaCode) {
		this.authAreaCode = authAreaCode;
	}
}