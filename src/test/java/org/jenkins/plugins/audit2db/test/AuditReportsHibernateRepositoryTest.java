/**
 * 
 */
package org.jenkins.plugins.audit2db.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;
import org.jenkins.plugins.audit2db.internal.data.AuditReportsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.HibernateUtil;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.junit.Test;

/**
 * Unit tests for the {@link AuditReportsHibernateRepository} class.
 * 
 * @author Marco Scata
 * 
 */
public class AuditReportsHibernateRepositoryTest extends RepositoryTests {
    private final String hostName = "MY_JENKINS";

    private final BuildDetailsRepository repository = new BuildDetailsHibernateRepository(
	    HibernateUtil.getSessionFactory(HibernateUtil.getExtraProperties(
		    "org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:mem:test", "SA",
	    "")));

    private Map<String, Integer> createRandomDataset() {
	final Map<String, Integer> retval = new HashMap<String, Integer>();

	final int numOfProjects = 10;
	final int maxBuildsPerProject = 25;
	for (int projCtr = 1; projCtr <= numOfProjects; projCtr++) {
	    final String projectName = "PROJECT_" + projCtr;
	    final int numOfBuilds = (int) (Math.random() * maxBuildsPerProject) + 1;
	    for (int buildCtr = 1; buildCtr <= numOfBuilds; buildCtr++) {
		final BuildDetails buildDetails = createRandomBuildDetails();
		buildDetails.setId(buildDetails.getId() + buildCtr);
		buildDetails.setName(projectName);
		buildDetails.getNode().setMasterHostName(hostName);
		repository.saveBuildDetails(buildDetails);
	    }
	    retval.put(projectName, Integer.valueOf(numOfBuilds));
	}

	return retval;
    }

    @Test
    public void retrievingAllProjectNamesShouldMatchDataset() {
	final Calendar fromDate = Calendar.getInstance();
	fromDate.add(Calendar.YEAR, -1);

	final Calendar toDate = Calendar.getInstance();
	toDate.add(Calendar.YEAR, 1);

	final Map<String, Integer> dataset = createRandomDataset();

	final List<String> projectNames = repository.getProjectNames(
		hostName, fromDate.getTime(), toDate.getTime());
	Assert.assertNotNull("Unexpected null list of project names", projectNames);
	Assert.assertFalse("Unexpected empty list of project names", projectNames.isEmpty());
	Assert.assertEquals("Unexpected number of project names", dataset.size(), projectNames.size());
    }

    @Test
    public void retrievingOldProjectNamesShouldReturnEmptyList() {
	createRandomDataset();

	final Calendar fromDate = Calendar.getInstance();
	fromDate.add(Calendar.YEAR, -10);

	final Calendar toDate = Calendar.getInstance();
	toDate.add(Calendar.YEAR, -1);

	final List<String> projectNames = repository.getProjectNames(
		hostName, fromDate.getTime(), toDate.getTime());
	Assert.assertNotNull("Unexpected null list of project names", projectNames);
	Assert.assertTrue("Unexpected non-empty list of project names", projectNames.isEmpty());
    }
}
