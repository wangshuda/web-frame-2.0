package com.cintel.frame.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Title: MyAuthenticator
 * 
 * @version 1.0
 * @author  wangshuda
 * @date   2006-11-18
 */
public class MyAuthenticator extends Authenticator{   
	  private   String   userName;   
	  private   String   password; 
	  
	  public   MyAuthenticator(String user,String pass){   
	      userName=user;   
	      password=pass;   
	  }
	  
	  public   PasswordAuthentication   getPasswordAuthentication(){   
	      return   new   PasswordAuthentication(userName,password);   
	  }   
}
