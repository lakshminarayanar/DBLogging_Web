package com.hlb.dblogging.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.hlb.dblogging.jpa.model.XSLT;


public class XSLTDataModel extends ListDataModel<XSLT> implements SelectableDataModel<XSLT> {

	
	public XSLTDataModel(){
		
	}
	
	public XSLTDataModel(List<XSLT> data){
		super(data);	
	}
	@SuppressWarnings("unchecked")
	@Override
	public XSLT getRowData(String rowkey) {
		  List<XSLT> xsltList = (List<XSLT>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(XSLT xslt : xsltList) {
	            if(xslt.getId() == rowKeyInt) {
	                return xslt;
	            }
	        }
	        
	        return null;		
	}

	@Override
	public Object getRowKey(XSLT xslt) {
		
		return xslt.getId();
	}


	
    
}