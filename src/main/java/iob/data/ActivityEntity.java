package iob.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iob.ActivityId;
import iob.InstanceId;
import iob.InvokedBy;
import iob.logic.instances.IobInstanceAttributesMapToJsonConverter;

@Entity
@Table(name = "ACTIVITIES")
public class ActivityEntity {
	private ActivityId activityId;
	private String type;
	private InstanceId instanceId;
	private Date createdTimestamp;
	private InvokedBy invokedBy;
	private Map<String, Object> activityAttributes;

	public ActivityEntity(ActivityId activityId, String type, InstanceId instanceId, Date createdTimestamp,
			InvokedBy invokedBy, Map<String, Object> activityAttributes) {
		super();
		this.activityId = activityId;
		this.type = type;
		this.instanceId = instanceId;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.activityAttributes = activityAttributes;
	}

	public ActivityEntity() {

	}

	@EmbeddedId
	@Column(name = "ACTIVITY_ID")
	public ActivityId getActivityId() {
		return activityId;
	}

	public void setActivityId(ActivityId activityId) {
		this.activityId = activityId;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "INSTANCE_ID")
	public InstanceId getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(InstanceId instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Column(name = "INVOKED_BY")
	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	@Convert(converter = IobInstanceAttributesMapToJsonConverter.class)
	@Column(name = "ACTIVITY_ATTRIBUTES")
	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}

	@Override
	public String toString() {
		return "ActivityEntity [activityId=" + activityId + ", type=" + type + ", instanceId=" + instanceId
				+ ", createdTimestamp=" + createdTimestamp + ", invokedBy=" + invokedBy + ", activityAttributes="
				+ activityAttributes + "]";
	}

	@Override
	public int hashCode() {
		return (this.activityId + this.createdTimestamp.toString()).hashCode();
	}

	public boolean equals(ActivityEntity obj) {
		if (this.activityId == obj.activityId)
			return true;
		return false;
	}
}
