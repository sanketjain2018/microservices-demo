package in.sj.exception;


@SuppressWarnings("serial")
public class OrderNotFoundException extends RuntimeException{

	public OrderNotFoundException(Long id){
		super("Order with ID "+ id + " Was not found");
	}
}
