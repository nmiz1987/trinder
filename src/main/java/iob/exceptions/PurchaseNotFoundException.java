package iob.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PurchaseNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 808145515962768921L;

	public PurchaseNotFoundException() {
	}

	public PurchaseNotFoundException(String message) {
		super(message);
	}

	public PurchaseNotFoundException(Throwable cause) {
		super(cause);
	}

	public PurchaseNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
