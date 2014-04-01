package com.cintel.frame.auth.menu;

import java.io.Serializable;

import com.cintel.frame.auth.func.FuncItemImpl;
import com.cintel.frame.util.MyAntPathMatcher;

public class FuncItemForSpring extends FuncItemImpl implements Serializable {
    private static final long serialVersionUID = -1801215863187383835L;
    
    private String authRoles;
    
    private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAuthRoles() {
		return authRoles;
	}

	public void setAuthRoles(String authRoles) {
		this.authRoles = authRoles;
		initAuthRolesArr();
	}
	
	private String[] authRolesArr;
	
	private void initAuthRolesArr() {
		this.authRolesArr = authRoles.split(",");
		for(String authRoleInfo:authRolesArr) {
			authRoleInfo = authRoleInfo.trim();
		}
	}
	
	/**
	 * 
	 * @method: isGranted
	 * @return: boolean
	 * @author: WangShuDa
	 * @param roleName
	 * @return
	 */
	public boolean doAuthenticate(String roleName) {
        boolean isMatch = false;
        MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
		for(String grantedAuthRoleName:authRolesArr) {
            isMatch = pathMatcher.matchIgnoreCase(grantedAuthRoleName, roleName);
			if(isMatch) {
				return true;
			}
		}
		return false;
	}
}
