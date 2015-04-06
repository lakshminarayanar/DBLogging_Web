package com.hlb.dblogging.log.utility;

import org.apache.log4j.Logger;

public class ApplLogger {
	private static Logger log;
	
	public static Logger getLogger() {
		if(log == null) {
			log = Logger.getLogger(ApplLogger.class);
		}
		
		return log;
	}
}
