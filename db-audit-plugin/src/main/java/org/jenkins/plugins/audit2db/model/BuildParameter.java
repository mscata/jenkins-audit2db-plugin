/**
 * 
 */
package org.jenkins.plugins.audit2db.model;

/**
 * Data model to map build parameters.
 * 
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
	BuildDetails getBuildDetails();
	void setBuildDetails(BuildDetails buildId);
}
