/**
 * 
 */
package org.jenkins.plugins.audit2db.internal;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.jenkins.plugins.audit2db.DbAuditPublisherDescriptor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author Marco Scata
 *
 */
//@Extension
public class DbAuditPublisherDescriptorImpl extends
		BuildStepDescriptor<Publisher> implements DbAuditPublisherDescriptor {
	private final static Logger LOGGER = Logger.getLogger(DbAuditPublisherDescriptorImpl.class.getName());

	private transient DataSource datasource;
	
	private boolean useJndi;
	private String jndiName;
	private String jndiUser;
	private String jndiPassword;

	private String jdbcDriver;
	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getUseJndi()
	 */
	public boolean getUseJndi() {
		return useJndi;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setUseJndi(boolean)
	 */
	public void setUseJndi(final boolean useJndi) {
		this.useJndi = useJndi;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getJndiName()
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJndiName(java.lang.String)
	 */
	public void setJndiName(final String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getJdbcDriver()
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJdbcDriver(java.lang.String)
	 */
	public void setJdbcDriver(final String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getJdbcUrl()
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJdbcUrl(java.lang.String)
	 */
	public void setJdbcUrl(final String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getJndiUser()
	 */
	public String getJndiUser() {
		return jndiUser;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJndiUser(java.lang.String)
	 */
	public void setJndiUser(final String username) {
		this.jndiUser = username;
	}

	/**
	 * @return the password for the specified user.
	 */
	String getJndiPassword() {
		return jndiPassword;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJndiPassword(java.lang.String)
	 */
	public void setJndiPassword(final String password) {
		this.jndiPassword = password;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getJdbcUser()
	 */
	public String getJdbcUser() {
		return jdbcUser;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJdbcUser(java.lang.String)
	 */
	public void setJdbcUser(final String username) {
		this.jdbcUser = username;
	}

	/**
	 * @return the password for the specified user.
	 */
	String getJdbcPassword() {
		return jdbcPassword;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#setJdbcPassword(java.lang.String)
	 */
	public void setJdbcPassword(final String password) {
		this.jdbcPassword = password;
	}	
	
	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#getDataSource()
	 */
	public DataSource getDataSource() {
		if (null == datasource) {
			datasource = getJdbcDatasource(jdbcDriver, jdbcUrl);
		}
		return datasource;
	}
	
	public DbAuditPublisherDescriptorImpl() {
		this(DbAuditPublisherImpl.class);
		LOGGER.log(Level.FINE, "init()");
	}
	
	public DbAuditPublisherDescriptorImpl(Class<? extends DbAuditPublisherImpl>clazz) {
		super(clazz);
		load();
	}
	
	@Override
	public boolean configure(final StaplerRequest req, final JSONObject json)
			throws hudson.model.Descriptor.FormException {
		LOGGER.log(Level.FINE, "configure() <- " + json.toString());
		final JSONObject datasourceDetails = json; //.getJSONObject("datasource");
//		this.useJndi = datasourceDetails.getBoolean("value");

		if (this.useJndi) {
			this.jndiName = datasourceDetails.getString("jndiName");
			this.jndiUser = datasourceDetails.getString("jndiUser");
			this.jndiPassword = datasourceDetails.getString("jndiPassword");
		} else {
			this.jdbcDriver = datasourceDetails.getString("jdbcDriver");
			this.jdbcUrl = datasourceDetails.getString("jdbcUrl");
			this.jdbcUser = datasourceDetails.getString("jdbcUser");
			this.jdbcPassword = datasourceDetails.getString("jdbcPassword");
		}
		save();
		return super.configure(req, json);
	}

	@Override
	public String getId() {
		return "audit2db";
	}
	
	@Override
	public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
		// applies to all kinds of project
		return true;
	}

	@Override
	public String getDisplayName() {
		return "Audit job info to Database";//Messages.DbAuditPublisher_DisplayName();
	}

	private DataSource getJdbcDatasource(final String jdbcDriver,
			final String jdbcUrl) {
		datasource = new DriverManagerDataSource(jdbcUrl);
		((DriverManagerDataSource) datasource).setDriverClassName(jdbcDriver);

		return datasource;
	}

	public Connection getJdbcConnection() throws SQLException {
		return getJdbcDatasource(jdbcDriver, jdbcUrl).getConnection(
				jdbcUser, jdbcPassword);
	}
	
	private void dumpClassloaderUrls() {
		LOGGER.log(Level.FINE, "Looking into current context classloader.");
		final URLClassLoader cl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		final URL[] urls = cl.getURLs();
		LOGGER.log(Level.FINE, "Classpath URLs:");
		for (final URL url : urls) {
			LOGGER.log(Level.FINE, url.toString());
		}
	}
	
	/**
	 * @see org.jenkins.plugins.audit2db.internal.DbAuditPublisherDescriptor#doTestJdbcConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public FormValidation doTestJdbcConnection(
			@QueryParameter("audit2db.jdbcDriver") final String jdbcDriver,
			@QueryParameter("audit2db.jdbcUrl") final String jdbcUrl,
			@QueryParameter("audit2db.jdbcUser") final String username,
			@QueryParameter("audit2db.jdbcPassword") final String password)
			throws IOException, ServletException {
		dumpClassloaderUrls();
		LOGGER.log(Level.FINE, String.format(
				"doTestJdbcConnection('%s','%s','%s','*****'",
				jdbcDriver, jdbcUrl, username));
		FormValidation retval = FormValidation.ok("Connection Successful");
		
		try {
			getJdbcDatasource(jdbcDriver, jdbcUrl).getConnection(username, password);
			this.jdbcDriver = jdbcDriver;
			this.jdbcUrl = jdbcUrl;
			this.jdbcUser = username;
			this.jdbcPassword = password;
		} catch (final Exception e) {
			final String msg = String.format(
					"Unable to establish connection: %s", e.getMessage());
			LOGGER.log(Level.SEVERE, msg, e);
			retval = FormValidation.error(msg);
		}

		return retval;
	}
}
