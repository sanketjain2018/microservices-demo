package in.sj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
	
	@GetMapping("/config")
	public String getConfigMsg() {
		return "Config Server Running On Port No: 8888";
	}
}
