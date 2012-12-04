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
    String getStartDateParam(String dateString);

    String getEndDateParam(String dateString);

    Map<String, List<BuildDetails>> getProjectExecutions(
	    String startDate, String endDate);
}
