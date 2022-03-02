package iob.boundaries;

public class NewUserBoundary {
	private String email;
	private String username;
	private String role;
	private String avatar;

	public NewUserBoundary() {
	}

	public NewUserBoundary(String email, String username, String role, String avatar) {
		this.email = email;
		this.username = username;
		this.role = role;
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
