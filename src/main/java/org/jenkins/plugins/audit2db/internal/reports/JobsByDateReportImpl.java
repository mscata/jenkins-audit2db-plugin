/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.reports;

import hudson.Extension;
import hudson.model.Descriptor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.jenkins.plugins.audit2db.Messages;
import org.jenkins.plugins.audit2db.internal.DbAuditUtil;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.reports.DbAuditReport;
import org.jenkins.plugins.audit2db.reports.JobsByDateReport;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * @author Marco Scata
 * 
 */
@Extension
public class JobsByDateReportImpl extends AbstractDbAuditReport implements JobsByDateReport {
    private final static transient SimpleDateFormat DATE_FORMAT_NOTIME = new SimpleDateFormat(
    "yyyy-MM-dd");
    private final static transient SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss");
    private final static transient Logger LOGGER = Logger
    .getLogger(JobsByDateReportImpl.class.getName());

    @Extension
    public static final class DescriptorImpl extends Descriptor<DbAuditReport> {
	@Override
	public String getDisplayName() {
	    return "Jobs By Date";
	}
    }

    private Date startDate;
    private Date endDate;

    public JobsByDateReportImpl() {
	final Calendar cal = Calendar.getInstance();
	// end date = tonight
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);
	endDate = cal.getTime();

	// start date = first day of this month
	cal.set(Calendar.DAY_OF_MONTH, 1);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	startDate = cal.getTime();
    }

    @Override
    public String getJenkinsHostname() {
	return DbAuditUtil.getHostName();
    }

    @Override
    public String getJenkinsIpAddr() {
	return DbAuditUtil.getIpAddress();
    }

    @Override
    public String getDateGenerated() {
	return DATE_FORMAT.format(new Date());
    }

    @Override
    public String getStartDate() {
	return DATE_FORMAT_NOTIME.format(startDate);
    }

    @Override
    public void setStartDate(final String date) {
	try {
	    this.startDate = DATE_FORMAT_NOTIME.parse(date);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String getEndDate() {
	return DATE_FORMAT_NOTIME.format(endDate);
    }

    @Override
    public void setEndDate(final String date) {
	try {
	    this.endDate = DATE_FORMAT_NOTIME.parse(date);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public Map<String, List<BuildDetails>> getProjectExecutions() {
	final Map<String, List<BuildDetails>> retval = new HashMap<String, List<BuildDetails>>();

	final String jenkinsHost = getJenkinsHostname();
	final List<String> projectNames = getRepository().getProjectNames(
		jenkinsHost, startDate, endDate);
	for (final String projectName : projectNames) {
	    final List<BuildDetails> buildDetails = getRepository()
	    .getBuildDetails(
		    jenkinsHost, projectName, startDate, endDate);
	    if (!buildDetails.isEmpty()) {
		retval.put(projectName, buildDetails);
	    }
	}

	return retval;
    }

    @Override
    public String getDisplayName() {
	return Messages.DbAuditReportsJobsByDate_DisplayName();
    }

    @Override
    public String getUrlName() {
	return "jobsByDate";
    }

    /**
     * Called by Stapler as default view.
     * 
     * @param request
     *            the Stapler request object.
     * @param response
     *            the Stapler response object.
     * @throws IOException
     * @throws ServletException
     */
    public void doFetchData(final StaplerRequest request,
	    final StaplerResponse response) throws ServletException,
	    IOException {
	final String startDateStr = request.getParameter("startDate");
	final String endDateStr = request.getParameter("endDate");
	LOGGER.log(Level.FINEST, String.format("-> doIndex('%s','%s')",
		startDateStr, endDateStr));
	if ((startDate != null) && (endDate != null)) {
	    try {
		startDate = DATE_FORMAT_NOTIME.parse(startDateStr);
		endDate = DATE_FORMAT_NOTIME.parse(endDateStr);
		// getProjectExecutions();
	    } catch (final ParseException e) {
		LOGGER.log(Level.WARNING, String.format(
			"Unable to parse date range from [%s] to [%s]",
			startDateStr, endDateStr));
		LOGGER.log(Level.FINE, e.getMessage(), e);
	    }
	}
	response.forwardToPreviousPage(request);
    }

    @Override
    public String getReportDescription() {
	return Messages.DbAuditReportsJobsByDate_ReportDescription();
    }

    @Override
    public String getReportInfo() {
	return Messages.DbAuditReportsJobsByDate_ReportInfo();
    }
}
