package iob;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InstanceId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String domain;
	private String id;

	public InstanceId() {
		this.domain = "";
		this.id = "";
	}

	public InstanceId(String domain, String id) {
		this();
		this.domain = domain;
		this.id = id;
	}

	@Column(name = "INSTANCEID_DOMAIN")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "INSTANCEID_ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "InstanceId [domain=" + domain + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceId other = (InstanceId) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(id, other.id);
	}
}
