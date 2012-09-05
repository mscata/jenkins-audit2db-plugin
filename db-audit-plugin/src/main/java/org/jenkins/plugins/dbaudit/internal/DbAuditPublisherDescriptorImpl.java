/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.jenkins.plugins.dbaudit.DbAuditPublisherDescriptor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

/**
 * @author Marco Scata
 *
 */
//@Extension
public class DbAuditPublisherDescriptorImpl extends
		BuildStepDescriptor<Publisher> implements DbAuditPublisherDescriptor {
	private final static Logger LOGGER = LoggerFactory.getLogger(DbAuditPublisherDescriptorImpl.class);

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
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getUseJndi()
	 */
	public boolean getUseJndi() {
		return useJndi;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setUseJndi(boolean)
	 */
	public void setUseJndi(final boolean useJndi) {
		this.useJndi = useJndi;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getJndiName()
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJndiName(java.lang.String)
	 */
	public void setJndiName(final String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getJdbcDriver()
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJdbcDriver(java.lang.String)
	 */
	public void setJdbcDriver(final String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getJdbcUrl()
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJdbcUrl(java.lang.String)
	 */
	public void setJdbcUrl(final String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getJndiUser()
	 */
	public String getJndiUser() {
		return jndiUser;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJndiUser(java.lang.String)
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
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJndiPassword(java.lang.String)
	 */
	public void setJndiPassword(final String password) {
		this.jndiPassword = password;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getJdbcUser()
	 */
	public String getJdbcUser() {
		return jdbcUser;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJdbcUser(java.lang.String)
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
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#setJdbcPassword(java.lang.String)
	 */
	public void setJdbcPassword(final String password) {
		this.jdbcPassword = password;
	}	
	
	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#getDataSource()
	 */
	public DataSource getDataSource() {
		if (null == datasource) {
			datasource = getJdbcDatasource(jdbcDriver, jdbcUrl);
		}
		return datasource;
	}
	
	public DbAuditPublisherDescriptorImpl() {
		this(DbAuditPublisherImpl.class);
		LOGGER.debug("init()");
	}
	
	public DbAuditPublisherDescriptorImpl(Class<? extends DbAuditPublisherImpl>clazz) {
		super(clazz);
		load();
	}
	
	@Override
	public boolean configure(final StaplerRequest req, final JSONObject json)
			throws hudson.model.Descriptor.FormException {
		LOGGER.debug("configure() <- " + json.toString());
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
		return "jenkins.dbAuditPlugin";
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

	/**
	 * @see org.jenkins.plugins.dbaudit.internal.DbAuditPublisherDescriptor#doTestJdbcConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public FormValidation doTestJdbcConnection(
			@QueryParameter("dbaudit.jdbcDriver") final String jdbcDriver,
			@QueryParameter("dbaudit.jdbcUrl") final String jdbcUrl,
			@QueryParameter("dbaudit.jdbcUser") final String username,
			@QueryParameter("dbaudit.jdbcPassword") final String password)
			throws IOException, ServletException {
		LOGGER.debug(String.format("doTestJdbcConnection('%s','%s','%s','*****'",
				jdbcDriver, jdbcUrl, username));
		FormValidation retval = FormValidation.ok("Connection Successful");
		
		try {
			getJdbcDatasource(jdbcDriver, jdbcUrl).getConnection(username, password);
		} catch (final Exception e) {
			final String msg = String.format(
					"Unable to establish connection: %s", e.getMessage());
			LOGGER.error(msg, e);
			retval = FormValidation.error(msg);
		}

		return retval;
	}
}
