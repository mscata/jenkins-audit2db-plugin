/**
 * 
 */
package org.jenkins.plugins.dbaudit;

import java.sql.Connection;

import javax.sql.DataSource;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditPlugin {
	/**
	 * @return whether to use a JNDI datasource or not.
	 */
	boolean isUseJndi();
	
	/**
	 * @param useJndi whether to use a JNDI datasource or not.
	 */
	void setUseJndi(boolean useJndi);
	
	/**
	 * @return the name of the JNDI datasource.
	 */
	String getJndiDatasource();
	
	/**
	 * @param datasource a valid JNDI datasource.
	 */
	void setJndiDatasource(String datasource);
	
	/**
	 * @return the JDBC driver class name.
	 */
	String getJdbcDriver();
	
	/**
	 * @param driver the JDBC driver class name.
	 */
	void setJdbcDriver(String driver);
	
	/**
	 * @return the JDBC connection URL.
	 */
	String getJdbcUrl();
	
	/**
	 * @param url the JDBC connection URL.
	 */
	void setJdbcUrl(String url);
	
	/**
	 * @return the user for the specified datasource.
	 */
	String getUsername();
	
	/**
	 * @param username a valid user for the specified datasource.
	 */
	void setUsername(String username);
	
	/**
	 * @param password a valid password for the specified user.
	 */
	void setPassword(String password);
	
	/**
	 * @return <code>true</code> if the datasource is reachable.
	 */
	boolean testDatasourceConnection();
	
	/**
	 * @return the datasource for the audit database.
	 */
	DataSource getDatasource();
}
