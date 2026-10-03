package in.sj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.sj.dto.UserRequest;
import in.sj.dto.UserResponse;
import in.sj.exception.UserNotFoundException;
import jakarta.validation.Valid;

@RestController
public class ConfigTestController {

	@Value("${app.message}")
	private String message;

	@Value("${app.owner}")
	private String owner;

	@GetMapping("/userconfig")
	public String getConfig() {

		return "Message: " + message + "<br>Owner: " + owner;
	}

	@GetMapping("/users/{id}")
	public UserResponse getUserById(@PathVariable Long id) {

		if (id != 101) {
			throw new UserNotFoundException(id);
		}

		return new UserResponse(id, "Sanket");
	}
	
	@PostMapping("/users")
	public UserRequest createUser(
	        @Valid @RequestBody UserRequest userRequest) {

	    return userRequest;
	}
}