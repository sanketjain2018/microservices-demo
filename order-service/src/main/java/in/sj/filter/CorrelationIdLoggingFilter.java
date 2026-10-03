package in.sj.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CorrelationIdLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(CorrelationIdLoggingFilter.class);

    private static final String CORRELATION_ID_HEADER =
            "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId =
                request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = "MISSING";
        }

        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        try {
            log.info(
                    "Order Service request: {} {} | CorrelationId: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    correlationId
            );

            filterChain.doFilter(request, response);

        } finally {
            log.info(
                    "Order Service completed: {} {} | Status: {} | CorrelationId: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    correlationId
            );
        }
    }
}