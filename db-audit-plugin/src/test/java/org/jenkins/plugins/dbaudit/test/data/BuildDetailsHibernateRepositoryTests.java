/**
 * 
 */
package org.jenkins.plugins.dbaudit.test.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jenkins.plugins.dbaudit.data.BuildDetailsRepository;
import org.jenkins.plugins.dbaudit.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.dbaudit.internal.model.BuildDetailsImpl;
import org.jenkins.plugins.dbaudit.internal.model.BuildParameterImpl;
import org.jenkins.plugins.dbaudit.model.BuildDetails;
import org.jenkins.plugins.dbaudit.model.BuildParameter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for the {@link BuildDetailsHibernateRepository} class.
 * 
 * @author Marco Scata
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/org/jenkins/plugins/dbaudit/application-context.xml")
public class BuildDetailsHibernateRepositoryTests {
	@Autowired
	private BuildDetailsRepository buildDetailsRepository;
	
	private BuildDetails getBuildDetails() {
		final BuildDetails build = new BuildDetailsImpl();
		build.setDuration(Long.valueOf(60));
		build.setEndDate(new Date(
				build.getStartDate().getTime() + (build.getDuration() * 1000)));
		build.setFullName("BUILD FULL NAME");
		build.setId("BUILD ID");
		build.setName("BUILD NAME");
		build.setUserId("BUILD USER ID");
		build.setUserName("BUILD USER NAME");

		final List<BuildParameter> params = new ArrayList<BuildParameter>();
		params.add(new BuildParameterImpl(
				Long.valueOf(123), "PARAM NAME", "PARAM VALUE", build.getId()));
		
		build.setParameters(params);
		
		return build;
	}
	
	@Test
	public void createShouldReturnMatchingId() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		Assert.assertEquals("Unexpected build id", buildId, build.getId());
	}

	
	@Test
	public void retrievalByNonMatchingIdShouldReturnNullEntity() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final BuildDetails retrievedBuild = buildDetailsRepository.getBuildDetailsById(
				build.getId() + "NOMATCH");
		Assert.assertNull("Unexpected null build", retrievedBuild);
	}

	@Test
	public void retrievalByMatchingIdShouldReturnSameEntity() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final BuildDetails retrievedBuild = buildDetailsRepository.getBuildDetailsById(
				build.getId());
		Assert.assertNotNull("Unexpected null build", build);
		Assert.assertEquals("Mismatching build details found", build, retrievedBuild);
	}
	
	@Test
	public void retrievalByNonMatchingDateRangeShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final Calendar start = Calendar.getInstance();
		start.set(1914, 6, 28, 9, 0, 0);
		final Calendar end = Calendar.getInstance();
		end.set(1918, 10, 11, 11, 0, 0);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDateRange(
				start.getTime(), end.getTime());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}
	
	@Test
	public void retrievalByMatchingDateRangeShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final Calendar start = Calendar.getInstance();
		final Calendar end = Calendar.getInstance();

		start.setTime(new Date(build.getStartDate().getTime() - 10000));
		end.setTime(new Date(build.getStartDate().getTime() + 10000));
		
		List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDateRange(
				start.getTime(), end.getTime());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));

		start.setTime(new Date(build.getEndDate().getTime() - 10000));
		end.setTime(new Date(build.getEndDate().getTime() + 10000));
		
		builds = buildDetailsRepository.getBuildDetailsByDateRange(
				start.getTime(), end.getTime());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
	}
	
	@Test
	public void retrievalByNonMatchingDurationRangeShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		long min = build.getDuration() + 10;
		long max = min + 100;
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDurationRange(min, max);
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}
	
	@Test
	public void retrievalByMatchingDurationRangeShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		long min = build.getDuration() - 10;
		long max = min + 100;

		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDurationRange(min, max);
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}

	@Test
	public void retrievalByNonMatchingFullNameShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByFullName(
				build.getFullName() + "NOMATCH");
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}

	@Test
	public void retrievalByMatchingFullNameShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByFullName(
				build.getFullName().toLowerCase());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}

	@Test
	public void retrievalByNonMatchingNameShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByName(
				build.getName() + "NOMATCH");
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}

	@Test
	public void retrievalByMatchingNameShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByName(
				build.getName().toLowerCase());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}

	@Test
	public void retrievalByNonMatchingUserIdShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserId(
				build.getUserId() + "NOMATCH");
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}

	@Test
	public void retrievalByMatchingUserIdShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserId(
				build.getUserId().toLowerCase());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}

	@Test
	public void retrievalByNonMatchingUserNameShouldReturnEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserName(
				build.getUserName() + "NOMATCH");
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
	}

	@Test
	public void retrievalByMatchingUserNameShouldReturnNonEmptyList() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserName(
				build.getUserName().toLowerCase());
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}
	
	@Test
	public void updatedBuildDetailsShouldBePersisted() {
		final BuildDetails build = getBuildDetails();
		final Object buildId = buildDetailsRepository.saveBuildDetails(build);
		Assert.assertNotNull("Unexpected null build id", buildId);
		
		final String oldName = build.getName();
		final String newName = oldName + "UPDATED";
		
		build.setName(newName);
		buildDetailsRepository.updateBuildDetails(build);
		
		List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByName(oldName);
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
		
		builds = buildDetailsRepository.getBuildDetailsByName(newName);
		Assert.assertNotNull("Unexpected null list of builds", builds);
		Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
		Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
		Assert.assertEquals("Mismatching build details found", build, builds.get(0));
	}
}
