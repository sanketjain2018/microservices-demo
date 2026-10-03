package in.sj.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignCorrelationIdConfig {

    private static final String CORRELATION_ID_HEADER =
            "X-Correlation-Id";

    @Bean
    public RequestInterceptor correlationIdInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                            RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return;
            }

            HttpServletRequest request =
                    attributes.getRequest();

            String correlationId =
                    request.getHeader(CORRELATION_ID_HEADER);

            if (correlationId != null &&
                    !correlationId.isBlank()) {

                requestTemplate.header(
                        CORRELATION_ID_HEADER,
                        correlationId
                );
            }
        };
    }
}