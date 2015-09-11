package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.ConfigurationProperties;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.jpa.service.ConfigurationPropertiesService;
import com.hlb.dblogging.model.UsersDataModel;
import com.hlb.dblogging.security.users.service.RoleService;
import com.hlb.dblogging.security.users.service.UsersService;
import com.hlb.dblogging.user.audit.logging.AuditTrail;
import com.hlb.dblogging.xml.utility.XSLTransformer;

@Component
@ViewScoped
public class SettingsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String changedLogLevel;
	private String currentLogLevel;
	private String existingXSLT;
	
	private Users loggedInUser;
	private Boolean applyXsltAtSave;
	private Boolean applyXsltAtView;
	
	
	private Boolean dmlOperationPerformed=Boolean.FALSE;
	

	@Autowired
	private UsersService usersService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AuditMasterService auditMasterService;

	@Autowired
	private ConfigurationPropertiesService configurationPropertiesService;

	@Autowired
	private AuditTrail auditTrail;
	
	public Boolean getApplyXsltAtSave() {
		return applyXsltAtSave;
	}


	public void setApplyXsltAtSave(Boolean applyXsltAtSave) {
		this.applyXsltAtSave = applyXsltAtSave;
	}


	public Boolean getApplyXsltAtView() {
		return applyXsltAtView;
	}


	public void setApplyXsltAtView(Boolean applyXsltAtView) {
		this.applyXsltAtView = applyXsltAtView;
	}


	public Users getLoggedInUser() {
		return loggedInUser;
	}


	public void setLoggedInUser(Users loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	
	public AuditTrail getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
	
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

	public String getExistingXSLT() {
		return existingXSLT;
	}

	public void setExistingXSLT(String existingXSLT) {
		this.existingXSLT = existingXSLT;
	}

	public AuditMasterService getAuditMasterService() {
		return auditMasterService;
	}

	public void setAuditMasterService(AuditMasterService auditMasterService) {
		this.auditMasterService = auditMasterService;
	}

	
	public UsersService getUsersService() {
		return usersService;
	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public List<String> getUserList(String username) {
		List<String> results = new ArrayList<String>();
		results = usersService.findUsersList(username);
		return results;
	}

	public List<Users> getAllUsersList() {
		return usersService.findAllUsers();

	}

	public UsersDataModel getUserdatamodel() {

		return new UsersDataModel(getAllUsersList());
	}

	
	
	public void populateValues() {
		//currentLogLevel = configurationPropertiesService.getLogLevelForMessageSaving();
		applyXsltAtSave = XSLTransformer.isSaveEnabled;
		applyXsltAtView = XSLTransformer.isViewEnabled;
	}

	public void populateLogValues() {
		currentLogLevel = configurationPropertiesService.getLogLevelForMessageSaving();
	}
	
	public ConfigurationPropertiesService getConfigurationPropertiesService() {
		return configurationPropertiesService;
	}

	public void setConfigurationPropertiesService(
			ConfigurationPropertiesService configurationPropertiesService) {
		this.configurationPropertiesService = configurationPropertiesService;
	}

	public void doUpdateSettings(){
		Boolean status = configurationPropertiesService.updateXSLTSettings(applyXsltAtView, applyXsltAtSave);
		
		if(status){
			XSLTransformer.isViewEnabled=applyXsltAtView;
			XSLTransformer.isSaveEnabled=applyXsltAtSave;
			FacesMessage msg = new FacesMessage("INFO : Global XSLT changes updated successfully, Will be effective at next interval");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			FacesMessage msg = new FacesMessage("ERROR : Error while updating settings, please try later");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
	}
	
	
	
	public void updateLogLevel() {
		try{
		configurationPropertiesService.updateLoglevl(changedLogLevel);
		currentLogLevel = changedLogLevel;
		changedLogLevel = "";
		FacesMessage msg = new FacesMessage("INFO : Log level changed successfully, Will be effective at next interval");
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		}catch(Exception e){
			FacesMessage msg = new FacesMessage("ERROR : Can't change Loglevel presently");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void populateExistingXSLT() {
		ConfigurationProperties configuration = configurationPropertiesService.getApplicationConfiguration();
		XSLTransformer.defaultViewXslStream = configuration.getViewXslTransformer();
		existingXSLT = XSLTransformer.defaultViewXslStream;
	}

}
