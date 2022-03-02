package iob.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotAdminException extends RuntimeException {
	private static final long serialVersionUID = 808145515962768921L;

	public UserNotAdminException() {
	}

	public UserNotAdminException(String message) {
		super(message);
	}

	public UserNotAdminException(Throwable cause) {
		super(cause);
	}

	public UserNotAdminException(String message, Throwable cause) {
		super(message, cause);
	}
}
