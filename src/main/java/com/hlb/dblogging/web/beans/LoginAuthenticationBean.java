package com.hlb.dblogging.web.beans;

import java.io.Serializable;

import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.log.utility.ApplLogger;

@Component
@SessionScoped
public class LoginAuthenticationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String doLogin(){
		ApplLogger.getLogger().info("Authenticate here...!!!");
		ApplLogger.getLogger().info("Given username : "+userName+" and password : "+password);
		if(userName.equalsIgnoreCase("admin")){
			ApplLogger.getLogger().info("Redirecting to /pages/adminhomepage.xhtml");
			return "/pages/adminhomepage.xhtml";
		}
		else
			return "/pages/homePage.xhtml";
	}
	
	public String doLogout(){
		//TODO Write logic to log out 
		SecurityContextHolder.clearContext();
		ApplLogger.getLogger().info("Logged out successsfuly...!");
		return "/login.jsf?faces-redirect=true";
	}
}