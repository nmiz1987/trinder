package iob;

import java.util.Objects;

public class CreatedBy {
	private UserID userId;

	public CreatedBy() {
	}

	public CreatedBy(UserID userId) {
		this();
		this.userId = userId;
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CreatedBy [userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreatedBy other = (CreatedBy) obj;
		return Objects.equals(userId, other.userId);
	}
}
