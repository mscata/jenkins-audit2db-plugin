/**
 * 
 */
package org.jenkins.plugins.audit2db.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;
import org.jenkins.plugins.audit2db.internal.data.AbstractHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.HibernateUtil;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.model.BuildNode;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Unit tests for the {@link BuildDetailsHibernateRepository} class.
 * 
 * @author Marco Scata
 * 
 */
public class BuildDetailsHibernateRepositoryTests extends RepositoryTests {
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
    public void createShouldReturnMatchingId() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);
	Assert.assertEquals("Unexpected build id", buildId, build.getId());
    }

    @Test
    public void createBuildsWithSameNodeShouldReuseNodeEntity() {
	final BuildDetails build1 = createRandomBuildDetails();
	build1.setId("BUILD_1");
	final BuildDetails build2 = createRandomBuildDetails();
	build2.setId("BUILD_2");

	repository.saveBuildDetails(build1);
	repository.saveBuildDetails(build2);

	final HibernateTemplate hibernate = new HibernateTemplate();
	hibernate
	.setSessionFactory(((AbstractHibernateRepository) repository)
		.getSessionFactory());

	@SuppressWarnings("unchecked")
	final List<BuildNode> nodes = hibernate.loadAll(BuildNode.class);
	Assert.assertEquals("Unexpected number of node entities", 1,
		nodes.size());
    }

    @Test
    public void retrievalByNonMatchingIdShouldReturnNullEntity() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final BuildDetails retrievedBuild = repository
	.getBuildDetailsById(build.getId() + "NOMATCH");
	Assert.assertNull("Unexpected null build", retrievedBuild);
    }

    @Test
    public void retrievalByMatchingIdShouldReturnSameEntity() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final BuildDetails retrievedBuild = repository
	.getBuildDetailsById(build.getId());
	Assert.assertNotNull("Unexpected null build", build);
	Assert.assertEquals("Mismatching build details found", build,
		retrievedBuild);
    }

    @Test
    public void retrievalByNonMatchingDateRangeShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final Calendar start = Calendar.getInstance();
	start.set(1914, 6, 28, 9, 0, 0);
	final Calendar end = Calendar.getInstance();
	end.set(1918, 10, 11, 11, 0, 0);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByDateRange(start.getTime(), end.getTime());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingDateRangeShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final Calendar start = Calendar.getInstance();
	final Calendar end = Calendar.getInstance();

	start.setTime(new Date(build.getStartDate().getTime() - 10000));
	end.setTime(new Date(build.getStartDate().getTime() + 10000));

	List<BuildDetails> builds = repository
	.getBuildDetailsByDateRange(start.getTime(), end.getTime());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));

	start.setTime(new Date(build.getEndDate().getTime() - 10000));
	end.setTime(new Date(build.getEndDate().getTime() + 10000));

	builds = repository.getBuildDetailsByDateRange(
		start.getTime(), end.getTime());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
    }

    @Test
    public void retrievalByNonMatchingDurationRangeShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final long min = build.getDuration() + 10;
	final long max = min + 100;

	final List<BuildDetails> builds = repository
	.getBuildDetailsByDurationRange(min, max);
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingDurationRangeShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final long min = build.getDuration() - 10;
	final long max = min + 100;

	final List<BuildDetails> builds = repository
	.getBuildDetailsByDurationRange(min, max);
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingFullNameShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByFullName(build.getFullName() + "NOMATCH");
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingFullNameShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByFullName(build.getFullName().toLowerCase());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingNameShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByName(build.getName() + "NOMATCH");
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingNameShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByName(build.getName().toLowerCase());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingUserIdShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByUserId(build.getUserId() + "NOMATCH");
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingUserIdShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByUserId(build.getUserId().toLowerCase());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingUserNameShouldReturnEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByUserName(build.getUserName() + "NOMATCH");
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingUserNameShouldReturnNonEmptyList() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final List<BuildDetails> builds = repository
	.getBuildDetailsByUserName(build.getUserName().toLowerCase());
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void updatedBuildDetailsShouldBePersisted() {
	final BuildDetails build = createRandomBuildDetails();
	final Object buildId = repository.saveBuildDetails(build);
	Assert.assertNotNull("Unexpected null build id", buildId);

	final String oldName = build.getName();
	final String newName = oldName + "UPDATED";

	build.setName(newName);
	repository.updateBuildDetails(build);

	List<BuildDetails> builds = repository
	.getBuildDetailsByName(oldName);
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertTrue("Unexpected non-empty list of builds",
		builds.isEmpty());

	builds = repository.getBuildDetailsByName(newName);
	Assert.assertNotNull("Unexpected null list of builds", builds);
	Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
	Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	Assert.assertEquals("Mismatching build details found", build,
		builds.get(0));
    }

    @Test
    public void updateNullBuildDetailsShouldFail() {
	try {
	    repository.updateBuildDetails(null);
	    Assert.fail("Unexpcted repository update for null object");
	} catch (final Exception e) {
	    Assert.assertEquals("Unexpected exception type",
		    IllegalArgumentException.class, e.getClass());
	}
    }

    @Test
    public void saveNullBuildDetailsShouldFail() {
	try {
	    repository.saveBuildDetails(null);
	    Assert.fail("Unexpected repository save for null object");
	} catch (final Exception e) {
	    Assert.assertEquals("Unexpected exception type",
		    IllegalArgumentException.class, e.getClass());
	}
    }

    @Test
    public void retrievingAllProjectNamesShouldMatchDataset() {
	final Calendar fromDate = Calendar.getInstance();
	fromDate.add(Calendar.YEAR, -1);

	final Calendar toDate = Calendar.getInstance();
	toDate.add(Calendar.YEAR, 1);

	final Map<String, Integer> dataset = createRandomDataset();

	final List<String> projectNames = repository.getProjectNames(hostName,
		fromDate.getTime(), toDate.getTime());
	Assert.assertNotNull("Unexpected null list of project names",
		projectNames);
	Assert.assertFalse("Unexpected empty list of project names",
		projectNames.isEmpty());
	Assert.assertEquals("Unexpected number of project names",
		dataset.size(), projectNames.size());
    }

    @Test
    public void retrievingOldProjectNamesShouldReturnEmptyList() {
	createRandomDataset();

	final Calendar fromDate = Calendar.getInstance();
	fromDate.add(Calendar.YEAR, -10);

	final Calendar toDate = Calendar.getInstance();
	toDate.add(Calendar.YEAR, -1);

	final List<String> projectNames = repository.getProjectNames(hostName,
		fromDate.getTime(), toDate.getTime());
	Assert.assertNotNull("Unexpected null list of project names",
		projectNames);
	Assert.assertTrue("Unexpected non-empty list of project names",
		projectNames.isEmpty());
    }
}
