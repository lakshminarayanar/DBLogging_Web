package com.hlb.dblogging.web.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.security.users.service.UsersService;

@Component
@ViewScoped
public class LoginAuthenticationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsersService usersService;
	private HashSet<String> accessRightsSet = new HashSet<String>();
	private String username;
	private String password;
	private Boolean wrongPassword;
	
	
	
	public LoginAuthenticationBean() {
		ApplLogger.getLogger().info("@@@@@@@@@@@ LoginAuthenticationBean object created... @@@@@@@@@");
	}
	public UsersService getUsersService() {
		return usersService;
	}
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	public Boolean getWrongPassword() {
		return wrongPassword;
	}
	public void setWrongPassword(Boolean wrongPassword) {
		this.wrongPassword = wrongPassword;
	}
	public String doLogin() throws ServletException, IOException{
		
		ApplLogger.getLogger().info("Authenticate here...!!!");
		ApplLogger.getLogger().info("Given username : "+username+" and password : "+password);
		// TODO: Write the code to check user in database and then get list of Authorities.
		if(!checkUserExistInDatabase()){
			ApplLogger.getLogger().info("username not found in the application database : "+username);
			FacesMessage msg = new FacesMessage("username","username not found in database"); 
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("login.jsf", msg); 
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			return "/login.jsf?faces-redirect=true";
		}
		populateAccessRightsSet();
		
		if(!"admin".equalsIgnoreCase(username)){
		ApplLogger.getLogger().info("Logged in user is Authenticating .......");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		RequestDispatcher requestDispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
		requestDispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null){
			ApplLogger.getLogger().info("Authentication is successful..");
		}else{
			ApplLogger.getLogger().info("Authentication is failed for user :"+username);
			wrongPassword=true;
			/*FacesMessage msg = new FacesMessage("password is invalid","username and password does not match!!!"); 
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("relogin.jsf", msg); 
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);*/
			//return "/login.jsf?faces-redirect=true";
		}
		}else{
			// TODO: Write code for checking password and if wrong, return to login page else get list of Authorities.
			ApplLogger.getLogger().info("Logged in user is Admin...");
			return "/pages/admin/adminhomepage.jsf?faces-redirect=true";
		}
		
		
		FacesContext.getCurrentInstance().responseComplete();
		return null;
		
	}
	
	private boolean checkUserExistInDatabase() {
		try{
		return usersService.findUserExistInApplication(username);
		}catch(Exception e){
			ApplLogger.getLogger().error("Caught Database Exception while checking username in Database for user :"+username,e);
		}
		return false;
	}
	public String doLogout(){
		//TODO Write logic to log out 
		SecurityContextHolder.clearContext();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		ApplLogger.getLogger().info("Logged out successsfuly...!");
		username=null;
		password=null;
		return "/login.jsf?faces-redirect=true"  ;
	 }
	
	private void populateAccessRightsSet() {
		try {
			accessRightsSet = usersService.getAccessRightsMapForUser(username);
		} catch(Exception e) {
			ApplLogger.getLogger().error("Error while populating access rights for user :"+username,e);
		}
	}
	
	public Boolean hasAccess(String key) {
		if (key != null && accessRightsSet.contains(key)) {
				return true;
		}		
		return false;
	}
	
	public void hasPageAccess(String key) {
		if (key != null && !accessRightsSet.contains(key)) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.jsf");
				} catch(IOException e) {
					e.printStackTrace();
				}
		}		
	}
	
	
}