/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;

/**
 * @author Marco Scata
 *
 */
public class DbAuditPluginImpl implements DbAuditPlugin {
	private boolean useJndi;
	private String jndiDatasource;
	
	private String jdbcDriver;
	private String jdbcUrl;
	
	private String username;
	private String password;
	/**
	 * @return the useJndi
	 */
	public boolean isUseJndi() {
		return useJndi;
	}
	/**
	 * @param useJndi the useJndi to set
	 */
	public void setUseJndi(boolean useJndi) {
		this.useJndi = useJndi;
	}
	/**
	 * @return the jndiDatasource
	 */
	public String getJndiDatasource() {
		return jndiDatasource;
	}
	/**
	 * @param jndiDatasource the jndiDatasource to set
	 */
	public void setJndiDatasource(String jndiDatasource) {
		this.jndiDatasource = jndiDatasource;
	}
	/**
	 * @return the jdbcDriver
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
