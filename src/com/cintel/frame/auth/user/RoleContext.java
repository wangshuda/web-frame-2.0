package com.cintel.frame.auth.user;

import java.io.Serializable;

public interface RoleContext extends Serializable {

	public String getRoleId();

	public String getTitle();

	public int getRoleLevel();

	public int getRoleGroup();

	public String getKey();

	public String getLoginUrl();

	public String getLogoutUrl();

	public String getWelcomeUrl();

	public String getDescription();

	public void setRoleId(String roleId);

	public void setKey(String key);
}
