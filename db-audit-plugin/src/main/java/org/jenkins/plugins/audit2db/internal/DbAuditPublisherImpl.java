/**
 * 
 */
package org.jenkins.plugins.audit2db.internal;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;
import org.hibernate.SessionFactory;
import org.jenkins.plugins.audit2db.DbAuditPublisher;
import org.jenkins.plugins.audit2db.DbAuditPublisherDescriptor;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.model.BuildDetailsImpl;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

/**
 * @author Marco Scata
 * 
 */
public class DbAuditPublisherImpl extends Notifier implements DbAuditPublisher {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(DbAuditPublisherImpl.class);
	
	private static ApplicationContext applicationContext;
	
	private static AbstractSessionFactoryBean sessionFactory;

	@DataBoundConstructor
	public DbAuditPublisherImpl() {}
	
	@Extension
	public final static DbAuditPublisherDescriptorImpl descriptor = new DbAuditPublisherDescriptorImpl(DbAuditPublisherImpl.class);
	
	@Override
	public BuildStepDescriptor<Publisher> getDescriptor() {
		LOGGER.debug("Retrieving descriptor");
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
		if (null == sessionFactory) {
			sessionFactory = (AbstractSessionFactoryBean) getAppContext()
					.getBean("sessionFactory");
		}
		return sessionFactory;
	}

	public ApplicationContext getAppContext() { 
		if (null == applicationContext) {
			final String contextFile = getClass().getResource("/application-context.xml").getFile();
			applicationContext = new FileSystemXmlApplicationContext(
					new String[] { contextFile });
		}
		return applicationContext;
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
		listener.getLogger().format("perform: %s; launcher: %s",
				build.getDisplayName(), launcher.toString());
		
		getSessionFactory().setDataSource(
				((DbAuditPublisherDescriptor)getDescriptor()).getDataSource());
		
		final BuildDetailsHibernateRepository repo = new BuildDetailsHibernateRepository(
				(SessionFactory) getSessionFactory());
		
		final BuildDetails details = new BuildDetailsImpl(build);
		final Object id = repo.saveBuildDetails(details);
		
		return (id != null);
	}
	
	@Override
	public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
		listener.getLogger().format("prebuild: %s;",
				build.getDisplayName());
		
		return true;
	}
}
