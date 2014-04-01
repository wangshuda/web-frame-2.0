package com.cintel.frame.auth.login;

import com.cintel.frame.auth.user.UserDetails;

public class LoginResponse {
	private UserDetails userDetails;

	private String forwardKey;

	public String getForwardKey() {
		return forwardKey;
	}

	public void setForwardKey(String forwardKey) {
		this.forwardKey = forwardKey;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
}
