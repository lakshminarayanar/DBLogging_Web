package com.hlb.dblogging.web.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.AuditMaster;
import com.hlb.dblogging.jpa.model.SearchBean;
import com.hlb.dblogging.jpa.service.AuditDetailService;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.AuditMasterDataModel;

@Component
@ViewScoped
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
	private Boolean insertDeleted = false;
	private SearchBean searchBean = new SearchBean();
	private AuditMaster selectedMessage = new AuditMaster();
	private Date searchTransactionDate;
	private String messageContent;
	private StreamedContent file;
	
	
	
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

	public Date getSearchTransactionDate() {
		return searchTransactionDate;
	}

	public void setSearchTransactionDate(Date searchTransactionDate) {
		this.searchTransactionDate = searchTransactionDate;
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
		
		if(auditMasterList ==null || insertDeleted ==true){
		auditMasterList =	auditMasterService.getListOfMessages();
		ApplLogger.getLogger().info("AuditMaster list of size from database is : "+auditMasterList.size());
		}
		return auditMasterList;
	}

	public AuditMasterDataModel getAuditMasterDataModel() {
		if(auditMasterDataModel==null || insertDeleted==true){
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
		setSelectedMessage((AuditMaster) event.getObject());
        FacesMessage msg = new FacesMessage("Message Selected", selectedMessage.getUniqueProcessID());  
        ApplLogger.getLogger().info("Message selected with unique ID is : "+selectedMessage.getUniqueProcessID());
        messageContent = auditDetailService.getMessageContentFormatted(selectedMessage.getMessageFormat(), selectedMessage.getUniqueProcessID());
        
        ApplLogger.getLogger().info("Formatted Message Is : "+messageContent);
        
        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
		}catch(Exception e){
			ApplLogger.getLogger().error("Error while finding the content of given message : "+selectedMessage.getUniqueProcessID(),e);
			messageContent = "Unable to get the content presently for this message";
		}
    } 
	
	public void doSearch(){
		try{
		List<AuditMaster>	filteredList = new ArrayList<AuditMaster>();
		ApplLogger.getLogger().info("Input data captured is : "+searchBean);
		searchBean.setTransactionDateTime(searchTransactionDate);
		filteredList  =	auditMasterService.getSearchResultList(searchBean);
		auditMasterDataModel = new AuditMasterDataModel(filteredList);
		}catch(Exception e){
			ApplLogger.getLogger().error("Error caught : ",e);
		}
		
	}
	
	
	public void reset() {
		ApplLogger.getLogger().info("Resetting the search criteria data..");
       searchBean = new SearchBean();
    }
	
	/*@PostConstruct
	public void init() {
		ApplLogger.getLogger().info("Creating AuditMaster in @PostConstruct...");
	    this.selectedMessage = new AuditMaster();
	}*/
	
	public StreamedContent getFile() {
		InputStream stream = new ByteArrayInputStream(messageContent.getBytes());
	    file = new DefaultStreamedContent(stream, "text/xml", selectedMessage.getUniqueProcessID()+".xml");
        return file;
    }
	
	

}
