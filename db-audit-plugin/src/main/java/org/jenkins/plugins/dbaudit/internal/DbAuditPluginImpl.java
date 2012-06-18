/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import hudson.Plugin;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import net.sf.json.JSONObject;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.kohsuke.stapler.DataBoundConstructor;
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
public class DbAuditPluginImpl extends Plugin implements DbAuditPlugin, Describable<DbAuditPluginImpl> {
	private final static Logger LOGGER = LoggerFactory.getLogger(DbAuditPluginImpl.class);
	private static DataSource datasource;
	private static ApplicationContext appContext;
	private static AbstractSessionFactoryBean sessionFactory;
	
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
	
	private AbstractSessionFactoryBean getSessionFactory() {
		if ((null == sessionFactory) && (appContext != null)){
			sessionFactory = (AbstractSessionFactoryBean) appContext.getBean("sessionFactory");
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
	public void setJndiDatasource(final String jndiDatasource) {
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
	
	private Connection getJndiConnection(final String jndiName,
			final String username, final String password) {
		Connection retval = null;
		final JndiTemplate jndi = new JndiTemplate();
		try {
			final Object jndiObject = jndi.lookup(jndiName);
			if (!DataSource.class.isAssignableFrom(jndiObject.getClass())) {
				throw new ClassCastException(String.format(
						"JNDI connection is not of the right type: found %s",
						jndiObject.getClass().getName()));
			}
			
			datasource = (DataSource) jndiObject;
			retval = datasource.getConnection(username, password);
		} catch (final Exception e) {
			final String msg = String.format(
					"Unable to retrieve JNDI datasource: %s", 
					e.getMessage());
			LOGGER.error(msg, e);
		}
		
		return retval;
	}
	
	private Connection getJdbcConnection(final String jdbcDriver,
			final String jdbcUrl, final String username, final String password) {
		Connection retval = null;
		
		datasource = new DriverManagerDataSource(jdbcUrl, username, password);
		((DriverManagerDataSource)datasource).setDriverClassName(jdbcDriver);
		
		try {
			retval = datasource.getConnection(username, password);
		} catch (final SQLException e) {
			final String msg = String.format(
					"Unable to create JDBC datasource: %s", 
					e.getMessage());
			LOGGER.error(msg, e);
		}
		
		return retval;
	}
	
	/**
	 * @see DbAuditPlugin#testDatasourceConnection()
	 */
	@Override
	public boolean testDatasourceConnection() {
		final Connection connection;
		if (useJndi) {
			connection = getJndiConnection(jndiDatasource, username, password);
		} else {
			connection = getJdbcConnection(jdbcDriver, jdbcUrl, username, password);
		}

		return (connection != null);
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
		final JSONObject datasourceDetails = formData.getJSONObject("datasource");
		this.username = datasourceDetails.getString("username");
		this.password = datasourceDetails.getString("password");
		this.useJndi = datasourceDetails.getBoolean("value");

		Connection connection = null;
		if (this.useJndi) {
			this.jndiDatasource = datasourceDetails.getString("jndiDatasource");
			connection = getJndiConnection(
					this.jndiDatasource, this.username, this.password);
		} else {
			this.jdbcDriver = datasourceDetails.getString("jdbcDriver");
			this.jdbcUrl = datasourceDetails.getString("jdbcUrl");
			connection = getJdbcConnection(this.jdbcDriver,
					this.jdbcUrl, this.username, this.password);
		}
		getSessionFactory().setDataSource(datasource);
		
		save();
	}
	
	public static Descriptor<DbAuditPluginImpl> getPlugindescriptor() {
		return PluginDescriptor;
	}

	public Descriptor<DbAuditPluginImpl> getDescriptor() {
		return PluginDescriptor;
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		appContext = new ClassPathXmlApplicationContext(
				new String[] {"application-context.xml"});
	}
}
