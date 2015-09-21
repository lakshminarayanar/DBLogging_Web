package com.hlb.dblogging.app.activedirectory.service;

import javax.naming.ldap.LdapContext;

public interface ActiveDirectoryService {

	 LdapContext getLdapContext() throws Exception;
	 
	 Boolean checkUserinAD(String username,LdapContext ldap);
	
	
}
