package in.sj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@GetMapping("/test")
	public String eurekaServerMsg() {
		return "Eureka Server Running on Port No : 8761 ";
	}
	
}
