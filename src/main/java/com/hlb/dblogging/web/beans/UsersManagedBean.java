package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.jpa.service.ConfigurationPropertiesService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.UsersDataModel;
import com.hlb.dblogging.security.users.service.UsersService;

@Component
@RequestScoped
public class UsersManagedBean implements Serializable {

/**
	 * 
	 */
	private String txt1;
	private String txt2;
	private static final long serialVersionUID = 1L;
	List<String> users = new ArrayList<String>();
	private String changedLogLevel;
	private String currentLogLevel;

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private AuditMasterService auditMasterService;
	
	@Autowired
	private ConfigurationPropertiesService	configurationPropertiesService;

	private List<Users> userslists;
	private UsersDataModel userdatamodel;
	private Boolean insertDeleted = false;
	
	
	public String getCurrentLogLevel() {
		return currentLogLevel;
	}

	public void setCurrentLogLevel(String currentLogLevel) {
		this.currentLogLevel = currentLogLevel;
	}

	public String getChangedLogLevel() {
		return changedLogLevel;
	}

	public void setChangedLogLevel(String changedLogLevel) {
		this.changedLogLevel = changedLogLevel;
	}

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public String getTxt2() {
		return txt2;
	}

	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}

	public AuditMasterService getAuditMasterService() {
		return auditMasterService;
	}

	public void setAuditMasterService(AuditMasterService auditMasterService) {
		this.auditMasterService = auditMasterService;
	}
	

	public List<Users> getUserslists() {
		return userslists;
	}

	public void setUserslists(List<Users> userslists) {
		this.userslists = userslists;
	}

	public UsersService getUsersService() {
		return usersService;
	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public List<String> getUserList(String username){
        List<String> results = new ArrayList<String>();
		results=usersService.findUsersList(username);
		return results;
	}
	
	public List<Users> getAllUsersList(){
		return usersService.findAllUsers();

	}

	public UsersDataModel getUserdatamodel() {
		
		return new UsersDataModel(getAllUsersList());
	}

	public void setUserdatamodel(UsersDataModel userdatamodel) {
		this.userdatamodel = userdatamodel;
	}
	
	public void createUser(){
		try{
			if(StringUtils.trimToEmpty(txt1).isEmpty()){
				FacesMessage msg = new FacesMessage("ERROR : Username can't be empty spaces, use windows active directory username");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return;
			}
			Users user=new Users();
			user.setUsername(txt1);
			usersService.create(user);
			FacesMessage msg = new FacesMessage("SUCCESS : User is added successfully to System");  
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        txt1=null;
		}catch(Exception e){
			if(e instanceof DataIntegrityViolationException){
				ApplLogger.getLogger().error("Error while adding new user to System",e);
				FacesMessage msg = new FacesMessage("ERROR : Username existed in the System");  
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
			}else{
			ApplLogger.getLogger().error("Error while adding new user to System",e);
			FacesMessage msg = new FacesMessage("ERROR : Not able to add user currently to System");  
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

 /*  public List<String> getUserList(String query) {
        List<String> results = new ArrayList<String>();
        for(int i = 0; i < 3; i++) {
            results.add(query + i);
        }
         
        return results;
    }
	*/
	
	

	public void populateValues(){
		currentLogLevel = configurationPropertiesService.getLogLevelForMessageSaving();
	}

	public ConfigurationPropertiesService getConfigurationPropertiesService() {
		return configurationPropertiesService;
	}

	public void setConfigurationPropertiesService(
			ConfigurationPropertiesService configurationPropertiesService) {
		this.configurationPropertiesService = configurationPropertiesService;
	}
	
	public void updateLogLevel(){
		configurationPropertiesService.updateLoglevl(changedLogLevel);
		currentLogLevel = changedLogLevel;
		changedLogLevel="";
	}
	
}


