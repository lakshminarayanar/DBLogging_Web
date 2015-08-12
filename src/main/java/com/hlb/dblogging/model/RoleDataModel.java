package com.hlb.dblogging.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.hlb.dblogging.jpa.model.Role;



public class RoleDataModel extends ListDataModel<Role> implements SelectableDataModel<Role>{
	
	public RoleDataModel(){	
		
	}

	public RoleDataModel(List<Role> data){
	super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Role getRowData(String rowKey) {
		
		List<Role> roleList = (List<Role>)getWrappedData();
		Integer rowKeyInt = Integer.parseInt(rowKey);
		  for(Role role : roleList) {
	          if(role.getId() == rowKeyInt) {
	              return role;
	          }
	      }
		return null;
	}

	@Override
	public Object getRowKey(Role role) {
		
		return role.getId();
	}
}
