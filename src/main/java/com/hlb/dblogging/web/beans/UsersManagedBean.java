package com.hlb.dblogging.web.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.app.context.FacesUtil;
import com.hlb.dblogging.jpa.model.ConfigurationProperties;
import com.hlb.dblogging.jpa.model.Role;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.jpa.service.ConfigurationPropertiesService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.UsersDataModel;
import com.hlb.dblogging.security.users.service.RoleService;
import com.hlb.dblogging.security.users.service.UsersService;
import com.hlb.dblogging.user.audit.logging.AuditTrail;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailActivity;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailLevel;
import com.hlb.dblogging.xml.utility.XSLTransformer;

@Component
@ViewScoped
public class UsersManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String changedLogLevel;
	private String currentLogLevel;
	private String existingXSLT;
	private UploadedFile newXSLTAttachment;

	List<String> users = new ArrayList<String>();
	private Users selectedUser = new Users();
	private Users newUser = new Users();
	private List<Users> usersList;
	private UsersDataModel usersDataModel;
	private Users loggedInUser;
	private Set<Role> roleSet = null;
	private Set<Role> assignedRoleSet = new HashSet<Role>();
	private Set<Role> unassignedRoleSet = new HashSet<Role>();
	private DualListModel<Role> dualRoleList = new DualListModel<Role>();
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
	
	
	@PostConstruct
	public void init() {
		roleSet = roleService.findAllInSet();
	}

	public Users getLoggedInUser() {
		return loggedInUser;
	}


	public void setLoggedInUser(Users loggedInUser) {
		this.loggedInUser = loggedInUser;
	}


	public Users getNewUser() {
		return newUser;
	}

	public List<String> getUsers() {
		return users;
	}


	public void setUsers(List<String> users) {
		this.users = users;
	}


	public Set<Role> getRoleSet() {
		return roleSet;
	}


	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}


	public Set<Role> getAssignedRoleSet() {
		return assignedRoleSet;
	}


	public void setAssignedRoleSet(Set<Role> assignedRoleSet) {
		this.assignedRoleSet = assignedRoleSet;
	}


	public Set<Role> getUnassignedRoleSet() {
		return unassignedRoleSet;
	}


	public void setUnassignedRoleSet(Set<Role> unassignedRoleSet) {
		this.unassignedRoleSet = unassignedRoleSet;
	}


	public DualListModel<Role> getDualRoleList() {
		return dualRoleList;
	}


	public void setDualRoleList(DualListModel<Role> dualRoleList) {
		this.dualRoleList = dualRoleList;
	}


	public void setNewUser(Users newUser) {
		this.newUser = newUser;
	}

	public AuditTrail getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}

	public Users getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Users selectedUser) {
		this.selectedUser = selectedUser;
	}
	public List<Users> getUsersListFromDatabase(){
		
		if(usersList ==null || usersList.isEmpty() || dmlOperationPerformed){
			usersList =	usersService.findAllUsers();
		ApplLogger.getLogger().info("Users list of size from database is : "+usersList.size());
		}
		return usersList;
	}

	public UsersDataModel getUsersDataModel() {
		if(usersDataModel==null || dmlOperationPerformed){
			ApplLogger.getLogger().info("Entering to get the datamodel..");
			usersDataModel = new UsersDataModel(getUsersListFromDatabase());
		}
		return usersDataModel;
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

	public List<Users> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<Users> usersList) {
		this.usersList = usersList;
	}

	public UploadedFile getNewXSLTAttachment() {
		return newXSLTAttachment;
	}

	public void setNewXSLTAttachment(UploadedFile newXSLTAttachment) {
		this.newXSLTAttachment = newXSLTAttachment;
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

	public void doCreateUser() {

		try {
			System.out.println("Getting logged in user from session..");
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			newUser.setDeleted(false);
			newUser.setCreatedBy(getLoggedInUser().getUsername());
			newUser.setCreationTime(new Date());
			List<Role> selectedRoleList = dualRoleList.getTarget();
			HashSet<Role> selectedRoleSet = new HashSet<Role>();
			selectedRoleSet.addAll(selectedRoleList);
			newUser.setUserRoles(selectedRoleSet);
			usersService.create(newUser);
			auditTrail.log(SystemAuditTrailActivity.CREATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has created a department");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("SUCCESS : User is added successfully to System"));
			newUser = new Users();
			dmlOperationPerformed = Boolean.TRUE;
		} catch(Exception e){ 
			if(e instanceof DataIntegrityViolationException){
			  ApplLogger.getLogger().error("Error while adding new user to System",e);
			  FacesMessage msg = new  FacesMessage("ERROR : Username existed in the System");
			  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			  FacesContext.getCurrentInstance().addMessage(null, msg); 
			  }else{
			  ApplLogger.getLogger().error("Error while adding new user to System",e);
			  FacesMessage msg = new  FacesMessage("ERROR : Not able to add user currently to System");
			  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			  FacesContext.getCurrentInstance().addMessage(null, msg); 
			  }
		}
	}
	public void onRowSelect(SelectEvent event) {
		setSelectedUser((Users) event.getObject());
		
	}

	public void doInitializeForm() {
		newUser = new Users();
		// Get the groups from the database
		//rolesList = roleService.findAll();
	}

	/*
	 * public void createUser(){ try{
	 * if(StringUtils.trimToEmpty(txt1).isEmpty()){ FacesMessage msg = new
	 * FacesMessage(
	 * "ERROR : Username can't be empty spaces, use windows active directory username"
	 * ); msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	 * FacesContext.getCurrentInstance().addMessage(null, msg); return; } Users
	 * user=new Users(); user.setUsername(txt1); //user.setEnabled(true);
	 * user.setCreatedBy("Admin"); user.setCreationTime(new Date());
	 * usersService.create(user); FacesMessage msg = new
	 * FacesMessage("SUCCESS : User is added successfully to System");
	 * msg.setSeverity(FacesMessage.SEVERITY_INFO);
	 * FacesContext.getCurrentInstance().addMessage(null, msg); txt1=null;
	 * }catch(Exception e){ if(e instanceof DataIntegrityViolationException){
	 * ApplLogger.getLogger().error("Error while adding new user to System",e);
	 * FacesMessage msg = new
	 * FacesMessage("ERROR : Username existed in the System");
	 * msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	 * FacesContext.getCurrentInstance().addMessage(null, msg); }else{
	 * ApplLogger.getLogger().error("Error while adding new user to System",e);
	 * FacesMessage msg = new
	 * FacesMessage("ERROR : Not able to add user currently to System");
	 * msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	 * FacesContext.getCurrentInstance().addMessage(null, msg); } } }
	 */
	/*
	 * public List<String> getUserList(String query) { List<String> results =
	 * new ArrayList<String>(); for(int i = 0; i < 3; i++) { results.add(query +
	 * i); }
	 * 
	 * return results; }
	 */

	public void populateValues() {
		currentLogLevel = configurationPropertiesService
				.getLogLevelForMessageSaving();
	}

	public void upload() throws IOException {
		if (newXSLTAttachment != null) {
			String fileName = newXSLTAttachment.getFileName();
			String format = fileName.substring(fileName.length() - 3);
			if (newXSLTAttachment != null && (format.equalsIgnoreCase("xsl"))) {
				try {
					existingXSLT = new String(
							IOUtils.toByteArray(newXSLTAttachment
									.getInputstream()));
					ApplLogger.getLogger().info(
							"Input Data XSLT in String form is : "
									+ existingXSLT);
					configurationPropertiesService
							.updateNewXSLTFile(existingXSLT);
					XSLTransformer.xslTranformerStream = existingXSLT;
					FacesMessage msg = new FacesMessage(
							"Info : New XSLT uploaded successfully, Changes will be effected for every 30 minutes");
					msg.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} catch (Exception e) {
					FacesMessage msg = new FacesMessage(
							"Error : Unbale to upload now, Please try later..");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} else {
				FacesMessage msg = new FacesMessage(
						"Error : Only XSL format allowed to upload..");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public ConfigurationPropertiesService getConfigurationPropertiesService() {
		return configurationPropertiesService;
	}

	public void setConfigurationPropertiesService(
			ConfigurationPropertiesService configurationPropertiesService) {
		this.configurationPropertiesService = configurationPropertiesService;
	}

	public void updateLogLevel() {
		configurationPropertiesService.updateLoglevl(changedLogLevel);
		currentLogLevel = changedLogLevel;
		changedLogLevel = "";
	}

	public void populateExistingXSLT() {
		ConfigurationProperties configuration = configurationPropertiesService
				.getApplicationConfiguration();
		XSLTransformer.xslTranformerStream = configuration.getXslTransformer();
		existingXSLT = XSLTransformer.xslTranformerStream;
	}

	public void deleteUser() {
		System.out.println("Username is going to be deleted is : "
				+ selectedUser.getUsername());
		if (!"admin".equalsIgnoreCase(selectedUser.getUsername())
				&& usersService.deleteUser(selectedUser.getUsername()) > 0)
			System.out.println("Deleted successfully");
		else
			System.out.println("Could not delete. Try later..");
	}
	
	public void populatePickList(){
		this.unassignedRoleSet.addAll(this.roleSet);
		List<Role> unassignedRoleList = new ArrayList<Role>();
		unassignedRoleList.addAll(unassignedRoleSet);
		List<Role> assignedRoleList = new ArrayList<Role>();
		this.dualRoleList = new DualListModel<Role>(unassignedRoleList, assignedRoleList);	
		
	}
	
	
	

}
