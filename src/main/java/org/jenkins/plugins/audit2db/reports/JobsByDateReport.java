/**
 * 
 */
package org.jenkins.plugins.audit2db.reports;

import java.util.List;
import java.util.Map;

import org.jenkins.plugins.audit2db.model.BuildDetails;

/**
 * @author Marco Scata
 * 
 */
public interface JobsByDateReport {
    String getJenkinsHostname();

    String getJenkinsIpAddr();

    String getDateGenerated();

    String getStartDate();

    void setStartDate(String date);

    String getEndDate();

    void setEndDate(String date);

    Map<String, List<BuildDetails>> getProjectExecutions();
}
