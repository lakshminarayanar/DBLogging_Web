package com.hlb.dblogging.app.activedirectory.service;


import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.xml.utility.XSLTransformer;



@Service
public class ActiveDirectoryServiceImpl implements	ActiveDirectoryService {

	

	static NamingEnumeration<SearchResult> answer;

	final static Logger logger =ApplLogger.getLogger();

	
	

	public LdapContext getLdapContext() {
		LdapContext ctx = null;
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "Simple");
			env.put(Context.SECURITY_PRINCIPAL, XSLTransformer.ldapUsername);//input user & password for access to ldap
			env.put(Context.SECURITY_CREDENTIALS, XSLTransformer.ldapPassword);
			env.put(Context.PROVIDER_URL, XSLTransformer.ldapUrl);
			env.put(Context.REFERRAL, "follow");
			ctx = new InitialLdapContext(env, null);
			//logger.info("LDAP Connection: COMPLETE");
		} catch (NamingException nex) {
		//	logger.error("LDAP Connection: FAILED");
			 nex.printStackTrace();
		}
		catch (Exception nex) {
			//	logger.error("LDAP Connection: FAILED");
				 nex.printStackTrace();
			}
		return ctx;
	}

	public LdapContext getLdapContext(String username,String password,String Domain,String url) throws Exception {
		LdapContext ctx = null;
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "Simple");
			env.put(Context.SECURITY_PRINCIPAL, username);//input user & password for access to ldap
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.REFERRAL, "follow");
			ctx = new InitialLdapContext(env, null);
			logger.info("LDAP Connection: COMPLETE");
		} catch (NamingException nex) {

			String msg="LDAP Connection: FAILED";
			logger.error(msg);
			throw new Exception(msg);

		}
		return ctx;
	}

	private static SearchControls getSearchControls() {
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = {"distinguishedName"};
		cons.setReturningAttributes(attrIDs);
		return cons;
	}
	@SuppressWarnings("unused")
	public  Boolean  checkUserinAD(String username,LdapContext ldap){

		boolean msg=Boolean.FALSE;
		SearchControls  searchControls=getSearchControls();
		String searchBase,searchFilter;
		try {
			String domain=getDomainBase(XSLTransformer.ldapDomain);
			String searchUser="(sAMAccountName="+username+")";
			if(!XSLTransformer.ldapSearchFilter.isEmpty()){
				searchBase =XSLTransformer.ldapSearchFilter;
			}
			else{
				searchBase=getDomainBase(XSLTransformer.ldapDomain);
			}
			answer = ldap.search(searchBase ,searchUser, searchControls);
			if(answer.hasMore())
			{
				msg=Boolean.TRUE;

			}

		}
		catch(Exception e){
				e.printStackTrace();

		}
		return msg;

	}

	public static  String getDomainBase(String base) {
		char[] namePair = base.toUpperCase().toCharArray();
		String dn = "DC=";
		for (int i = 0; i < namePair.length; i++) {
			if (namePair[i] == '.') {
				dn += ",DC=" + namePair[++i];
			} else {
				dn += namePair[i];
			}
		}
		return dn;
	}



}

