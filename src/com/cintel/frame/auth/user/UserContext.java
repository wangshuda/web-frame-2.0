package com.cintel.frame.auth.user;

import java.io.Serializable;

public interface UserContext extends Serializable {
	public String getUserPassword();
    
    public String getAreaCode();
    
}
