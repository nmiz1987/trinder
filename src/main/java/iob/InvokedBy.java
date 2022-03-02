package iob;

import java.io.Serializable;

public class InvokedBy implements Serializable {
	private static final long serialVersionUID = 1L;

	private UserID userId;

	public InvokedBy() {
	}

	public InvokedBy(UserID userId) {
		this();
		this.userId = userId;
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}
}
