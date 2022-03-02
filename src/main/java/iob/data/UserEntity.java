package iob.data;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import iob.UserID;

@Entity
@Table(name = "USERS")
public class UserEntity {
	private UserID userId;
	private String role;
	private String username;
	private String avatar;

	public UserEntity() {
	}

	@Column(name = "USER_ID")
	@EmbeddedId
	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		if (userId.getEmail() != null)
			this.userId = userId;
		else // DEFAULT
			this.userId = new UserID(userId.getDomain(), "user@demo.com");
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if (role != null && UserRole.contains(role))
			this.role = role;
		else // DEFAULT
			this.role = UserRole.PLAYER.name();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null)
			this.username = username;
		else // DEFAULT
			this.username = "Demo User";
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		if (avatar != null || avatar.isEmpty())
			this.avatar = avatar;
		else // DEFAULT
			this.avatar = "J";
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
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
		UserEntity other = (UserEntity) obj;
		return Objects.equals(userId, other.userId);
	}
}
