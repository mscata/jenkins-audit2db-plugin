/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jenkins.plugins.audit2db.data.AuditReportsRepository;
import org.jenkins.plugins.audit2db.model.BuildDetails;

/**
 * @author Marco Scata
 * 
 */
public class AuditReportsHibernateRepository extends
AbstractHibernateRepository implements AuditReportsRepository {

    private final static Logger LOGGER = Logger
    .getLogger(AuditReportsHibernateRepository.class.getName());

    /**
     * @param sessionFactory
     */
    public AuditReportsHibernateRepository(final SessionFactory sessionFactory) {
	super(sessionFactory);
    }

    @Override
    public List<String> getProjectNames(final String masterHostName,
	    final Date fromDate, final Date toDate) {
	final List<String> retval = new ArrayList<String>();

	final DetachedCriteria criteria = DetachedCriteria
	.forClass(BuildDetails.class)
	.add(Restrictions.and(
		Restrictions.ge("startDate", fromDate),
		Restrictions.le("endDate", toDate)))
		.createCriteria("node")
		.add(Restrictions.ilike("masterHostName", masterHostName));

	try {
	    @SuppressWarnings("unchecked")
	    final List<BuildDetails> buildDetails = getHibernateTemplate()
	    .findByCriteria(criteria);
	    if ((buildDetails != null) && !buildDetails.isEmpty()) {
		for (final BuildDetails detail : buildDetails) {
		    final String projectName = detail.getName();
		    if (!retval.contains(projectName)) {
			retval.add(projectName);
		    }
		}
	    }
	} catch (final Throwable t) {
	    LOGGER.log(Level.SEVERE, t.getMessage(), t);
	}

	return retval;
    }

    @Override
    public List<BuildDetails> getBuildDetails(final String masterHostName,
	    final Date fromDate, final Date toDate) {
	return getBuildDetails(masterHostName, null, fromDate, toDate);
    }

    /**
     * @see org.jenkins.plugins.audit2db.data.AuditReportsRepository#getBuildDetails(java.lang.String,
     *      java.sql.Date, java.sql.Date)
     */
    @Override
    public List<BuildDetails> getBuildDetails(
	    final String masterHostName,
	    final String projectName, final Date fromDate, final Date toDate) {
	final List<BuildDetails> retval = new ArrayList<BuildDetails>();

	// we need to specifically state >=startdate AND <=enddate
	// because the "between" semantics vary between database
	// implementations and we want to use an inclusive filter every time
	DetachedCriteria criteria = DetachedCriteria
	.forClass(BuildDetails.class);
	if (projectName != null) {
	    criteria = criteria.add(Restrictions.ilike("name", projectName));
	}
	criteria = criteria
	.add(Restrictions.and(
		Restrictions.ge("startDate", fromDate),
		Restrictions.le("endDate", toDate)))
		.createCriteria("node")
		.add(Restrictions.ilike("masterHostName", masterHostName))
		.addOrder(Property.forName("startDate").asc());

	try {
	    @SuppressWarnings("unchecked")
	    final List<BuildDetails> buildDetails = getHibernateTemplate()
	    .findByCriteria(criteria);
	    if ((buildDetails != null) && !buildDetails.isEmpty()) {
		retval.addAll(buildDetails);
	    }
	} catch (final Throwable t) {
	    LOGGER.log(Level.SEVERE, t.getMessage(), t);
	}

	return retval;
    }
}
