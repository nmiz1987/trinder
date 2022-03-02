package iob;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ActivityId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String domain;
	private String id;

	public ActivityId() {
	}

	public ActivityId(String domain, String id) {
		this();
		this.domain = domain;
		this.id = id;
	}

	@Column(name = "ACTIVITYID_DOMAIN")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "ACTIVITYID_ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return (this.id + this.domain).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActivityId) {
			ActivityId tmp = (ActivityId) obj;
			if (this.id == tmp.id)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "ActivityId [domain=" + domain + ", id=" + id + "]";
	}
}
