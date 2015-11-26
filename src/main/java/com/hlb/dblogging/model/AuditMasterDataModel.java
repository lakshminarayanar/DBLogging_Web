package com.hlb.dblogging.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.hlb.dblogging.jpa.model.AuditMaster;


public class AuditMasterDataModel extends ListDataModel<AuditMaster> implements SelectableDataModel<AuditMaster> {

	
	public AuditMasterDataModel(){
		
	}
	
	public AuditMasterDataModel(List<AuditMaster> data){
		super(data);
		
	}
	
	@SuppressWarnings("unchecked")
	public AuditMaster getRowData(String rowkey) {
		  List<AuditMaster> AuditMasterList = (List<AuditMaster>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(AuditMaster AuditMaster : AuditMasterList) {
	            if(AuditMaster.getId() == rowKeyInt) {
	                return AuditMaster;
	            }
	        }
	        
	        return null;

		
	}

	public Object getRowKey(AuditMaster AuditMaster) {
		
		return AuditMaster.getId();
	}


	
    
}