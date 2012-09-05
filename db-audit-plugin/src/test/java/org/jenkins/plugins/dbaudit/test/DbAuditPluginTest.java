/**
 * 
 */
package org.jenkins.plugins.dbaudit.test;

import hudson.util.FormValidation;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.jenkins.plugins.dbaudit.DbAuditPlugin;
import org.jenkins.plugins.dbaudit.DbAuditPublisher;
import org.jenkins.plugins.dbaudit.DbAuditPublisherDescriptor;
import org.jenkins.plugins.dbaudit.internal.DbAuditPluginImpl;
import org.jenkins.plugins.dbaudit.internal.DbAuditPublisherImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Unit tests for the {@link DbAuditPluginImpl} class.
 * 
 * @author Marco Scata
 *
 */
public class DbAuditPluginTest extends HudsonTestCase {
	private static final String jndiName = "jdbc/dbauditplugin";
	private static final String jdbcDriver = "org.hsqldb.jdbc.JDBCDriver";
	private static final String jdbcUrl = "jdbc:hsqldb:mem:test";
	
//	private DataSource getDatasource() {
//		final DriverManagerDataSource retval = new DriverManagerDataSource();
//		retval.setDriverClassName(jdbcDriver);
//		retval.setUrl(jdbcUrl);
//		return retval;
//	}
	
	@Test
	public void testingValidJdbcDatasourceShouldBeSuccessful() throws Exception {

//		final DbAuditPlugin plugin = new DbAuditPluginImpl(
//				false, null, jdbcDriver, jdbcUrl, "SA", "");
		final DbAuditPublisher publisher = new DbAuditPublisherImpl();
		final DbAuditPublisherDescriptor descriptor = (DbAuditPublisherDescriptor) publisher.getDescriptor();
		
		
		final FormValidation testResult = descriptor.doTestJdbcConnection(
				jdbcDriver, jdbcUrl, "SA", "");
		Assert.assertEquals("Failed connection", FormValidation.Kind.OK, testResult.kind);
	}
	
//	@Test
//	public void testingValidJndiDatasourceShouldBeSuccessful() throws Exception {
//		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
//		contextBuilder.bind(jndiName, getDatasource());
//
//		final DbAuditPlugin plugin = new DbAuditPluginImpl(
//				true, jndiName, null, null, "SA", "");
//		
//		final boolean testResult = plugin.testDatasourceConnection();
//		Assert.assertTrue("Failed connection", testResult);
//	}
//	
//	@Test
//	public void testingInvalidJndiDatasourceShouldFail() throws Exception {
//		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
//		contextBuilder.bind(jndiName, getDatasource());
//
//		final DbAuditPlugin plugin = new DbAuditPluginImpl(
//				true, jndiName + "WRONG", null, null, "SA", "");
//		
//		final boolean testResult = plugin.testDatasourceConnection();
//		Assert.assertFalse("Unexpected successful connection", testResult);
//	}
//	
//	@Test
//	public void testingValidJndiDatasourceWithWrongLoginShouldFail() throws Exception {
//		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
//		contextBuilder.bind(jndiName, getDatasource());
//
//		final DbAuditPlugin plugin = new DbAuditPluginImpl(
//				true, jndiName, null, null, "WRONG", "WRONG");
//		
//		final boolean testResult = plugin.testDatasourceConnection();
//		Assert.assertFalse("Unexpected successful connection", testResult);
//	}
}
