/**
 * 
 */
package org.jenkins.plugins.dbaudit.internal.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.jenkins.plugins.dbaudit.model.BuildDetails;
import org.jenkins.plugins.dbaudit.model.BuildParameter;

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
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getId()
	 */
	@Id
	@Column(nullable=false, unique=true)
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getName()
	 */
	@Override
	@Column(nullable=false, unique=false)
	public String getName() {
		return name;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getFullName()
	 */
	@Column(nullable=false, unique=false)
	@Override
	public String getFullName() {
		return fullName;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setFullName(java.lang.String)
	 */
	@Override
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getStartDate()
	 */
	@Column(nullable=false, unique=false)
	@Override
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setStartDate(java.util.Date)
	 */
	@Override
	public void setStartDate(Date start) {
		this.startDate = start;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getEndDate()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setEndDate(java.util.Date)
	 */
	@Override
	public void setEndDate(Date end) {
		this.endDate = end;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getDuration()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public Long getDuration() {
		return duration;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setDuration(java.lang.Long)
	 */
	@Override
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getUserId()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getUserName()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public String getUserName() {
		return userName;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;

	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#getParameters()
	 */
	@OneToMany(cascade=CascadeType.ALL, targetEntity=BuildParameterImpl.class)
	@Column(nullable=true, unique=false)
	@Override
	public List<BuildParameter> getParameters() {
		return parameters;
	}

	/**
	 * @see org.jenkins.plugins.dbaudit.model.BuildDetails#setParameters(java.util.List)
	 */
	@Override
	public void setParameters(List<BuildParameter> params) {
		if (null != params) {
			// need a temporary array otherwise hibernate
			// will clear the property bag too
			final BuildParameter[] tempParams = params.toArray(new BuildParameter[]{});
			this.parameters.clear();
			Collections.addAll(this.parameters, tempParams);
//			for (final BuildParameter tempParam : tempParams) {
//				this.parameters.add(tempParam);
//			}
		}
	}

}
