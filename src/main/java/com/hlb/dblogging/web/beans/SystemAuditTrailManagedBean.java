package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.SystemAuditTrail;
import com.hlb.dblogging.model.SystemAuditTrailDataModel;
import com.hlb.dblogging.user.audit.logging.SystemAuditTrailRecordService;


@Component
@ViewScoped
public class SystemAuditTrailManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	final static Logger logger = Logger.getLogger(SystemAuditTrailManagedBean.class);
    
	
	@Autowired
	SystemAuditTrailRecordService systemAuditTrailService;
	
	private List<SystemAuditTrail> systemAuditTrailList;
	private SystemAuditTrailDataModel systemAuditTrailDataModel;	
	private boolean insertDelete = false;

	
	public SystemAuditTrailRecordService getSystemAuditTrailService() {
		return systemAuditTrailService;
	}
	public void setSystemAuditTrailService(
			SystemAuditTrailRecordService systemAuditTrailService) {
		this.systemAuditTrailService = systemAuditTrailService;
	}
	public List<SystemAuditTrail> getSystemAuditTrailList() {
			if(systemAuditTrailList==null || insertDelete==true){
			    systemAuditTrailList = systemAuditTrailService.findAll();
			}
		return systemAuditTrailList;
	}
	public void setSystemAuditTrailList(List<SystemAuditTrail> systemAuditTrailList) {
		this.systemAuditTrailList = systemAuditTrailList;
	}
	public SystemAuditTrailDataModel getSystemAuditTrailDataModel() {
		return new SystemAuditTrailDataModel(getSystemAuditTrailList());
	}
	public void setSystemAuditTrailDataModel(
			SystemAuditTrailDataModel systemAuditTrailDataModel) {
		this.systemAuditTrailDataModel = systemAuditTrailDataModel;
	}
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	
	
	
	
}
