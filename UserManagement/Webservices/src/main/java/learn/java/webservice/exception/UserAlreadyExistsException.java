package learn.java.webservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException() {
	}

	public UserAlreadyExistsException(String message) {
		super(message);
	}
	
}
