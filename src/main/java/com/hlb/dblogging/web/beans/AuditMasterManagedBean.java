package com.hlb.dblogging.web.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.AuditMaster;
import com.hlb.dblogging.jpa.model.SearchBean;
import com.hlb.dblogging.jpa.service.AuditDetailService;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.AuditMasterDataModel;

@Component
@ViewScoped
@Scope("session")
public class AuditMasterManagedBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6134161295509174334L;
	@Autowired
	private AuditMasterService auditMasterService;
	@Autowired
	private AuditDetailService auditDetailService;
	private List<AuditMaster> auditMasterList;
	private AuditMasterDataModel auditMasterDataModel;
	private AuditMasterDataModel searchResultDataModel;
	private Boolean insertDeleted = false;
	private String loginSuccess;
	private SearchBean searchBean = new SearchBean();
	private AuditMaster selectedMessage = new AuditMaster();
	private Date transactionStartDateTime;
	private Date transactionEndDateTime;
	private String messageContent;
	private StreamedContent file;
	private AuditMaster selectedRecord = new AuditMaster();
	
	public Date getTransactionStartDateTime() {
		return transactionStartDateTime;
	}

	public void setTransactionStartDateTime(Date transactionStartDateTime) {
		this.transactionStartDateTime = transactionStartDateTime;
	}

	public Date getTransactionEndDateTime() {
		return transactionEndDateTime;
	}

	public void setTransactionEndDateTime(Date transactionEndDateTime) {
		this.transactionEndDateTime = transactionEndDateTime;
	}

	public String getLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(String loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public AuditMaster getSelectedRecord() {
		return selectedRecord;
	}

	public void setSelectedRecord(AuditMaster selectedRecord) {
		this.selectedRecord = selectedRecord;
	}


	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public AuditDetailService getAuditDetailService() {
		return auditDetailService;
	}

	public void setAuditDetailService(AuditDetailService auditDetailService) {
		this.auditDetailService = auditDetailService;
	}

	
	public AuditMasterDataModel getSearchResultDataModel() {
		return searchResultDataModel;
	}

	public void setSearchResultDataModel(AuditMasterDataModel searchResultDataModel) {
		this.searchResultDataModel = searchResultDataModel;
	}

	public AuditMaster getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(AuditMaster selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

	public AuditMasterService getAuditMasterService() {
		return auditMasterService;
	}

	public void setAuditMasterService(AuditMasterService auditMasterService) {
		this.auditMasterService = auditMasterService;
	}

	public Boolean getInsertDeleted() {
		return insertDeleted;
	}

	public void setInsertDeleted(Boolean insertDeleted) {
		this.insertDeleted = insertDeleted;
	}

	public List<AuditMaster> getAuditMasterList() {
		return auditMasterList;
	}

	public void setAuditMasterList(List<AuditMaster> auditMasterList) {
		this.auditMasterList = auditMasterList;
	}
	
	public List<AuditMaster> getAuditMasterMessageList(){
		
		if(auditMasterList ==null || auditMasterList.isEmpty()){
		auditMasterList =	auditMasterService.getListOfMessagesByTime(new Date());
		ApplLogger.getLogger().info("AuditMaster list of size from database is : "+auditMasterList.size());
		insertDeleted=false;
		}
		return auditMasterList;
	}

	public AuditMasterDataModel getAuditMasterDataModel() {
		FacesContext f = FacesContext.getCurrentInstance();
		Map<String, String> parameterMap = (Map<String, String>) f.getExternalContext().getRequestParameterMap();
		if("success".equalsIgnoreCase(parameterMap.get("login"))){
			auditMasterDataModel= new AuditMasterDataModel();
			searchBean=new SearchBean();
			transactionStartDateTime=null;
			transactionEndDateTime=null;
		}
		
		if(auditMasterDataModel==null){
			ApplLogger.getLogger().info("Entering to get the datamodel..");
			auditMasterDataModel = new AuditMasterDataModel(getAuditMasterMessageList());
		}
		return auditMasterDataModel;
	}

	public void setAuditMasterDataModel(AuditMasterDataModel auditMasterDataModel) {
		this.auditMasterDataModel = auditMasterDataModel;
	}
	
	
	public void onRowSelect(SelectEvent event) {  
		try{
		// code to get the list of messages associated with unique process ID
		setSelectedMessage((AuditMaster) event.getObject());
        FacesMessage msg = new FacesMessage("Message Selected", selectedMessage.getUniqueProcessID());  
        ApplLogger.getLogger().info("Message selected with unique ID is : "+selectedMessage.getUniqueProcessID());
        List<AuditMaster>	searchResultList =	auditMasterService.getResultByUniqueProcessId(selectedMessage.getUniqueProcessID());
        ApplLogger.getLogger().info("List size for the uniqueprocessid : "+selectedMessage.getUniqueProcessID()+" is : "+searchResultList.size());
        searchResultDataModel = new AuditMasterDataModel(searchResultList);
        
        selectedRecord = searchResultDataModel.getRowData(String.valueOf(selectedMessage.getId()));
        ApplLogger.getLogger().info("Message selected with unique ID is : "+selectedRecord.getUniqueProcessID());
        messageContent = auditDetailService.getMessageContentFormatted(selectedRecord.getMessageFormat(), String.valueOf(selectedMessage.getId()),selectedRecord.getTransType());
        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}catch(Exception e){
			ApplLogger.getLogger().error("Error while finding the content of given message : "+selectedMessage.getUniqueProcessID(),e);
			messageContent = "Unable to get the content presently for this message";
		}
    } 
	
	public void onMessageRowSelect(String auditMasterId) {  
		try{
        selectedRecord = searchResultDataModel.getRowData(auditMasterId);
        ApplLogger.getLogger().info("Message selected with unique ID is : "+selectedRecord.getUniqueProcessID());
        messageContent = auditDetailService.getMessageContentFormatted(selectedRecord.getMessageFormat(), auditMasterId,selectedRecord.getTransType());
        ApplLogger.getLogger().info("Formatted Message Is : "+messageContent);
		}catch(Exception e){
			ApplLogger.getLogger().error("Error while finding the content of given message : "+selectedRecord.getUniqueProcessID(),e);
			messageContent = "Unable to get the content presently for this message";
		}
    } 
	
	public int getSearchListSize() {
		if(searchResultDataModel!=null)
			return searchResultDataModel.getRowCount();
		else return 0;
	}
	
	public void doSearch(){
		try{
		List<AuditMaster>	filteredList = new ArrayList<AuditMaster>();
		ApplLogger.getLogger().info("Input data captured is : "+searchBean);
		searchBean.setTransactionStartDateTime(transactionStartDateTime);
		searchBean.setTransactionEndDateTime(transactionEndDateTime);
		filteredList  =	auditMasterService.getSearchResultList(searchBean);
		auditMasterDataModel = new AuditMasterDataModel(filteredList);
		}catch(Exception e){
			ApplLogger.getLogger().error("Error caught : ",e);
		}
		
	}
	
	
	public void reset() {
		ApplLogger.getLogger().info("Resetting the search criteria data..");
       searchBean = new SearchBean();
       transactionStartDateTime=null;
       transactionEndDateTime=null;
    }
	
		
	public StreamedContent getFile() {
		InputStream stream = new ByteArrayInputStream(messageContent.getBytes());		// Check the message format, if it is xml, directly download the content else, download as text file
		if("XML".equalsIgnoreCase(selectedRecord.getMessageFormat()))
			file = new DefaultStreamedContent(stream, "text/xml", selectedRecord.getUniqueProcessID()+".xml");
		else
			file = new DefaultStreamedContent(stream, "text/xml", selectedRecord.getUniqueProcessID()+".txt");
        return file;
    }
	
	public StreamedContent getFileRaw() {
		try{
		messageContent = auditDetailService.getMessageContentInRawFormat(selectedRecord.getMessageFormat(), String.valueOf(selectedMessage.getId()));
		InputStream stream = new ByteArrayInputStream(messageContent.getBytes());		// Check the message format, if it is xml, directly download the content else, download as text file
			file = new DefaultStreamedContent(stream, "text/xml", selectedRecord.getUniqueProcessID()+".txt");
		}catch(Exception e){
			ApplLogger.getLogger().error("Error while trying to download the file with uniqueprocess Id : "+selectedRecord.getUniqueProcessID(), e);
		}
		  return file;
    }

}
