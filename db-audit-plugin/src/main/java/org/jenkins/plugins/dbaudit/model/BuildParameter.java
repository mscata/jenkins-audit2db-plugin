/**
 * 
 */
package org.jenkins.plugins.dbaudit.model;

/**
 * @author Marco Scata
 *
 */
public interface BuildParameter {
	Long getId();
	void setId(Long id);
	String getName();
	void setName(String name);
	String getValue();
	void setValue(String value);
	String getBuildId();
	void setBuildId(String buildId);
}
