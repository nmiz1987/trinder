package iob.exceptions;

public class UnimplementedOperationException extends RuntimeException {

	private static final long serialVersionUID = -1502579623997236791L;

	public UnimplementedOperationException() {
		super();
	}

	public UnimplementedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnimplementedOperationException(String message) {
		super(message);
	}

	public UnimplementedOperationException(Throwable cause) {
		super(cause);
	}
}
