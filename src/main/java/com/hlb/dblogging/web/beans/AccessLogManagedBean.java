package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.jpa.model.AccessLog;
import com.hlb.dblogging.model.AccessLogDataModel;
import com.hlb.dblogging.user.accesslog.logging.AccessLogService;



@Component
@RequestScoped
public class AccessLogManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	

	
	@Autowired
	AccessLogService accessLogService;
	
	private List<AccessLog> accessLogList;
	private AccessLogDataModel accessLogDataModel;

	private boolean insertDelete = false;

	
	public AccessLogService getAccessLogService() {
		return accessLogService;
	}
	
	public void setAccessLogService(AccessLogService accessLogService) {
		this.accessLogService = accessLogService;
	}
	public List<AccessLog> getAccessLogList() {
		if(accessLogList==null || insertDelete==true ){
			accessLogList = accessLogService.findAll();
		}
	    return accessLogList;
	}
	
	public void setAccessLogList(List<AccessLog> accessLogList) {
		this.accessLogList = accessLogList;
	}
	public AccessLogDataModel getAccessLogDataModel() {
		
		return new AccessLogDataModel(getAccessLogList());
	}
	public void setAccessLogDataModel(AccessLogDataModel accessLogDataModel) {
		this.accessLogDataModel = accessLogDataModel;
	}
	public boolean isInsertDelete() {
		return insertDelete;
	}
	public void setInsertDelete(boolean insertDelete) {
		this.insertDelete = insertDelete;
	}
	
	
	
	
}
