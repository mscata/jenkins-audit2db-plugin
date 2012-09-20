/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.jenkins.plugins.audit2db.model.BuildNode;

/**
 * @author Marco Scata
 *
 */
@Entity(name="JENKINS_BUILD_NODE")
public class BuildNodeImpl implements BuildNode {
    private String hostname;
    private String url;
    private String name;
    private String description;
    private String label;
    //	private List<BuildDetails> buildDetails = new ArrayList<BuildDetails>();

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#getHostname()
     */
    @Override
    public String getHostname() {
        return hostname;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#setHostname(java.lang.String)
     */
    @Column(nullable=false, unique=false)
    @Override
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#getUrl()
     */
    @Id
    @Column(nullable=false, unique=true)
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#setUrl(java.lang.String)
     */
    @Override
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#getName()
     */
    @Column(nullable=false, unique=false)
    @Override
    public String getName() {
        return name;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#setName(java.lang.String)
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#getDescription()
     */
    @Column(nullable=true, unique=false)
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#getLabel()
     */
    @Column(nullable=true, unique=false)
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @see org.jenkins.plugins.audit2db.model.BuildNode#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(final String label) {
        this.label = label;
    }
    //
    //	/**
    //	 *  @see org.jenkins.plugins.audit2db.model.BuildNode
    //	 */
    //	@OneToMany(cascade=CascadeType.ALL, targetEntity=BuildDetailsImpl.class, mappedBy="node", fetch=FetchType.LAZY)
    //	@Column(nullable=true, unique=false)
    //	@Override
    //	public List<BuildDetails> getBuildDetails() {
    //		return this.buildDetails;
    //	}
    //
    //	@Override
    //	public void setBuildDetails(final List<BuildDetails> buildDetails) {
    //		if (buildDetails != null) {
    //			this.buildDetails.clear();
    //			this.buildDetails.addAll(buildDetails);
    //		}
    //	}

    @Override
    public String toString() {
        return this.url;
    }

    @Override
    public int hashCode() {
        return this.url.toUpperCase().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        // fail-fast logic
        if (this == obj) { return true; }
        if (null == obj) { return false; }
        if (!(obj instanceof BuildNode)) { return false; }

        final BuildNode other = (BuildNode) obj;

        return other.hashCode() == this.hashCode();
    }

    /**
     * Default constructor.
     */
    public BuildNodeImpl() {
        super();
    }

    /**
     * Constructs a new object with the given properties.
     * 
     * @param hostname the hostname.
     * @param url the node url.
     * @param name the node name.
     * @param description the node description.
     * @param label the node label.
     */
    public BuildNodeImpl(final String hostname, final String url,
            final String name, final String description, final String label) {
        this.hostname = hostname;
        this.url = url;
        this.name = name;
        this.description = description;
        this.label = label;
    }
}
