package com.hlb.dblogging.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hlb.dblogging.security.users.service.UsersService;
import com.hlb.dblogging.jpa.model.Users;
import com.hlb.dblogging.jpa.service.AuditMasterService;
import com.hlb.dblogging.model.UsersDataModel;

@Component
@Scope("request")
public class UsersManagedBean implements Serializable {

/**
	 * 
	 */
	private String txt1;
	private String txt2;
	private static final long serialVersionUID = 1L;
	List<String> users = new ArrayList<String>();

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private AuditMasterService auditMasterService;

	private List<Users> userslists;
	private UsersDataModel userdatamodel;
	private Boolean insertDeleted = false;
	

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public String getTxt2() {
		return txt2;
	}

	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}

	public AuditMasterService getAuditMasterService() {
		return auditMasterService;
	}

	public void setAuditMasterService(AuditMasterService auditMasterService) {
		this.auditMasterService = auditMasterService;
	}
	

	public List<Users> getUserslists() {
		return userslists;
	}

	public void setUserslists(List<Users> userslists) {
		this.userslists = userslists;
	}

	public UsersService getUsersService() {
		return usersService;
	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

	public List<String> getUserList(String username){
        List<String> results = new ArrayList<String>();
		results=usersService.findUsersList(username);
		return results;
	}
	
	public List<Users> getAllUsersList(){
		if(userslists ==null || insertDeleted ==true){
			userslists =usersService.findAllUsers();
			
	}
		return userslists;

	}

	public UsersDataModel getUserdatamodel() {
		if(userdatamodel ==null || insertDeleted ==true){
			userdatamodel = new UsersDataModel(getAllUsersList());
		}
		return userdatamodel;
	}

	public void setUserdatamodel(UsersDataModel userdatamodel) {
		this.userdatamodel = userdatamodel;
	}
	
	public String createUser(){
	Users user=new Users();
	user.setUsername(txt1);
	usersService.create(user);	
	System.out.println("success");
	return "/pages/index.xhtml"; 
	}
}
 /*  public List<String> getUserList(String query) {
        List<String> results = new ArrayList<String>();
        for(int i = 0; i < 3; i++) {
            results.add(query + i);
        }
         
        return results;
    }
	*/

	




