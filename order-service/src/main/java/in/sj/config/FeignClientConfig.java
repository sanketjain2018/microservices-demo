package in.sj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @SuppressWarnings("deprecation")
	@Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                3000,
                5000
        );
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserServiceErrorDecoder();
    }
    
}