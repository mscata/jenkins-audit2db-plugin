/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.jenkins.plugins.audit2db.model.BuildDetails;
import org.jenkins.plugins.audit2db.model.BuildParameter;

/**
 * Data class for build parameters.
 * 
 * @author Marco Scata
 *
 */
@Entity(name="JENKINS_BUILD_PARAMS")
public class BuildParameterImpl implements BuildParameter {
	private Long id;
	private String name;
	private String value;
	private BuildDetails buildDetails;
	
	public BuildParameterImpl() {
		super();
	}
	
	public BuildParameterImpl(final Long id, final String name, final String value, final BuildDetails buildDetails) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.buildDetails = buildDetails;
	}
	
	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#getId()
	 */
	@Id
	@Column(nullable=false, unique=true)
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#setId(java.lang.Long)
	 */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#getName()
	 */
	@Column(nullable=false, unique=false)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#getValue()
	 */
	@Column(nullable=true, unique=false)
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#setValue(java.lang.String)
	 */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#getBuildDetails()
	 */
	@ManyToOne(targetEntity=BuildDetailsImpl.class)
	@JoinColumn(nullable=false, unique=false)
	@Override
	public BuildDetails getBuildDetails() {
		return buildDetails;
	}

	/**
	 * @see org.jenkins.plugins.audit2db.model.BuildParameter#setBuildDetails(java.lang.String)
	 */
	@Override
	public void setBuildDetails(final BuildDetails buildDetails) {
		this.buildDetails = buildDetails;
	}

	/**
	 * Used to decide equality.
	 * 
	 * @return the internal id.
	 */
	@Transient
	private String getInternalId() {
		return String.format("%s/%s", 
				this.buildDetails.getId(), this.name);
	}
	
	@Override
	public String toString() {
		return String.format("%s=%s [%s]", 
				getInternalId(), this.value, this.id);
	}
	
	@Override
	public int hashCode() {
		return getInternalId().hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		// fail-fast logic
		if (null == obj) { return false; }
		if (!(obj instanceof BuildParameter)) { return false; }
		
		final BuildParameter other = (BuildParameter) obj;
		
		return other.hashCode() == this.hashCode();
	}
}
