/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import java.io.IOException;

import javax.servlet.ServletException;

import hudson.Plugin;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;

import net.sf.json.JSONObject;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * @author Marco Scata
 *
 */
public class DbAuditPluginImpl extends Plugin implements DbAuditPlugin, Describable<DbAuditPluginImpl> {
	private boolean useJndi;
	private String jndiDatasource;
	
	private String jdbcDriver;
	private String jdbcUrl;
	
	private String username;
	private String password;
	
	public static final Descriptor<DbAuditPluginImpl> PluginDescriptor = new Descriptor<DbAuditPluginImpl>() {
		public final String DISPLAY_NAME = "Database Audit Plugin";
		@Override
		public String getDisplayName() {
			return DISPLAY_NAME;
		}
	};
	
	public DbAuditPluginImpl(){
		super();
	};
	
	@DataBoundConstructor
	public DbAuditPluginImpl(
			final boolean useJndi, final String jndiDatasource,
			final String jdbcDriver, final String jdbcUrl,
			final String username, final String password) {
		this();
		this.useJndi = useJndi;
		this.jndiDatasource = jndiDatasource;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * @see DbAuditPlugin#isUseJndi()
	 */
	@Override
	public boolean isUseJndi() {
		return useJndi;
	}
	/**
	 * @see DbAuditPlugin#setUseJndi(boolean)
	 */
	@Override
	public void setUseJndi(boolean useJndi) {
		this.useJndi = useJndi;
	}
	/**
	 * @see DbAuditPlugin#getJndiDatasource()
	 */
	@Override
	public String getJndiDatasource() {
		return jndiDatasource;
	}
	/**
	 * @see DbAuditPlugin#setJndiDatasource(String)
	 */
	@Override
	public void setJndiDatasource(String jndiDatasource) {
		this.jndiDatasource = jndiDatasource;
	}
	/**
	 * @see DbAuditPlugin#getJdbcDriver()
	 */
	@Override
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	/**
	 * @see DbAuditPlugin#setJdbcDriver(String)
	 */
	@Override
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	/**
	 * @see DbAuditPlugin#getJdbcUrl()
	 */
	@Override
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	/**
	 * @see DbAuditPlugin#setJdbcUrl(String)
	 */
	@Override
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	/**
	 * @see DbAuditPlugin#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	/**
	 * @see DbAuditPlugin#setUsername(String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password for the specified user.
	 */
	String getPassword() {
		return password;
	}
	/**
	 * @see DbAuditPlugin#setPassword(String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public void configure(final StaplerRequest req, final JSONObject formData)
			throws IOException, ServletException, FormException {
		super.configure(req, formData);
		final JSONObject datasourceDetails = formData.getJSONObject("datasource");
		this.useJndi = datasourceDetails.getBoolean("value");
		if (this.useJndi) {
			this.jndiDatasource = datasourceDetails.getString("jndiDatasource");
		} else {
			this.jdbcDriver = datasourceDetails.getString("jdbcDriver");
			this.jdbcUrl = datasourceDetails.getString("jdbcUrl");
		}
		this.username = datasourceDetails.getString("username");
		this.password = datasourceDetails.getString("password");
	}

	@Override
	public Descriptor getDescriptor() {
		return PluginDescriptor;
	}
}
