package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.app.context.FacesUtil;
import com.hlb.dblogging.exception.utility.BSLException;
import com.hlb.dblogging.jpa.model.AccessRights;
import com.hlb.dblogging.jpa.model.Role;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.model.RoleDataModel;
import com.hlb.dblogging.security.users.service.AccessRightsService;
import com.hlb.dblogging.security.users.service.RoleService;
import com.hlb.dblogging.user.audit.logging.AuditTrail;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailActivity;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailLevel;


@Component
@RequestScoped
public class RoleManagedBean implements Serializable {
private static final long serialVersionUID = 1L;
	 
	@Autowired
	RoleService roleService;	

	@Autowired
	private AccessRightsService accessRightsService;	
	
	private List<Role> roleList;
	private RoleDataModel roleDataModel;
	private Role newRole = new Role();
	private Role selectedRole = new Role();
	private boolean insertDelete = false;
	private List<Role> searchRole;
	private Set<AccessRights> accessRightsSet = null;
	private Set<AccessRights> assignedAccessRightsSet = new HashSet<AccessRights>();
	private Set<AccessRights> unassignedAccessRightsSet = new HashSet<AccessRights>();
	private DualListModel<AccessRights> dualAccessRightsList = new DualListModel<AccessRights>();
	
	
	private String searchRoleName = "";
	
	private Users loggedInUser;
	
	
	@Autowired
	private AuditTrail auditTrail;	
	
	
/*	@PostConstruct
	public void init(){
		accessRightsSet = accessRightsService.findAllInSet();			
	}*/	
	
	public RoleService getRoleService() {
		return roleService;
	}
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public List<Role> getRoleList() {
		
		if(roleList == null || insertDelete == true) {			
			roleList = roleService.findAll();
		}
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	public RoleDataModel getRoleDataModel() {
		/*
		if(roleDataModel == null || insertDelete == true) {
			
			roleDataModel = new RoleDataModel(getRoleList());
		}
		
		return roleDataModel;*/
		
		return new RoleDataModel(getRoleList());
	}
	
	public void setRoleDataModel(RoleDataModel roleDataModel) {
		this.roleDataModel = roleDataModel;
	}
	
	public Role getNewRole() {
		return newRole;
	}
	
	public void setNewRole(Role newRole) {
		this.newRole = newRole;
	}
	
	
	public void doCreateRole() {		
	 try{	
		 
		 loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
		 
		newRole.setDeleted(false);
		newRole.setCreatedBy(getLoggedInUser().getUsername());
		newRole.setCreationTime(new java.util.Date());
		List<AccessRights> selectedAccessRightsList = dualAccessRightsList.getTarget();
		HashSet<AccessRights> selectedAccessRightsSet = new HashSet<AccessRights>();
		selectedAccessRightsSet.addAll(selectedAccessRightsList);
		newRole.setAccessRights(selectedAccessRightsSet);
		getRoleService().create(newRole);
		setInsertDelete(true);	
		
		auditTrail.log(SystemAuditTrailActivity.CREATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has created new Group " + newRole.getRole());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Role  " + newRole.getRole()+ " created successfully"));		
		newRole = new Role();
	 }catch(BSLException e){
		//FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
	//	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      //  FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
}
	
	public Role getSelectedRole() {
		return selectedRole;
	}
	
/*	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
	*/
	
	
	public void setSelectedRole(Role selectedRole) {		
		this.selectedRole = selectedRole;
		this.assignedAccessRightsSet = this.selectedRole.getAccessRights();
		if(accessRightsSet==null)
			accessRightsSet = accessRightsService.findAllInSet();
		this.unassignedAccessRightsSet.addAll(this.accessRightsSet);
		this.unassignedAccessRightsSet.removeAll(this.assignedAccessRightsSet);
		List<AccessRights> unassignedAccessRightsList = new ArrayList<AccessRights>();
		unassignedAccessRightsList.addAll(unassignedAccessRightsSet);
		List<AccessRights> assignedAccessRightsList = new ArrayList<AccessRights>();
		assignedAccessRightsList.addAll(assignedAccessRightsSet);
		this.dualAccessRightsList = new DualListModel<AccessRights>(unassignedAccessRightsList, assignedAccessRightsList);		
	}
	
	
	public void doUpdateRole() {
		try {
			
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			
			System.out.println("New Role:" + selectedRole.getRole());
			System.out.println("Id:" + selectedRole.getId());
			System.out.println("Description:" + selectedRole.getDescription());
			selectedRole.setLastModifiedBy(getLoggedInUser().getUsername());
			List<AccessRights> selectedAccessRightsList = dualAccessRightsList.getTarget();
			HashSet<AccessRights> selectedAccessRightsSet = new HashSet<AccessRights>();
			selectedAccessRightsSet.addAll(selectedAccessRightsList);
			selectedRole.setAccessRights(selectedAccessRightsSet);
			getRoleService().update(selectedRole);			
			setInsertDelete(true);
			auditTrail.log(SystemAuditTrailActivity.UPDATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has update Group " + selectedRole.getRole());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Role  " + selectedRole.getRole()+ " updated successfully"));
		} catch (BSLException e){
		//	FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
		//	msg.setSeverity(FacesMessage.SEVERITY_ERROR);  
		//	FacesContext.getCurrentInstance().addMessage(null, msg);  
		}		
	}
	
