/**
 * 
 */
package org.jenkins.plugins.audit2db.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jenkins.plugins.audit2db.data.BuildDetailsRepository;
import org.jenkins.plugins.audit2db.internal.data.BuildDetailsHibernateRepository;
import org.jenkins.plugins.audit2db.internal.data.HibernateUtil;
import org.jenkins.plugins.audit2db.internal.model.BuildDetailsImpl;
import org.jenkins.plugins.audit2db.internal.model.BuildNodeImpl;
import org.jenkins.plugins.audit2db.internal.model.BuildParameterImpl;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.model.BuildNode;
import org.jenkins.plugins.audit2db.model.BuildParameter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Unit tests for the {@link BuildDetailsHibernateRepository} class.
 * 
 * @author Marco Scata
 * 
 */
public class BuildDetailsHibernateRepositoryTests {
    private final BuildDetailsRepository buildDetailsRepository = new BuildDetailsHibernateRepository(
            HibernateUtil.getSessionFactory(HibernateUtil
                    .getExtraProperties("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:mem:test", "SA", "")));

    private BuildDetails createBuildDetails() {
        final BuildDetails build = new BuildDetailsImpl();
        build.setDuration(Long.valueOf(60));
        build.setEndDate(new Date(build.getStartDate().getTime() + (build.getDuration() * 1000)));
        build.setFullName("BUILD FULL NAME");
        build.setId("BUILD ID");
        build.setName("BUILD NAME");
        build.setUserId("BUILD USER ID");
        build.setUserName("BUILD USER NAME");

        final List<BuildParameter> params = new ArrayList<BuildParameter>();
        params.add(new BuildParameterImpl("PARAM_ID", "PARAM NAME", "PARAM VALUE", build));
        build.setParameters(params);

        final BuildNode node = new BuildNodeImpl("NODE HOSTNAME", "NODE URL", "NODE NAME", "NODE DESCRIPTION", "NODE LABEL");
        build.setNode(node);

        return build;
    }

    @Test
    public void createShouldReturnMatchingId() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);
        Assert.assertEquals("Unexpected build id", buildId, build.getId());
    }

    @Test
    public void createBuildsWithSameNodeShouldReuseNodeEntity() {
        final BuildDetails build1 = createBuildDetails();
        build1.setId("BUILD_1");
        final BuildDetails build2 = createBuildDetails();
        build2.setId("BUILD_2");

        buildDetailsRepository.saveBuildDetails(build1);
        buildDetailsRepository.saveBuildDetails(build2);

        final HibernateTemplate hibernate = new HibernateTemplate();
        hibernate.setSessionFactory(((BuildDetailsHibernateRepository) buildDetailsRepository).getSessionFactory());

        @SuppressWarnings("unchecked")
        final List<BuildNode> nodes = hibernate.loadAll(BuildNode.class);
        Assert.assertEquals("Unexpected number of node entities", 1, nodes.size());
    }

    @Test
    public void retrievalByNonMatchingIdShouldReturnNullEntity() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final BuildDetails retrievedBuild = buildDetailsRepository.getBuildDetailsById(build.getId() + "NOMATCH");
        Assert.assertNull("Unexpected null build", retrievedBuild);
    }

    @Test
    public void retrievalByMatchingIdShouldReturnSameEntity() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final BuildDetails retrievedBuild = buildDetailsRepository.getBuildDetailsById(build.getId());
        Assert.assertNotNull("Unexpected null build", build);
        Assert.assertEquals("Mismatching build details found", build, retrievedBuild);
    }

    @Test
    public void retrievalByNonMatchingDateRangeShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final Calendar start = Calendar.getInstance();
        start.set(1914, 6, 28, 9, 0, 0);
        final Calendar end = Calendar.getInstance();
        end.set(1918, 10, 11, 11, 0, 0);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDateRange(start.getTime(), end.getTime());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingDateRangeShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final Calendar start = Calendar.getInstance();
        final Calendar end = Calendar.getInstance();

        start.setTime(new Date(build.getStartDate().getTime() - 10000));
        end.setTime(new Date(build.getStartDate().getTime() + 10000));

        List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDateRange(start.getTime(), end.getTime());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));

        start.setTime(new Date(build.getEndDate().getTime() - 10000));
        end.setTime(new Date(build.getEndDate().getTime() + 10000));

        builds = buildDetailsRepository.getBuildDetailsByDateRange(start.getTime(), end.getTime());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
    }

    @Test
    public void retrievalByNonMatchingDurationRangeShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final long min = build.getDuration() + 10;
        final long max = min + 100;

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDurationRange(min, max);
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingDurationRangeShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final long min = build.getDuration() - 10;
        final long max = min + 100;

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByDurationRange(min, max);
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingFullNameShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByFullName(build.getFullName() + "NOMATCH");
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingFullNameShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByFullName(build.getFullName().toLowerCase());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingNameShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByName(build.getName() + "NOMATCH");
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingNameShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByName(build.getName().toLowerCase());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingUserIdShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserId(build.getUserId() + "NOMATCH");
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingUserIdShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserId(build.getUserId().toLowerCase());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));
    }

    @Test
    public void retrievalByNonMatchingUserNameShouldReturnEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserName(build.getUserName() + "NOMATCH");
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertTrue("Unexpected non-empty list of builds", builds.isEmpty());
    }

    @Test
    public void retrievalByMatchingUserNameShouldReturnNonEmptyList() {
        final BuildDetails build = createBuildDetails();
        final Object buildId = buildDetailsRepository.saveBuildDetails(build);
        Assert.assertNotNull("Unexpected null build id", buildId);

        final List<BuildDetails> builds = buildDetailsRepository.getBuildDetailsByUserName(build.getUserName().toLowerCase());
        Assert.assertNotNull("Unexpected null list of builds", builds);
        Assert.assertFalse("Unexpected empty list of builds", builds.isEmpty());
        Assert.assertTrue("Unexpected number of builds", builds.size() == 1);
        Assert.assertEquals("Mismatching build details found", build, builds.get(0));
    }

    @Test
    public void updatedBuildDetailsShouldBePersisted() {
        final BuildDetails build = createBuildDetails();
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

    @Test
    public void updateNullBuildDetailsShouldFail() {
        try {
            buildDetailsRepository.updateBuildDetails(null);
            Assert.fail("Unexpcted repository update for null object");
        } catch (final Exception e) {
            Assert.assertEquals("Unexpected exception type", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void saveNullBuildDetailsShouldFail() {
        try {
            buildDetailsRepository.saveBuildDetails(null);
            Assert.fail("Unexpected repository save for null object");
        } catch (final Exception e) {
            Assert.assertEquals("Unexpected exception type", IllegalArgumentException.class, e.getClass());
        }
    }
}
