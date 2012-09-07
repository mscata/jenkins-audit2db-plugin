/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.model;

import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Cause.UserIdCause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.model.BuildParameter;

/**
 * @author Marco Scata
 *
 */
@Entity(name="JENKINS_BUILD_DETAILS")
public class BuildDetailsImpl implements BuildDetails {
	private String id;
	private String name;
	private String fullName;
	private Date startDate = new Date();
	private Date endDate;
	private Long duration;
	private String userId;
	private String userName;
	private final List<BuildParameter> parameters = new ArrayList<BuildParameter>();

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getId()
	 */
	@Id
	@Column(nullable=false, unique=true)
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getName()
	 */
	@Override
	@Column(nullable=false, unique=false)
	public String getName() {
		return name;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getFullName()
	 */
	@Column(nullable=false, unique=false)
	@Override
	public String getFullName() {
		return fullName;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setFullName(java.lang.String)
	 */
	@Override
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getStartDate()
	 */
	@Column(nullable=false, unique=false)
	@Override
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setStartDate(java.util.Date)
	 */
	@Override
	public void setStartDate(Date start) {
		this.startDate = start;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getEndDate()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setEndDate(java.util.Date)
	 */
	@Override
	public void setEndDate(Date end) {
		this.endDate = end;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getDuration()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public Long getDuration() {
		return duration;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setDuration(java.lang.Long)
	 */
	@Override
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getUserId()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getUserName()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public String getUserName() {
		return userName;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;

	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#getParameters()
	 */
	@OneToMany(cascade=CascadeType.ALL, targetEntity=BuildParameterImpl.class)
	@Column(nullable=true, unique=false)
	@Override
	public List<BuildParameter> getParameters() {
		return parameters;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildDetails#setParameters(java.util.List)
	 */
	@Override
	public void setParameters(List<BuildParameter> params) {
		if (null != params) {
			// need a temporary array otherwise hibernate
			// will clear the property bag too
			final BuildParameter[] tempParams = params.toArray(new BuildParameter[]{});
			this.parameters.clear();
			Collections.addAll(this.parameters, tempParams);
		}
	}

	@Override
	public String toString() {
		return String.format("%s [%s]");
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// fail-fast logic
		if (null == obj) { return false; }
		if (!(obj instanceof BuildDetails)) { return false; }
		
		final BuildDetails other = (BuildDetails) obj;
		
		return other.hashCode() == this.hashCode();
	}
	
	/**
	 * Default constructor.
	 */
	public BuildDetailsImpl() {}
	
	/**
	 * Constructs a new BuildDetailsImpl object using the details
	 * of the given Jenkins build.
	 * 
	 * @param build a valid Jenkins build object.
	 */
	public BuildDetailsImpl(final AbstractBuild<?, ?> build) {
		this.id = build.getId();
		this.fullName = build.getDisplayName();
		this.startDate = build.getTime();
		final List<CauseAction> actions = build.getActions(CauseAction.class);
		boolean userFound = false;
		for (final CauseAction action : actions) {
			for (final Cause cause : action.getCauses()) {
				if (cause instanceof UserIdCause) {
					userFound = true;
					this.userId = ((UserIdCause)cause).getUserId();
					this.userName = ((UserIdCause)cause).getUserName();
					break;
				}
			}
			if (userFound) { break; }
		}
	}
}
