/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import hudson.Plugin;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

/**
 * Implementation of the {@link DbAuditPlugin} interface.
 * 
 * @author Marco Scata
 * 
 */
public class DbAuditPluginImpl extends Plugin implements DbAuditPlugin,
		Describable<DbAuditPlugin> {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(DbAuditPluginImpl.class);
	private static DataSource datasource;
	private static ApplicationContext appContext;
	private static AbstractSessionFactoryBean sessionFactory;

	private boolean useJndi;
	private String jndiName;

	private String jdbcDriver;
	private String jdbcUrl;

	private String username;
	private String password;

	public static final Descriptor<DbAuditPlugin> PluginDescriptor = 
		new Descriptor<DbAuditPlugin>() {
		public final String DISPLAY_NAME = "Audit to Database Plugin";

		@Override
		public String getDisplayName() {
			return DISPLAY_NAME;
		}
	};

	public DbAuditPluginImpl() {
		super();
	};

	@DataBoundConstructor
	public DbAuditPluginImpl(final boolean useJndi,
			final String jndiName, final String jdbcDriver,
			final String jdbcUrl, final String username, final String password) {
		this();
		this.useJndi = useJndi;
		this.jndiName = jndiName;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	private AbstractSessionFactoryBean getSessionFactory() {
		if ((null == sessionFactory) && (appContext != null)) {
			sessionFactory = (AbstractSessionFactoryBean) appContext
					.getBean("sessionFactory");
		}
		return sessionFactory;
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
	public void setUseJndi(final boolean useJndi) {
		this.useJndi = useJndi;
	}

	/**
	 * @see DbAuditPlugin#getJndiName()
	 */
	@Override
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @see DbAuditPlugin#setJndiName(String)
	 */
	@Override
	public void setJndiName(final String jndiName) {
		this.jndiName = jndiName;
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
	public void setJdbcDriver(final String jdbcDriver) {
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
	public void setJdbcUrl(final String jdbcUrl) {
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
	public void setUsername(final String username) {
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
	public void setPassword(final String password) {
		this.password = password;
	}

	private DataSource getJndiDatasource(final String jndiName) {
		final JndiTemplate jndi = new JndiTemplate();
		try {
			final Object jndiObject = jndi.lookup(jndiName);
			if (!DataSource.class.isAssignableFrom(jndiObject.getClass())) {
				throw new ClassCastException(String.format(
						"JNDI connection is not of the right type: found %s, expected %s",
						jndiObject.getClass().getName(),
						DataSource.class.getName()));
			}

			datasource = (DataSource) jndiObject;
		} catch (final Exception e) {
			final String msg = String.format(
					"Unable to retrieve JNDI datasource: %s", e.getMessage());
			LOGGER.error(msg, e);
			throw new RuntimeException(e);
		}

		return datasource;
	}

	private DataSource getJdbcDatasource(final String jdbcDriver,
			final String jdbcUrl) {
		datasource = new DriverManagerDataSource(jdbcUrl);
		((DriverManagerDataSource) datasource).setDriverClassName(jdbcDriver);

		return datasource;
	}

	/**
	 * @see DbAuditPlugin#testDatasourceConnection()
	 */
	@Override
	public boolean testDatasourceConnection() {
		FormValidation result = FormValidation.error("");
		try {
			if (useJndi) {
				result = doTestJndiConnection(jndiName, username, password);
			} else {
				result = doTestJdbcConnection(jdbcDriver, jdbcUrl, username, password);
			}
		} catch (final Exception e) {
			// do nothing -> result is failure
		}
		return result.kind.equals(FormValidation.Kind.OK);
	}

	/**
	 * @see DbAuditPlugin#getDatasource()
	 */
	@Override
	public DataSource getDatasource() {
		return datasource;
	}

	@Override
	public void configure(final StaplerRequest req, final JSONObject formData)
			throws IOException, ServletException, FormException {
		super.configure(req, formData);
		this.username = formData.getString("username");
		this.password = formData.getString("password");

		final JSONObject datasourceDetails = formData.getJSONObject("datasource");
		this.useJndi = datasourceDetails.getBoolean("value");

		if (this.useJndi) {
			try {
				this.jndiName = datasourceDetails.getString("jndiName");
//					datasource = getJndiDatasource(this.jndiName);
			} catch (final Exception e) {
				throw new FormException(e.getMessage(), "jndiName");
			}
		} else {
			try {
				this.jdbcDriver = datasourceDetails.getString("jdbcDriver");
				this.jdbcUrl = datasourceDetails.getString("jdbcUrl");
//					datasource = getJdbcDatasource(this.jdbcDriver, this.jdbcUrl);
			} catch (final Exception e) {
				throw new FormException(e.getMessage(), e, "jdbcDriver");
			}
		}
		save();
	}

	public static Descriptor<DbAuditPlugin> getPlugindescriptor() {
		return PluginDescriptor;
	}

	public Descriptor<DbAuditPlugin> getDescriptor() {
		return PluginDescriptor;
	}

	@Override
	public void start() throws Exception {
		super.start();
		appContext = new ClassPathXmlApplicationContext(
				new String[] { "application-context.xml" });
	}

	public FormValidation doTestJndiConnection(
			@QueryParameter("jndiName") final String jndiName,
			@QueryParameter("username") final String username,
			@QueryParameter("password") final String password)
			throws IOException, ServletException {
		
		FormValidation retval = FormValidation.ok("Connection Successful");
		
		try {
			getJndiDatasource(jndiName).getConnection(username, password);
		} catch (final Exception e) {
			final String msg = String.format(
					"Unable to establish connection: %s", e.getMessage());
			LOGGER.error(msg, e);
			retval = FormValidation.error(msg);
		}

		return retval;
	}

	public FormValidation doTestJdbcConnection(
			@QueryParameter("jdbcDriver") final String jdbcDriver,
			@QueryParameter("jdbcUrl") final String jdbcUrl,
			@QueryParameter("username") final String username,
			@QueryParameter("password") final String password)
			throws IOException, ServletException {
		
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
