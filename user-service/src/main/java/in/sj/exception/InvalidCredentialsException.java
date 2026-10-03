package in.sj.exception;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}