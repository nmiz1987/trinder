package iob;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserID implements Serializable {
	private static final long serialVersionUID = 1L;
	private String domain;
	private String email;

	public UserID(String domain, String email) {
		this();
		this.domain = domain;
		this.email = email;
	}

	public UserID() {
	}

	@Column(name = "USERID_DOMAIN")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "USERID_EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserID other = (UserID) obj;
		return Objects.equals(domain, other.domain) && Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "[domain=" + domain + ", email=" + email + "]";
	}
}
