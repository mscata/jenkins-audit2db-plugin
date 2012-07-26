/**
 * 
 */
package org.jenkins.plugins.dbaudit;

import hudson.model.Describable;

import javax.sql.DataSource;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditPlugin extends Describable<DbAuditPlugin> {
	/**
	 * @return whether to use a JNDI data source or not.
	 */
	boolean isUseJndi();
	
	/**
	 * @param useJndi whether to use a JNDI data source or not.
	 */
	void setUseJndi(boolean useJndi);
	
	/**
	 * @return the name of the JNDI data source.
	 */
	String getJndiName();
	
	/**
	 * @param datasource a valid JNDI data source.
	 */
	void setJndiName(String datasource);
	
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
	 * @return the user for the specified data source.
	 */
	String getUsername();
	
	/**
	 * @param username a valid user for the specified data source.
	 */
	void setUsername(String username);
	
	/**
	 * @param password a valid password for the specified user.
	 */
	void setPassword(String password);
	
	/**
	 * @return <code>true</code> if the data source is reachable.
	 */
	boolean testDatasourceConnection();
	
	/**
	 * @return the data source for the audit database.
	 */
	DataSource getDatasource();
}
