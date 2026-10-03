package in.sj.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.sj.config.FeignClientConfig;
import in.sj.config.FeignCorrelationIdConfig;
import in.sj.dto.UserResponse;

@FeignClient(name = "user-service", configuration = { FeignClientConfig.class, FeignCorrelationIdConfig.class })
public interface UserClient {

	@GetMapping("/users/{id}")
	UserResponse getUserById(@PathVariable("id") Long id);

}
