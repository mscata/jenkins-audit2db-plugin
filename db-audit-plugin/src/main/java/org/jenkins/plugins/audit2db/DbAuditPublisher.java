/**
 * 
 */
package org.jenkins.plugins.audit2db;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import javax.sql.DataSource;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditPublisher {
	/**
	 * @return the plugin descriptor.
	 */
	BuildStepDescriptor<Publisher> getDescriptor();
}
