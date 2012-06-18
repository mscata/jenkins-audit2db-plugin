/**
 * 
 */
package org.jenkins.plugins.dbaudit;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.jenkins.plugins.dbaudit.internal.DbAuditPluginImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit tests for the {@link DbAuditPluginImpl} class.
 * 
 * @author Marco Scata
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/application-context.xml")
public class DbAuditPluginTest {
	private static final String jndiName = "jdbc/dbauditplugin";
	private static final String jdbcDriver = "org.hsqldb.jdbc.JDBCDriver";
	private static final String jdbcUrl = "jdbc:hsqldb:mem:test";
	
	private DataSource getDatasource() {
		final DriverManagerDataSource retval = new DriverManagerDataSource();
		retval.setDriverClassName(jdbcDriver);
		retval.setUrl(jdbcUrl);
		return retval;
	}
	
	@Test
	public void testingValidJdbcDatasourceShouldBeSuccessful() throws Exception {

		final DbAuditPlugin plugin = new DbAuditPluginImpl(
				false, null, jdbcDriver, jdbcUrl, "SA", "");
		
		final boolean testResult = plugin.testDatasourceConnection();
		Assert.assertTrue("Failed connection", testResult);
	}
	
	@Test
	public void testingValidJndiDatasourceShouldBeSuccessful() throws Exception {
		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		contextBuilder.bind(jndiName, getDatasource());

		final DbAuditPlugin plugin = new DbAuditPluginImpl(
				true, jndiName, null, null, "SA", "");
		
		final boolean testResult = plugin.testDatasourceConnection();
		Assert.assertTrue("Failed connection", testResult);
	}
	
	@Test
	public void testingInvalidJndiDatasourceShouldFail() throws Exception {
		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		contextBuilder.bind(jndiName, getDatasource());

		final DbAuditPlugin plugin = new DbAuditPluginImpl(
				true, jndiName + "WRONG", null, null, "SA", "");
		
		final boolean testResult = plugin.testDatasourceConnection();
		Assert.assertFalse("Unexpected successful connection", testResult);
	}
	
	@Test
	public void testingValidJndiDatasourceWithWrongLoginShouldFail() throws Exception {
		final SimpleNamingContextBuilder contextBuilder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		contextBuilder.bind(jndiName, getDatasource());

		final DbAuditPlugin plugin = new DbAuditPluginImpl(
				true, jndiName, null, null, "WRONG", "WRONG");
		
		final boolean testResult = plugin.testDatasourceConnection();
		Assert.assertFalse("Unexpected successful connection", testResult);
	}
}
