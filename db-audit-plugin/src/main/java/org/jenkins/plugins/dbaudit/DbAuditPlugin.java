/**
 * 
 */
package org.jenkins.plugins.dbaudit;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditPlugin {
	boolean isUseJndi();
	String getJndiDatasource();
	void setJndiDatasource(String datasource);
	String getJdbcDriver();
	void setJdbcDriver(String driver);
	String getUsername();
	void setUsername(String username);
	void setPassword(String password);
}
