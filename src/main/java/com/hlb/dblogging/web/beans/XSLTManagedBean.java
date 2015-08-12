package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.app.context.FacesUtil;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.jpa.model.XSLT;
import com.hlb.dblogging.jpa.service.XSLTService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.XSLTDataModel;
import com.hlb.dblogging.user.audit.logging.AuditTrail;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailActivity;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailLevel;
import com.hlb.dblogging.xml.utility.XSLTransformer;

@Component
@ViewScoped
public class XSLTManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private XSLT selectedXSLT = new XSLT();
	private XSLT newXSLT = new XSLT();
	private XSLTDataModel xsltDataModel;
	private Users loggedInUser;
	private List<XSLT> xsltList;
	private String uploadingNewXSLT;
	private Boolean dmlOperationPerformed=Boolean.FALSE;
	

	@Autowired
	private XSLTService xsltService;
	
	@Autowired
	private AuditTrail auditTrail;
	
	

	public Users getLoggedInUser() {
		return loggedInUser;
	}


	public void setLoggedInUser(Users loggedInUser) {
		this.loggedInUser = loggedInUser;
	}


	
	public XSLT getSelectedXSLT() {
		return selectedXSLT;
	}


	public void setSelectedXSLT(XSLT selectedXSLT) {
		this.selectedXSLT = selectedXSLT;
	}


	public XSLT getNewXSLT() {
		return newXSLT;
	}


	public void setNewXSLT(XSLT newXSLT) {
		this.newXSLT = newXSLT;
	}


	public XSLTDataModel getXsltDataModel() {
		if(xsltDataModel==null || xsltDataModel.getRowCount()==0 || dmlOperationPerformed){
			ApplLogger.getLogger().info("Entering to get the datamodel..");
			xsltDataModel = new XSLTDataModel(getXSLTListFromDatabase());
		}
		return xsltDataModel;
	}


	public XSLTService getXsltService() {
		return xsltService;
	}


	public void setXsltService(XSLTService xsltService) {
		this.xsltService = xsltService;
	}


	public AuditTrail getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(AuditTrail auditTrail) {
		this.auditTrail = auditTrail;
	}
	
	public List<XSLT> getXsltList() {
		return xsltList;
	}


	public void setXsltList(List<XSLT> xsltList) {
		this.xsltList = xsltList;
	}


	public List<XSLT> getXSLTListFromDatabase(){
		
		if(xsltList ==null || xsltList.isEmpty() || dmlOperationPerformed){
			xsltList =	xsltService.getAllXSLT();
		ApplLogger.getLogger().info("XSLT list of size from database is : "+xsltList.size());
		}
		return xsltList;
	}

		
	public String getUploadingNewXSLT() {
		return uploadingNewXSLT;
	}


	public void setUploadingNewXSLT(String uploadingNewXSLT) {
		this.uploadingNewXSLT = uploadingNewXSLT;
	}


	public void doCreateXSLT() {

		try {
			System.out.println("Getting logged in user from session..");
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			newXSLT.setDeleted(false);
			newXSLT.setCreatedBy(getLoggedInUser().getUsername());
			newXSLT.setCreationTime(new Date());
			newXSLT.setXsltFile(uploadingNewXSLT);
			Boolean status = xsltService.createNewXSLT(newXSLT);
			XSLTransformer.xsltMap.put(newXSLT.getTransType(),uploadingNewXSLT);
			auditTrail.log(SystemAuditTrailActivity.CREATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has created a new XSLT with name : "+newXSLT.getName());
			dmlOperationPerformed = Boolean.TRUE;
			uploadingNewXSLT=null;
			if(status==Boolean.TRUE)
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("SUCCESS : XSLT is added successfully to System"));
			newXSLT = new XSLT();
		} catch(DataIntegrityViolationException e){ 
			  ApplLogger.getLogger().error("XSLT already existed in the System",e);
			  FacesMessage msg = new  FacesMessage("ERROR : XSLT already existed in the System");
			  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			  FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
		catch(Exception e){ 
			  ApplLogger.getLogger().error("Error while adding new XSLT to System",e);
			  FacesMessage msg = new  FacesMessage("ERROR : Database problem while adding new XSLT");
			  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			  FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	public void onRowSelect(SelectEvent event) {
		setSelectedXSLT((XSLT) event.getObject());
		
	}

	public void doInitializeForm() {
		newXSLT = new XSLT();
		uploadingNewXSLT = null;
	}

		
	public void doDeleteXSLT() {
		System.out.println("XSLT is going to be deleted is : "+ selectedXSLT.getName());
		try{
			/*if("ALL".equalsIgnoreCase(selectedXSLT.getTransType()) && ("VIEW".equalsIgnoreCase(selectedXSLT.getViewOrSave()) || "SAVE".equalsIgnoreCase(selectedXSLT.getViewOrSave())))
			{
				FacesMessage msg = new  FacesMessage("ERROR : Database problem while deleting XSLT, please try later");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg); 
			}*/
			loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
			selectedXSLT.setLastModifiedBy(loggedInUser.getUsername());
			selectedXSLT.setLastModifiedTime(new Date());
			selectedXSLT.setDeleted(true);
			Boolean deleteStatus = xsltService.deleteXSLT(selectedXSLT);
			if(deleteStatus==Boolean.TRUE)
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("SUCCESS : XSLT is deleted successfully..!"));
			else
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("ERROR : XSLT is not able delete now, Please try later"));
		
			auditTrail.log(SystemAuditTrailActivity.DELETED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has deleted XSLT with id: "+selectedXSLT.getId());
			dmlOperationPerformed = Boolean.TRUE;
			XSLTransformer.xsltMap.remove(selectedXSLT.getTransType());
			}catch(Exception e){
				 ApplLogger.getLogger().error("Error while deleting XSLT from System",e);
				  FacesMessage msg = new  FacesMessage("ERROR : Database problem while deleting XSLT, please try later");
				  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				  FacesContext.getCurrentInstance().addMessage(null, msg); 
			}
	}
	
	public void doUpdateXSLT() {
		try{
		System.out.println("XSLT is going to be updated is : "+ selectedXSLT.getName());
		if(uploadingNewXSLT!=null)
			selectedXSLT.setXsltFile(uploadingNewXSLT);
		loggedInUser = (Users) FacesUtil.getSessionMapValue("LOGGEDIN_USER");
		selectedXSLT.setLastModifiedBy(loggedInUser.getUsername());
		selectedXSLT.setLastModifiedTime(new Date());
		Boolean updateStatus = xsltService.updateXSLT(selectedXSLT);
		if(updateStatus==Boolean.TRUE)
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("SUCCESS : XSLT is updated successfully..!"));
		else
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("ERROR : XSLT is not able update now, Please try later"));
	
		auditTrail.log(SystemAuditTrailActivity.UPDATED,SystemAuditTrailLevel.INFO, getLoggedInUser().getId(),getLoggedInUser().getUsername(), getLoggedInUser().getUsername() + " has updated a new XSLT with id: "+selectedXSLT.getId());
		dmlOperationPerformed = Boolean.TRUE;
		XSLTransformer.xsltMap.put(selectedXSLT.getTransType(),uploadingNewXSLT);
		uploadingNewXSLT=null;
		}catch(Exception e){
			 ApplLogger.getLogger().error("Error while updating new XSLT to System",e);
			  FacesMessage msg = new  FacesMessage("ERROR : Database problem while updating XSLT, please try later");
			  msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			  FacesContext.getCurrentInstance().addMessage(null, msg); 
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		if (event.getFile() != null) {
				try {
					uploadingNewXSLT = new String(IOUtils.toByteArray(event.getFile().getInputstream()));
					ApplLogger.getLogger().info("Input Data XSLT in String form is : "+ uploadingNewXSLT);
					//FacesMessage msg = new FacesMessage("Info : New XSLT uploaded successfully, Changes will be effected for every 30 minutes");
					FacesMessage msg = new FacesMessage("File is uploaded, Submit to save the new XSLT");
					msg.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} catch (Exception e) {
					FacesMessage msg = new FacesMessage("Error : Unbale to upload now, Please try later..");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
		}
    }}
