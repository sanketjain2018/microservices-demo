package in.sj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.sj.client.UserClient;
import in.sj.dto.UserResponse;
import in.sj.exception.OrderNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderController {

    private final UserClient userClient;
    
    @SuppressWarnings("rawtypes")
	private final CircuitBreakerFactory circuitBreakerFactory;
    
    @Value("${server.port}")
    private String serverPort;

    @SuppressWarnings("rawtypes")
	public OrderController(
            UserClient userClient,
            CircuitBreakerFactory circuitBreakerFactory) {

        this.userClient = userClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/orders")
    public String getOrders(HttpServletRequest request) {

        String gatewayHeader =
                request.getHeader("X-Gateway-Request");

        return "Orders Service is working on port "
                + serverPort
                + " | Gateway Header: "
                + gatewayHeader;
    }

    @GetMapping("/orders/user/{id}")
    public Object getOrderUser(@PathVariable Long id) {

        CircuitBreaker circuitBreaker =
                circuitBreakerFactory.create("user-service");

        return circuitBreaker.run(
                () -> userClient.getUserById(id),
                throwable -> userServiceFallback(id)
        );
    }
    
    private UserResponse userServiceFallback(Long id) {

        return new UserResponse(
                id,
                "User Service is currently unavailable"
        );
    }
    
    @GetMapping("/orders/{id}")
    public String getOrderById(@PathVariable Long id) {

        if (id != 101L) {
            throw new OrderNotFoundException(id);
        }

        return "Order 101 found";
    }
}