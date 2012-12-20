package org.jenkins.plugins.audit2db.test;

import hudson.ExtensionList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jenkins.model.Jenkins;

import org.jenkins.plugins.audit2db.internal.model.BuildDetailsImpl;
import org.jenkins.plugins.audit2db.internal.model.BuildNodeImpl;
import org.jenkins.plugins.audit2db.internal.model.BuildParameterImpl;
import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.model.BuildNode;
import org.jenkins.plugins.audit2db.model.BuildParameter;
import org.jenkins.plugins.audit2db.reports.DbAuditReport;

public class RepositoryTests {

    private RepositoryTests() {
	// cannot be instantiated by others
    }

    public static BuildDetails createRandomBuildDetails(final boolean withParams) {
	final long salt = System.nanoTime();
	final BuildDetails build = new BuildDetailsImpl();
	build.setDuration(Long.valueOf(60 + (long) (Math.random() * 60)));
	build.setEndDate(new Date(build.getStartDate().getTime()
		+ (build.getDuration() * 1000)));
	build.setFullName("BUILD FULL NAME " + salt);
	build.setId("BUILD ID " + salt);
	build.setName("BUILD NAME " + salt);
	build.setUserId("BUILD USER ID " + salt);
	build.setUserName("BUILD USER NAME " + salt);

	if (withParams) {
	    final List<BuildParameter> params = new ArrayList<BuildParameter>();
	    params.add(new BuildParameterImpl("PARAM_ID 1 " + salt,
		    "PARAM NAME 1 " + salt, "PARAM VALUE 1 " + salt, build));
	    params.add(new BuildParameterImpl("PARAM_ID 2 " + salt,
		    "PARAM NAME 2 " + salt, "PARAM VALUE 2 " + salt, build));
	    build.setParameters(params);
	}

	final BuildNode node = new BuildNodeImpl("NODE ADDRESS",
		"NODE HOSTNAME", "NODE DISPLAYNAME", "NODE URL", "NODE NAME",
		"NODE DESCRIPTION", "NODE LABEL");
	build.setNode(node);

	return build;
    }

    public static BuildDetails createRandomBuildDetails() {
	return createRandomBuildDetails(true);
    }

    public static List<BuildDetails> createRandomBuildHistory(
	    final String hostName, final String projectName,
	    final int numOfBuilds, final boolean withParams) {
	final List<BuildDetails> retval = new ArrayList<BuildDetails>(
		numOfBuilds);
	for (int buildCtr = 1; buildCtr <= numOfBuilds; buildCtr++) {
	    final BuildDetails buildDetails = createRandomBuildDetails(withParams);
	    buildDetails.setId(buildDetails.getId() + buildCtr);
	    buildDetails.setName(projectName);
	    buildDetails.getNode().setMasterHostName(hostName);
	    retval.add(buildDetails);
	}
	return retval;
    }

    public static Map<String, List<BuildDetails>> createRandomDataset(
	    final String hostName) {
	final Map<String, List<BuildDetails>> retval = new HashMap<String, List<BuildDetails>>();

	final int numOfProjects = 10;
	final int maxBuildsPerProject = 25;
	for (int projCtr = 1; projCtr <= numOfProjects; projCtr++) {
	    final String projectName = "PROJECT_" + projCtr;
	    final int numOfBuilds = (int) (Math.random() * maxBuildsPerProject) + 1;

	    // half of the projects will have params
	    final boolean withParams = ((projCtr % 2) == 0);
	    final List<BuildDetails> details = createRandomBuildHistory(
		    hostName, projectName, numOfBuilds, withParams);

	    retval.put(projectName, details);
	}

	return retval;
    }

    public static <T extends DbAuditReport> T getReportExtension(final Class<T> reportClass) {
	final ExtensionList<DbAuditReport> reportExtensions = Jenkins.getInstance().getExtensionList(DbAuditReport.class);
	for (final DbAuditReport reportExtension : reportExtensions) {
	    if (reportClass.isAssignableFrom(reportExtension.getClass())) {
		return reportClass.cast(reportExtension);
	    }
	}
	throw new RuntimeException("Unable to find extension of type " + reportClass);
    }
}