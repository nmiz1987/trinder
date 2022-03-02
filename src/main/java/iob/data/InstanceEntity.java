package iob.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import iob.InstanceId;
import iob.CreatedBy;
import iob.Location;
import iob.logic.instances.IobCreatedByToStringConverter;
import iob.logic.instances.IobInstanceAttributesMapToJsonConverter;
import iob.logic.instances.IobLocationToStringConverter;

@Entity
@Table(name = "INSTANCES")
public class InstanceEntity {
	private InstanceId instanceId;
	private String type; /* catalog / physical (in store/stock) */
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String, Object> instanceAttributes; /* "clothing_type": "t-shirt"/"pants" */

	private Set<InstanceEntity> children;
	private InstanceEntity parent;

	public InstanceEntity() {
		this.children = new HashSet<>();
	}

	public InstanceEntity(InstanceId instanceId, String type, String name, Boolean active, Date createdTimestamp,
			CreatedBy createdBy, Location location) {
		super();
		this.instanceId = instanceId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.instanceAttributes = new HashMap<String, Object>();
	}

	@EmbeddedId
	@Column(name = "INSTANCE_ID")
	public InstanceId getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(InstanceId instanceId) {
		this.instanceId = instanceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Convert(converter = IobCreatedByToStringConverter.class)
	@Lob
	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	@Convert(converter = IobLocationToStringConverter.class)
	@Lob
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public InstanceEntity getParent() {
		return parent;
	}

	public void setParent(InstanceEntity parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public Set<InstanceEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<InstanceEntity> children) {
		this.children = children;
	}

	public void addChild(InstanceEntity child) {
		this.children.add(child);
	}

	@Convert(converter = IobInstanceAttributesMapToJsonConverter.class)
	@Lob
	public Map<String, Object> getInstanceAttributes() {
		return instanceAttributes;
	}

	public void setInstanceAttributes(Map<String, Object> instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}

	@Override
	public String toString() {
		return "InstanceEntity [instanceId=" + instanceId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(instanceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceEntity other = (InstanceEntity) obj;
		return Objects.equals(instanceId, other.instanceId);
	}
}
