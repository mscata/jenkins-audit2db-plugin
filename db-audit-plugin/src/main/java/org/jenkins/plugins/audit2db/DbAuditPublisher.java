/**
 * 
 */
package org.jenkins.plugins.audit2db;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

/**
 * Interface for the plugin publisher.
 * 
 * @author Marco Scata
 *
 */
public interface DbAuditPublisher {
	/**
	 * @return the plugin descriptor.
	 */
	BuildStepDescriptor<Publisher> getDescriptor();
}
