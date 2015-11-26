package com.hlb.dblogging.app.activedirectory.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hlb.dblogging.log.utility.ApplLogger;
import com.hlb.dblogging.xml.utility.XSLTransformer;



public class PropertiesConfig {
	
	
	
	final static Logger logger =ApplLogger.getLogger();
	
	
	public PropertiesConfig(){
		
		
		try {
			doSomeOperation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doSomeOperation() throws IOException {
		 String result = "";
			InputStream inputStream;
		 Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
					RC4 rc4=new RC4();
					XSLTransformer.rc4Key=prop.getProperty("DBLoggingPassKey");
					rc4.setKey(XSLTransformer.rc4Key);
				    XSLTransformer.ldapUsername=prop.getProperty("ldap.username");
		            XSLTransformer.ldapPassword=rc4.decrypt(prop.getProperty("ldap.password"));
		            XSLTransformer.ldapUrl=prop.getProperty("ldap.url");
		            XSLTransformer.ldapDomain=prop.getProperty("ldap.domain");
		            XSLTransformer.ldapSearchFilter=prop.getProperty("ldap.searchfilter");
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
	    }
	
}
