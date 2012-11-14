/**
 * 
 */
package org.jenkins.plugins.audit2db.reports;

import hudson.ExtensionPoint;
import hudson.model.Action;
import hudson.model.Describable;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;

/**
 * @author Marco Scata
 *
 */
public interface DbAuditReport extends ExtensionPoint, Action,
Describable<DbAuditReport> {
    /**
     * @return a generic explanation about the report's layout and objectives.
     */
    String getReportDescription();

    /**
     * @return a list of information parts exposed by ths report.
     */
    String getReportDisplayedInfo();

    /**
     * @return the build details repository.
     */
    BuildDetailsRepository getRepository();
}
