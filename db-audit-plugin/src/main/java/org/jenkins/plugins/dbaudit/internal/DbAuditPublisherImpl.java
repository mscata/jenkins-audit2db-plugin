/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal;

import java.io.IOException;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import jenkins.model.Jenkins;

import net.sf.json.JSONObject;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.jenkins.plugins.dbaudit.DbAuditPublisher;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

/**
 * @author Marco Scata
 * 
 */
public class DbAuditPublisherImpl extends Notifier implements DbAuditPublisher {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(DbAuditPublisherImpl.class);
	
	private static ApplicationContext appContext;
	private static AbstractSessionFactoryBean sessionFactory;

	@Extension
	public final static DbAuditPublisherDescriptorImpl descriptor = new DbAuditPublisherDescriptorImpl(DbAuditPublisherImpl.class);
	
	public static ApplicationContext getAppContext() {
		return appContext;
	}
	
	@Override
	public BuildStepDescriptor<Publisher> getDescriptor() {
		LOGGER.debug("Retrieving descriptor");
//		return Jenkins.getInstance().getDescriptorByType(DbAuditPublisherDescriptorImpl.class);
//		return (DbAuditPublisherDescriptorImpl) super.getDescriptor();
		return descriptor;
	}
	
	@Override
	public boolean needsToRunAfterFinalized() {
		// run even after the build is marked as complete
		return true;
	}
	
	/**
	 * @see hudson.tasks.BuildStep#getRequiredMonitorService()
	 */
	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	private AbstractSessionFactoryBean getSessionFactory() {
		if ((null == sessionFactory) && (appContext != null)) {
			sessionFactory = (AbstractSessionFactoryBean) appContext
					.getBean("sessionFactory");
		}
		return sessionFactory;
	}

	@DataBoundConstructor
	public DbAuditPublisherImpl() { 
//		final String contextFile = getClass().getResource("/application-context.xml").getFile();
//		appContext = new FileSystemXmlApplicationContext(
//				new String[] { contextFile });
	}
	
	/**
	 * @see DbAuditPublisher#getDatasource()
	 */
	public DataSource getDatasource() {
		return null; //descriptor.getDataSource();
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().format("build: %s; launcher: %s",
				build.getDisplayName(), launcher.toString());
		return true;
	}
	
	/*
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

	@Override
	public boolean configure(final StaplerRequest req, final JSONObject formData)
			throws FormException {
		super.configure(req, formData);

		final JSONObject datasourceDetails = formData.getJSONObject("datasource");
		this.useJndi = datasourceDetails.getBoolean("value");

		if (this.useJndi) {
			try {
				this.jndiName = datasourceDetails.getString("jndiName");
				this.username = datasourceDetails.getString("jndiUser");
				this.password = datasourceDetails.getString("jndiPassword");
			} catch (final Exception e) {
				throw new FormException(e.getMessage(), e, "jndiName");
			}
		} else {
			try {
				this.jdbcDriver = datasourceDetails.getString("jdbcDriver");
				this.jdbcUrl = datasourceDetails.getString("jdbcUrl");
				this.username = datasourceDetails.getString("jdbcUser");
				this.password = datasourceDetails.getString("jdbcPassword");
			} catch (final Exception e) {
				throw new FormException(e.getMessage(), e, "jdbcDriver");
			}
		}
		save();
		return super.configure(req, formData);
	}
	*/
}
