/**
 * 
 */
package org.jenkins.plugins.dbaudit.data;

import java.util.Date;
import java.util.List;

import org.jenkins.plugins.dbaudit.model.BuildDetails;

/**
 * @author Marco Scata
 *
 */
public interface BuildDetailsRepository {
	Object saveBuildDetails(BuildDetails details);
	List<BuildDetails> getBuildDetailsByName(String name);
	List<BuildDetails> getBuildDetailsByFullName(String fullName);
	List<BuildDetails> getBuildDetailsByDateRange(Date start, Date end);
	List<BuildDetails> getBuildDetailsByDurationRange(long min, long max);
	List<BuildDetails> getBuildDetailsByUserId(String userId);
	List<BuildDetails> getBuildDetailsByUserName(String userName);
	void updateBuildDetails(BuildDetails details);
}
