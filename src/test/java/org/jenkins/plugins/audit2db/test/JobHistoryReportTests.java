/**
 *
 */
package org.jenkins.plugins.audit2db.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;
import org.jenkins.plugins.audit2db.internal.DbAuditUtil;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.HibernateUtil;
import org.jenkins.plugins.audit2db.internal.reports.JobHistoryReportImpl;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.reports.JobHistoryReport;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marco Scata
 *
 */
public class JobHistoryReportTests {
    private final BuildDetailsRepository repository = new BuildDetailsHibernateRepository(
	    HibernateUtil.getSessionFactory(HibernateUtil.getExtraProperties(
		    "org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:mem:test", "SA",
	    "")));

    private final String now;
    private final String yesterday;
    private final String tomorrow;

    public JobHistoryReportTests() {
	final Calendar cal = Calendar.getInstance();

	cal.add(Calendar.DAY_OF_MONTH, -1);
	yesterday = DATE_FORMAT_NOTIME.format(cal.getTime());

	cal.add(Calendar.DAY_OF_MONTH, 1);
	now = DATE_FORMAT_NOTIME.format(cal.getTime());

	cal.add(Calendar.DAY_OF_MONTH, 1);
	tomorrow = DATE_FORMAT_NOTIME.format(cal.getTime());
    }

    private final SimpleDateFormat DATE_FORMAT_NOTIME = new SimpleDateFormat(
	"yyyy-MM-dd");

    @Test
    public void matchingParametersShouldReturnNonEmptyResults() {
	final JobHistoryReport report = new JobHistoryReportImpl();
	report.setRepository(repository);

	final Map<String, List<BuildDetails>> dataset = RepositoryTests
		.createRandomDataset(DbAuditUtil.getHostName());
	// no need to use transactions because the mem db will be dumped
	// after each test run
	for (final List<BuildDetails> detailsList : dataset.values()) {
	    repository.saveBuildDetailsList(detailsList);
	}

	final String projectName = dataset.keySet().iterator().next();
	final Map<String, List<BuildDetails>> results = report
		.getProjectExecutions(projectName, now, now);

	Assert.assertFalse("Unexpected empty results", results.isEmpty());
    }
}
