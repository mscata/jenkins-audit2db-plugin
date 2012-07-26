/**
 * 
 */
package org.jenkins.plugins.dbaudit.test.htmlunit;

import junit.framework.Assert;

import org.jenkins.plugins.dbaudit.test.htmlunit.webpages.JenkinsConfigurationPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jvnet.hudson.test.HudsonTestCase;

/**
 * System tests. 
 * Plugin configuration.
 * 
 * NOTE: the names of test methods for classes extending HudsonTestCase
 * must begin with the word 'test'.
 * 
 * @author Marco Scata
 *
 */
public class WhenConfiguringPlugin extends HudsonTestCase {
	private static JenkinsConfigurationPage page;
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		page = new JenkinsConfigurationPage(this.getURL().toString());
		page.load();
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		page.unload();
		super.tearDown();
	}
	
	@Test
	public void testShouldSaveJndiDatasourceDetails() {
		final String datasourceName = "MyJndiDatasource";
		final String user = "MyJndiUser";
		final String password = "MyJndiPassword";
		
		page.setUseJndi(true);
		page.setDatasourceName(datasourceName);
		page.setUser(user);
		page.setPassword(password);
		page.saveChanges();
		page.load();
		
		Assert.assertTrue("The useJndi flag was not set to true.", page.isUseJndi());
		Assert.assertEquals("Mismatched datasource name", datasourceName, page.getDatasourceName());
		Assert.assertEquals("Mismatched user", user, page.getUser());
		Assert.assertTrue("Mismatched password", page.getPassword().isEmpty());
	}
	
	@Test
	public void testShouldSaveJdbcDatasourceDetails() {
		final String jdbcDriver = "MyJdbcDriver";
		final String jdbcUrl = "MyJdbcUrl";
		final String user = "MyJdbcUser";
		final String password = "MyJdbcPassword";

		page.setUseJndi(false);
		page.setJdbcDriver(jdbcDriver);
		page.setJdbcUrl(jdbcUrl);
		page.setUser(user);
		page.setPassword(password);
		page.saveChanges();
		page.load();
		
		Assert.assertFalse("The useJndi flag was not set to false.", page.isUseJndi());
		Assert.assertEquals("Mismatched driver class", jdbcDriver, page.getJdbcDriver());
		Assert.assertEquals("Mismatched jdbc url", jdbcUrl, page.getJdbcUrl());
		Assert.assertEquals("Mismatched user", user, page.getUser());
		Assert.assertTrue("Mismatched password", page.getPassword().isEmpty());
	}
}
