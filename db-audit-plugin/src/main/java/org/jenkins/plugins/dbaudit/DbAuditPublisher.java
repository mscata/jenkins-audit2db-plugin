/**
 * 
 */
package org.jenkins.plugins.dbaudit;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import javax.sql.DataSource;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditPublisher {
	/**
	 * @return the data source for the audit database.
	 */
	DataSource getDatasource();
	
	/**
	 * @return the plugin descriptor.
	 */
	BuildStepDescriptor<Publisher> getDescriptor();
}
