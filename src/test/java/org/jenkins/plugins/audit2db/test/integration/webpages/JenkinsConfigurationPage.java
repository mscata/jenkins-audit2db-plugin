/**
 * 
 */
package org.jenkins.plugins.audit2db.test.integration.webpages;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

/**
 * @author Marco Scata
 *
 */
public class JenkinsConfigurationPage extends AbstractJenkinsPage {
    private final static String urlPath = "/configure";
    private HtmlForm configForm;

    public JenkinsConfigurationPage(final String baseUrl) {
        super(baseUrl, urlPath);
    }

    @Override
    public void load() {
        super.load();
        configForm = getPage().getFormByName("config");
    }

    public String getConfigValue(final String inputName) {
        String retval = null;
        final HtmlInput input = configForm.getInputByName(inputName);
        if (null == input) {
            throw new RuntimeException(
                    String.format("Input '%s' cannot be found", inputName));
        } else {
            retval = input.getValueAttribute();
        }

        return retval;
    }

    public void setConfigValue(final String inputName, final String value) {
        final HtmlInput input = configForm.getInputByName(inputName);
        input.setValueAttribute(value);
    }

    public HtmlElement getJndiDatasourceRadioButton() {
        HtmlElement retval = null;
        final List<HtmlElement> elements = getPage().getElementsByName("audit2db.datasource");
        for (final HtmlElement element : elements) {
            if (element.getAttribute("value").equalsIgnoreCase("true")) {
                retval = element;
                break;
            }
        }
        return retval;
    }

    public HtmlElement getJdbcDatasourceRadioButton() {
        HtmlElement retval = null;
        final List<HtmlElement> elements = getPage().getElementsByName("audit2db.datasource");
        for (final HtmlElement element : elements) {
            if (element.getAttribute("value").equalsIgnoreCase("false")) {
                retval = element;
                break;
            }
        }
        return retval;
    }

    public boolean isUseJndi() {
        final HtmlElement element = getJndiDatasourceRadioButton();
        final String checked = element.getAttribute("checked");
        return ((checked != null) && (!checked.isEmpty()));
    }

    public void setUseJndi(final boolean useJndi) {
        try {
            if (useJndi) {
                getJndiDatasourceRadioButton().click();
            } else {
                getJdbcDatasourceRadioButton().click();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJndiDatasource() {
        return getConfigValue("audit2db.jndiName");
    }

    public void setJndiDatasource(final String datasourceName) {
        setConfigValue("audit2db.jndiName", datasourceName);
    }


    public String getJndiUser() {
        return getConfigValue("audit2db.jndiUser");
    }

    public void setJndiUser(final String user) {
        setConfigValue("audit2db.jndiUser", user);
    }

    public String getJndiPassword() {
        return getConfigValue("audit2db.jndiPassword");
    }

    public void setJndiPassword(final String password) {
        setConfigValue("audit2db.jndiPassword", password);
    }

    public String getJdbcDriver() {
        return getConfigValue("audit2db.jdbcDriver");
    }

    public void setJdbcDriver(final String driver) {
        setConfigValue("audit2db.jdbcDriver", driver);
    }

    public String getJdbcUrl() {
        return getConfigValue("audit2db.jdbcUrl");
    }

    public void setJdbcUrl(final String url) {
        setConfigValue("audit2db.jdbcUrl", url);
    }

    public String getJdbcUser() {
        return getConfigValue("audit2db.jdbcUser");
    }

    public void setJdbcUser(final String user) {
        setConfigValue("audit2db.jdbcUser", user);
    }

    public String getJdbcPassword() {
        return getConfigValue("audit2db.jdbcPassword");
    }

    public void setJdbcPassword(final String password) {
        setConfigValue("audit2db.jdbcPassword", password);
    }

    private HtmlElement getElementByTagNameAndTextContent(final String tagName, final String textContent) {
        final List<HtmlElement> elements = configForm.getElementsByTagName(tagName);
        HtmlElement retval = null;
        // find the save button (it has no predictable id)
        for (final HtmlElement element : elements) {
            if (element.getTextContent().trim().equalsIgnoreCase(textContent)) {
                retval = element;
                break;
            }
        }
        return retval;
    }

    public HtmlElement getSaveButton() {
        return getElementByTagNameAndTextContent("button", "save");
    }

    public HtmlElement getTestConnectionButton() {
        return getElementByTagNameAndTextContent("button", "test connection");
    }

    public void saveChanges() {
        final HtmlElement saveButton = getSaveButton();

        if (null == saveButton) {
            throw new RuntimeException("Save button not found on config form!");
        }

        try {
            saveButton.click();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void testConnection() {
        final HtmlElement testConnectionButton = getTestConnectionButton();

        if (null == testConnectionButton) {
            throw new RuntimeException("Test connection button not found on config form!");
        }

        try {
            testConnectionButton.click();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
