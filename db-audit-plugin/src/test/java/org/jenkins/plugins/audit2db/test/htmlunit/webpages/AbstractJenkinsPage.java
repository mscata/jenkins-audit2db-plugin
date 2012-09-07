/**
 * 
 */
package org.jenkins.plugins.audit2db.test.htmlunit.webpages;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Marco Scata
 *
 */
public abstract class AbstractJenkinsPage {
	private final WebClient webClient = new WebClient();
	
	private final String baseUrl;
	private final String urlPath;
	private HtmlPage page;
	
	public HtmlPage getPage() {
		return page;
	}
	
	public AbstractJenkinsPage(final String baseUrl, final String urlPath) {
		if ((null == baseUrl) || baseUrl.isEmpty()) {
			this.baseUrl = "http://localhost";
		} else {
			this.baseUrl = baseUrl;
		}

		if ((null == urlPath) || urlPath.isEmpty()) {
			throw new IllegalArgumentException("Page URL path must be provided");
		}
		
		// add initial slash if not present
		if (baseUrl.endsWith("/") || urlPath.startsWith("/")) {
			this.urlPath = this.baseUrl + urlPath;
		} else {
			this.urlPath = this.baseUrl + "/" + urlPath;
		}
	}
	
	public void load() {
		try {
			page = webClient.getPage(urlPath);
		} catch (final IOException e) {
			throw new IllegalArgumentException(
					"Error loading page", e);
		}
	}
	
	public void unload() {
		if (webClient != null) {
			webClient.closeAllWindows();
		}
	}
}
