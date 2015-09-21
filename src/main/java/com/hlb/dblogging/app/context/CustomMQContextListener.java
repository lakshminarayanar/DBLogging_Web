package com.hlb.dblogging.app.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.hlb.dblogging.app.activedirectory.service.PropertiesConfig;
import com.hlb.dblogging.log.utility.ApplLogger;

@WebListener
public final class CustomMQContextListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {

		/*
		 * This method is called prior to the servlet context being initialized
		 * (when the Web application is deployed). You can initialize servlet
		 * context related data here.
		 */
		new PropertiesConfig();
		new SystemConfigPropertiesService().updateConfigurationWithDatabaseValues();
	}

	public void contextDestroyed(ServletContextEvent event) {

		/*
		 * This method is invoked when the Servlet Context (the Web application)
		 * is undeployed or WebLogic Server shuts down.
		 */
		ApplLogger.getLogger().info("contextDestroyed completed... @@@ ");

	}
}