	public void populatePickList(){
		accessRightsSet = accessRightsService.findAllInSet();	
		this.unassignedAccessRightsSet.addAll(this.accessRightsSet);
		List<AccessRights> unassignedAccessRightsList = new ArrayList<AccessRights>();
		unassignedAccessRightsList.addAll(unassignedAccessRightsSet);
		List<AccessRights> assignedAccessRightsList = new ArrayList<AccessRights>();
		this.dualAccessRightsList = new DualListModel<AccessRights>(unassignedAccessRightsList, assignedAccessRightsList);	
		
	}
	
	public void onRowSelect(SelectEvent event) { 
		setSelectedRole((Role)event.getObject());
	}
	
	public void doDeleteRole()  {
		try {
			
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			
			getRoleService().delete(selectedRole.getId());			
			setInsertDelete(true);
			auditTrail.log(SystemAuditTrailActivity.UPDATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has deleted Group " + selectedRole.getRole());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Role  " + selectedRole.getRole()+ " deleted successfully"));
		//	new Refresh().refreshPage();
		} catch (BSLException e) {
		//	FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
		//	msg.setSeverity(FacesMessage.SEVERITY_ERROR); 
		//	FacesContext.getCurrentInstance().addMessage(null, msg);  
		}			
	}
	
	
	public List<Role> getSearchRole() {
		return searchRole;
	}
	
	public void setSearchRole(List<Role> searchRole) {
		this.searchRole = searchRole;
	}

	public boolean isInsertDelete() {
		return insertDelete;
	}

	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}

	public String getSearchRoleName() {
		return searchRoleName;
	}

	public void setSearchRoleName(String searchRoleName) {
		this.searchRoleName = searchRoleName;
	}	
	
	public void searchRole(){
		
		if(getSearchRoleName() == null || getSearchRoleName().trim().equals("")){
			this.roleList = null;
			this.roleDataModel = null;			
		}else{
			this.roleList = roleService.findRoleByRoleName(getSearchRoleName());
			this.roleDataModel = null;		
		}
		
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

	public AccessRightsService getAccessRightsService() {
		return accessRightsService;
	}

	public void setAccessRightsService(AccessRightsService accessRightsService) {
		this.accessRightsService = accessRightsService;
	}

	public Set<AccessRights> getAccessRightsSet() {
		return accessRightsSet;
	}

	public void setAccessRightsSet(Set<AccessRights> accessRightsSet) {
		this.accessRightsSet = accessRightsSet;
	}

	public Set<AccessRights> getAssignedAccessRightsSet() {
		return assignedAccessRightsSet;
	}

	public void setAssignedAccessRightsSet(Set<AccessRights> assignedAccessRightsSet) {
		this.assignedAccessRightsSet = assignedAccessRightsSet;
	}

	public Set<AccessRights> getUnassignedAccessRightsSet() {
		return unassignedAccessRightsSet;
	}

	public void setUnassignedAccessRightsSet(
			Set<AccessRights> unassignedAccessRightsSet) {
		this.unassignedAccessRightsSet = unassignedAccessRightsSet;
	}

	public DualListModel<AccessRights> getDualAccessRightsList() {
		return dualAccessRightsList;
	}

	public void setDualAccessRightsList(
			DualListModel<AccessRights> dualAccessRightsList) {
		this.dualAccessRightsList = dualAccessRightsList;
	}
	
	
	
	
}


