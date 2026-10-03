package in.sj.exception;

@SuppressWarnings("serial")
public class UserServiceClientException extends RuntimeException{
	
	public UserServiceClientException(String message) {
		super(message);
	}
}
