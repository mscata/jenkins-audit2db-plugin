/**
 * 
 */
package org.jenkins.plugins.audit2db.test.integration;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jenkins.plugins.audit2db.test.integration.webpages.JobsByDateReportPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * @author Marco Scata
 *
 */
public class WhenRunningJobsByDateReport extends HudsonTestCase {
    private JobsByDateReportPage page;

    @Before
    @Override
    public void setUp() throws Exception {
	super.setUp();
	page = new JobsByDateReportPage(createWebClient());
    }

    @After
    @Override
    public void tearDown() throws Exception {
	page.unload();
	super.tearDown();
    }

    @Test
    public void testShouldDisplayDefaultDateRangeForRequestWithoutParams() {
	final Calendar expectedStartDate = Calendar.getInstance();
	expectedStartDate.set(Calendar.DAY_OF_MONTH, 1);
	expectedStartDate.set(Calendar.HOUR_OF_DAY, 0);
	expectedStartDate.set(Calendar.MINUTE, 0);
	expectedStartDate.set(Calendar.SECOND, 0);
	expectedStartDate.set(Calendar.MILLISECOND, 0);

	final Calendar expectedEndDate = Calendar.getInstance();
	expectedEndDate.set(Calendar.HOUR_OF_DAY, 23);
	expectedEndDate.set(Calendar.MINUTE, 59);
	expectedEndDate.set(Calendar.SECOND, 59);
	expectedEndDate.set(Calendar.MILLISECOND, 999);

	final SimpleDateFormat DATE_FORMAT_NOTIME = new SimpleDateFormat(
	"yyyy-MM-dd");

	try {
	    page.load();
	    WebAssert.assertInputContainsValue(page.getPage(), "startDate",
		    DATE_FORMAT_NOTIME.format(expectedStartDate.getTime()));
	    WebAssert.assertInputContainsValue(page.getPage(), "endDate",
		    DATE_FORMAT_NOTIME.format(expectedEndDate.getTime()));
	} catch (final Exception e) {
	    // expecting successful access
	    fail("Unexpected failed access. Auditors should have valid permissions.");
	}
    }
}
