/**
 * 
 */
package org.jenkins.plugins.dbaudit.model;

import java.util.Date;
import java.util.List;

/**
 * @author Marco Scata
 *
 */
public interface BuildDetails {
	String getId();
	void setId(String id);
	String getName();
	void setName(String name);
	String getFullName();
	void setFullName(String fullName);
	Date getStartDate();
	void setStartDate(Date start);
	Date getEndDate();
	void setEndDate(Date end);
	Long getDuration();
	void setDuration(Long duration);
	String getUserId();
	void setUserId(String userId);
	String getUserName();
	void setUserName(String userName);
	List<BuildParameter> getParameters();
	void setParameters(List<BuildParameter> params);
}
