package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.app.context.FacesUtil;
import com.hlb.dblogging.exception.utility.BSLException;
import com.hlb.dblogging.jpa.model.AccessRights;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.model.AccessRightsDataModel;
import com.hlb.dblogging.security.users.service.AccessRightsService;
import com.hlb.dblogging.user.audit.logging.AuditTrail;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailActivity;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailLevel;


@Component
@RequestScoped
public class AccessRightsManagedBean implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Autowired
	AccessRightsService accessRightsService;
	
	private List<AccessRights> accessRightsList;
	private AccessRightsDataModel accessRightsDataModel;
	private AccessRights newAccessRights = new AccessRights();
	private AccessRights selectedAccessRights = new AccessRights();
	private boolean insertDelete = false;
	private List<AccessRights> searchAccessRights;
	
	private String searchAccessRight = "";
	
	private Users loggedInUser;
	
	@Autowired
	private AuditTrail auditTrail;	
	
	public AccessRightsService getAccessRightsService() {
		return accessRightsService;
	}
	
	public void setAccessRightsService(AccessRightsService accessRightsService) {
		this.accessRightsService = accessRightsService;
	}
	
	
	public List<AccessRights> getAccessRightsList() {
		
		if(accessRightsList == null || insertDelete == true){
			
			accessRightsList = accessRightsService.findAll();
		}		
		return accessRightsList;
	}
	
	public void setAccessRightsList(List<AccessRights> accessRightsList) {
		this.accessRightsList = accessRightsList;
	}
	
	
	public AccessRightsDataModel getAccessRightsDataModel() {
		
	/*	if(accessRightsDataModel == null || insertDelete == true){
			
			accessRightsDataModel = new AccessRightsDataModel(getAccessRightsList());
		}		
		return accessRightsDataModel;*/
		
		return new AccessRightsDataModel(getAccessRightsList());
	}
	
	public void setAccessRightsDataModel(AccessRightsDataModel accessRightsDataModel) {
		this.accessRightsDataModel = accessRightsDataModel;
	}
	
	
	public AccessRights getNewAccessRights() {
		return newAccessRights;
	}
	
	public void setNewAccessRights(AccessRights newAccessRights) {
		this.newAccessRights = newAccessRights;
	}
	
	public void doCreateAccessRights() {
	try{
		
		loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
		
		newAccessRights.setDeleted(false);
		newAccessRights.setCreatedBy(getLoggedInUser().getUsername());
		newAccessRights.setCreationTime(new java.util.Date());
		getAccessRightsService().create(newAccessRights);
		setInsertDelete(true);	
		auditTrail.log(SystemAuditTrailActivity.CREATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has created new AccessRight " +newAccessRights.getAccessRights());
		
	 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Access Right  " + newAccessRights.getAccessRights()+ " created successfully"));
	 newAccessRights = new AccessRights();	
	 //new Refresh().refreshPage();
	}catch(BSLException e){
	//	FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
	//	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
     //   FacesContext.getCurrentInstance().addMessage(null, msg); 
	}
}
	
	
	public AccessRights getSelectedAccessRights() {
		return selectedAccessRights;
	}
	
	public void setSelectedAccessRights(AccessRights selectedAccessRights) {
		this.selectedAccessRights = selectedAccessRights;
	}
	
	
	public void doUpdateAccessRights() {
		try {
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			
			System.out.println("New UserAccess:" + selectedAccessRights.getAccessRights());
			System.out.println("Id:" + selectedAccessRights.getId());
			System.out.println("Description:" + selectedAccessRights.getDescription());
			selectedAccessRights.setLastModifiedBy(getLoggedInUser().getUsername());
			accessRightsService.update(selectedAccessRights);		
			setInsertDelete(true);
			auditTrail.log(SystemAuditTrailActivity.UPDATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has updated AccessRight " +selectedAccessRights.getAccessRights());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Access Right " + selectedAccessRights.getAccessRights()+ " updated successfully"));
		} catch (BSLException e){
		//	FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
		//	msg.setSeverity(FacesMessage.SEVERITY_ERROR);  
		//	FacesContext.getCurrentInstance().addMessage(null, msg);  
		}		
	}
	
	public void onRowSelect(SelectEvent event){
		setSelectedAccessRights((AccessRights)event.getObject());
	}
	
	
	public void doDeleteAccessRights() {
		try {
			
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			
			getAccessRightsService().delete(selectedAccessRights.getId());
			setInsertDelete(true);
			auditTrail.log(SystemAuditTrailActivity.DELETED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has deleted AccessRight " +selectedAccessRights.getAccessRights());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS : Access Right " + selectedAccessRights.getAccessRights()+ " deleted successfully"));
		//	new Refresh().refreshPage();
		}catch (BSLException e) {
		//	FacesMessage msg = new FacesMessage("Error",getExcptnMesProperty(e.getMessage()));  
		//	msg.setSeverity(FacesMessage.SEVERITY_ERROR); 
		//	FacesContext.getCurrentInstance().addMessage(null, msg);  
		}			
	}
	
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	public List<AccessRights> getSearchAccessRights() {
		return searchAccessRights;
	}
	public void setSearchAccessRights(List<AccessRights> searchAccessRights) {
		this.searchAccessRights = searchAccessRights;
	}

	public String getSearchAccessRight() {
		return searchAccessRight;
	}

	public void setSearchAccessRight(String searchAccessRight) {
		this.searchAccessRight = searchAccessRight;
	}	
	
	
	public void searchAccessRightName(){		
		if(getSearchAccessRight() == null || getSearchAccessRight().trim().equals("")){
			this.accessRightsList = null;
			this.accessRightsDataModel = null;			
		}else {
			this.accessRightsList = accessRightsService.findAccessRightsByAccessRight(getSearchAccessRight());
			this.accessRightsDataModel = null;
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
	
}
