/**
 * 
 */
package org.jenkins.plugins.audit2db.test.integration.webpages;

import org.jvnet.hudson.test.HudsonTestCase.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * @author Marco Scata
 *
 */
public class JobsByDateReportPage extends AbstractJenkinsPage {
    private final static String urlPath = "audit2db.reports/jobsByDate";
    private HtmlForm reportFilter;

    public JobsByDateReportPage(final WebClient client) {
	super(client, urlPath);
    }

    @Override
    public void load() {
	super.load();
	reportFilter = getPage().getFormByName("reportFilter");
    }

    public String getStartDate() {
	return getInputValue(reportFilter, "startDate");
    }

    public void setStartDate(final String startDate) {
	setInputValue(reportFilter, "startDate", startDate);
    }
}
