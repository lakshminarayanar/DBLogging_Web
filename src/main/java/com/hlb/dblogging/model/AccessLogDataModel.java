package com.hlb.dblogging.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.hlb.dblogging.jpa.model.AccessLog;

public class AccessLogDataModel extends ListDataModel<AccessLog> implements SelectableDataModel<AccessLog>{

	
	public AccessLogDataModel(){	
		
	}
	
	public AccessLogDataModel(List<AccessLog> data){		
		super(data);
	}
	
	@SuppressWarnings("unchecked")
	
	public AccessLog getRowData(String rowKey) {
		
		List<AccessLog> accessLogList = (List<AccessLog>)getWrappedData();
		Integer rowKeyInt = Integer.parseInt(rowKey);
		  for(AccessLog accessLog : accessLogList) {
	          if(accessLog.getAccessLogId() == rowKeyInt) {
	              return accessLog;
	          }
	      }
		return null;
	}

	
	
	public Object getRowKey(AccessLog accessRights) {
		
		return accessRights.getAccessLogId();
	}
}
