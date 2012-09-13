package org.jenkins.plugins.audit2db;

import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.QueryParameter;

public interface DbAuditPublisherDescriptor {

    /**
     * @see DbAuditPlugin#isUseJndi()
     */
    boolean getUseJndi();

    /**
     * @see DbAuditPlugin#setUseJndi(boolean)
     */
    void setUseJndi(final boolean useJndi);

    /**
     * @see DbAuditPlugin#getJndiName()
     */
    String getJndiName();

    /**
     * @see DbAuditPlugin#setJndiName(String)
     */
    void setJndiName(final String jndiName);

    /**
     * @see DbAuditPlugin#getJdbcDriver()
     */
    String getJdbcDriver();

    /**
     * @see DbAuditPlugin#setJdbcDriver(String)
     */
    void setJdbcDriver(final String jdbcDriver);

    /**
     * @see DbAuditPlugin#getJdbcUrl()
     */
    String getJdbcUrl();

    /**
     * @see DbAuditPlugin#setJdbcUrl(String)
     */
    void setJdbcUrl(final String jdbcUrl);

    /**
     * @see DbAuditPlugin#getUsername()
     */
    String getJndiUser();

    /**
     * @see DbAuditPlugin#setUsername(String)
     */
    void setJndiUser(final String username);

    /**
     * @see DbAuditPlugin#setPassword(String)
     */
    void setJndiPassword(final String password);

    /**
     * @see DbAuditPlugin#getUsername()
     */
    String getJdbcUser();

    /**
     * @see DbAuditPlugin#setUsername(String)
     */
    void setJdbcUser(final String username);

    /**
     * @see DbAuditPlugin#setPassword(String)
     */
    void setJdbcPassword(final String password);

    FormValidation doTestJdbcConnection(
            @QueryParameter("audit2db.jdbcDriver") final String jdbcDriver,
            @QueryParameter("audit2db.jdbcUrl") final String jdbcUrl,
            @QueryParameter("audit2db.jdbcUser") final String username,
            @QueryParameter("audit2db.jdbcPassword") final String password)
    throws IOException, ServletException;
}