/**
 * 
 */
package org.jenkins.plugins.audit2db.test;

import hudson.util.FormValidation;
import junit.framework.Assert;

import org.jenkins.plugins.audit2db.DbAuditPublisher;
import org.jenkins.plugins.audit2db.DbAuditPublisherDescriptor;
import org.jenkins.plugins.audit2db.internal.DbAuditPublisherImpl;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Unit tests for the {@link DbAuditPluginImpl} class.
 * 
 * @author Marco Scata
 *
 */
public class DbAuditPluginTest extends HudsonTestCase {
//	private static final String jndiName = "jdbc/dbauditplugin";
	private static final String jdbcDriver = "org.hsqldb.jdbc.JDBCDriver";
	private static final String jdbcUrl = "jdbc:hsqldb:mem:test";
	
	
	@Test
	public void testingValidJdbcDatasourceShouldBeSuccessful() throws Exception {
		final DbAuditPublisher publisher = new DbAuditPublisherImpl();
		final DbAuditPublisherDescriptor descriptor = (DbAuditPublisherDescriptor) publisher.getDescriptor();
		
		final FormValidation testResult = descriptor.doTestJdbcConnection(
				jdbcDriver, jdbcUrl, "SA", "");
		Assert.assertEquals("Unexpected connection error.", FormValidation.Kind.OK, testResult.kind);
	}
	
	@Test
	public void testingInvalidJdbcDatasourceShouldFail() throws Exception {
		final DbAuditPublisher publisher = new DbAuditPublisherImpl();
		final DbAuditPublisherDescriptor descriptor = (DbAuditPublisherDescriptor) publisher.getDescriptor();
		
		final FormValidation testResult = descriptor.doTestJdbcConnection(
				jdbcDriver, jdbcUrl, "SA", "wrong-password!!!");
		Assert.assertEquals("Unexpected successful connection.", FormValidation.Kind.ERROR, testResult.kind);
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
