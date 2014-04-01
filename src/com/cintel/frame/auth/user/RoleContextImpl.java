package com.cintel.frame.auth.user;

import java.io.Serializable;

/**
 * @version : $Id: RoleContextImpl.java 32332 2011-12-23 09:07:47Z anlina $
 */
public class RoleContextImpl implements Serializable, RoleContext {
    private static final long serialVersionUID = 6888500723514707488L;
    //
    /**
     * using it to load the functions info for the role.
     */
    private String roleId = null;
    
    /**
     * using it to check granted in auth tag.
     */
    private String key;

	private String title;
    
    private String englishTitle;

	private String loginUrl;

	private String logoutUrl;

	private String welcomeUrl = "about:blank";
	
	private String description;

	private int roleLevel = 0;
	
	private int roleGroup = 0;


	public String getRoleId() {
		if(roleId == null) {
			return key;
		}
		else {
			return roleId;
		}
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }
}
