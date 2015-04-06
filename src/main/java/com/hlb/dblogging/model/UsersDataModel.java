package com.hlb.dblogging.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.hlb.dblogging.jpa.model.Users;


public class UsersDataModel extends ListDataModel<Users> implements SelectableDataModel<Users> {

	
	public UsersDataModel(){
		
	}
	
	public UsersDataModel(List<Users> data){
		super(data);	
	}
	@SuppressWarnings("unchecked")
	@Override
	public Users getRowData(String rowkey) {
		  List<Users> UsersList = (List<Users>) getWrappedData();
	        Integer rowKeyInt = Integer.parseInt(rowkey);
	        for(Users users : UsersList) {
	            if(users.getId() == rowKeyInt) {
	                return users;
	            }
	        }
	        
	        return null;		
	}

	@Override
	public Object getRowKey(Users Users) {
		
		return Users.getId();
	}


	
    
}