package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.AuditMaster;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.model.AuditMasterDataModel;

@Component
@Scope("request")
public class AuditMasterManagedBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6134161295509174334L;
	@Autowired
	private AuditMasterService auditMasterService;
	private List<AuditMaster> auditMasterList;
	private AuditMasterDataModel auditMasterDataModel;
	private Boolean insertDeleted = false;

	
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
	
		if(auditMasterDataModel ==null || insertDeleted ==true){
			auditMasterDataModel = new AuditMasterDataModel(getAuditMasterMessageList());
		}
		return auditMasterDataModel;
	}

	public void setAuditMasterDataModel(AuditMasterDataModel auditMasterDataModel) {
		this.auditMasterDataModel = auditMasterDataModel;
	}
	
	
	
}
