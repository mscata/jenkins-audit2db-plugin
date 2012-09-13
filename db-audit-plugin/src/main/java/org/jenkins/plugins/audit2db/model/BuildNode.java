/**
 * 
 */
package org.jenkins.plugins.audit2db.model;


/**
 * Data model to map node details.
 * 
 * @author Marco Scata
 *
 */
public interface BuildNode {
	String getHostname();
	void setHostname(String hostname);
	String getUrl();
	void setUrl(String url);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	String getLabel();
	void setLabel(String label);
//	List<BuildDetails> getBuildDetails();
//	void setBuildDetails(List<BuildDetails> buildDetails);
}
