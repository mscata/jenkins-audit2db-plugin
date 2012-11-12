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

import jenkins.model.Jenkins;

import org.jenkins.plugins.audit2db.Messages;
import org.jenkins.plugins.audit2db.internal.DbAuditPlugin;
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

    private Date getDefaultStartDate() {
	final Calendar cal = Calendar.getInstance();
	// start date = first day of this month
	cal.set(Calendar.DAY_OF_MONTH, 1);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);

	return cal.getTime();
    }

    private Date getDefaultEndDate() {
	final Calendar cal = Calendar.getInstance();
	// end date = tonight
	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);

	return cal.getTime();
    }

    /**
     * @param dateString
     *            a valid date string.
     * @return the equivalent {@link Date} object, or <code>null</code> if the
     *         date string cannot be parsed.
     */
    private Date stringToDate(final String dateString) {
	Date retval = null;
	if ((dateString != null) && !dateString.isEmpty()) {
	    try {
		retval = DATE_FORMAT_NOTIME.parse(dateString);
	    } catch (final ParseException e) {
		LOGGER.log(Level.WARNING, "Unable to parse date string "
			+ dateString);
	    }
	}
	return retval;
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
    public String getStartDateParam(final String dateString) {
	Date date = stringToDate(dateString);
	if (null == date) {
	    date = getDefaultStartDate();
	}
	return DATE_FORMAT_NOTIME.format(date);
    }

    @Override
    public String getEndDateParam(final String dateString) {
	Date date = stringToDate(dateString);
	if (null == date) {
	    date = getDefaultEndDate();
	}
	return DATE_FORMAT_NOTIME.format(date);
    }

    @Override
    public Map<String, List<BuildDetails>> getProjectExecutions(
	    final String startDateString, final String endDateString) {
	Jenkins.getInstance().checkPermission(DbAuditPlugin.RUN);
	final Map<String, List<BuildDetails>> retval = new HashMap<String, List<BuildDetails>>();
	final Date startDate = stringToDate(startDateString);
	final Date endDate = stringToDate(endDateString);
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
	return Messages.DbAuditReportsJobsByDate_ReportTitle();
    }

    @Override
    public String getUrlName() {
	return "jobsByDate";
    }

    /**
     * Applies the filter selected by the user through the UI.
     * 
     * @param request
     *            the Stapler request object.
     * @param response
     *            the Stapler response object.
     * @throws IOException
     * @throws ServletException
     */
    public void doApplyFilter(final StaplerRequest request,
	    final StaplerResponse response) throws ServletException,
	    IOException {
	Jenkins.getInstance().checkPermission(DbAuditPlugin.RUN);
	final String startDateStr = request.getParameter("startDate");
	final String endDateStr = request.getParameter("endDate");
	LOGGER.log(Level.FINEST, String.format("-> doApplyFilter('%s','%s')",
		startDateStr, endDateStr));
	// if ((startDate != null) && (endDate != null)) {
	// try {
	// startDate = DATE_FORMAT_NOTIME.parse(startDateStr);
	// endDate = DATE_FORMAT_NOTIME.parse(endDateStr);
	// // getProjectExecutions();
	// } catch (final ParseException e) {
	// LOGGER.log(Level.WARNING, String.format(
	// "Unable to parse date range from [%s] to [%s]",
	// startDateStr, endDateStr));
	// LOGGER.log(Level.FINE, e.getMessage(), e);
	// }
	// }
	// response.forwardToPreviousPage(request);
    }

    @Override
    public String getReportDescription() {
	return Messages.DbAuditReportsJobsByDate_ReportDescription();
    }

    @Override
    public String getReportDisplayedInfo() {
	return Messages.DbAuditReportsJobsByDate_ReportDisplayedInfo();
    }
}
