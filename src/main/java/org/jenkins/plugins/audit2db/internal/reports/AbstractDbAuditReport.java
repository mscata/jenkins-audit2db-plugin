package org.jenkins.plugins.audit2db.internal.reports;

import hudson.model.Descriptor;
import jenkins.model.Jenkins;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;
import org.jenkins.plugins.audit2db.internal.DbAuditPublisherImpl;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.reports.DbAuditReport;

public abstract class AbstractDbAuditReport implements DbAuditReport {
    private transient BuildDetailsRepository repository;

    public AbstractDbAuditReport() {
	super();
    }

    @Override
    public String getIconFileName() {
	return "document.gif";
    }

    @Override
    public BuildDetailsRepository getRepository() {
	if (null == repository) {
	    // DbAuditPublisherImpl.descriptor.load();
	    repository = new BuildDetailsHibernateRepository(
		    DbAuditPublisherImpl.getSessionFactory());
	}
	return repository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Descriptor<DbAuditReport> getDescriptor() {
	return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
}