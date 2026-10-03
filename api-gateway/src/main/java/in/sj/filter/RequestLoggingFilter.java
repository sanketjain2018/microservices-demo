package in.sj.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String requestPath =
                exchange.getRequest().getURI().getPath();

        String method =
                exchange.getRequest().getMethod().name();

        long startTime = System.currentTimeMillis();

        log.info("Incoming request: {} {}", method, requestPath);

        return chain.filter(exchange)
                .doFinally(signalType -> {

                    long duration =
                            System.currentTimeMillis() - startTime;

                    Integer statusCode =
                            exchange.getResponse()
                                    .getStatusCode() != null
                                    ? exchange.getResponse()
                                    .getStatusCode().value()
                                    : null;

                    log.info(
                            "Completed request: {} {} | Status: {} | Time: {} ms",
                            method,
                            requestPath,
                            statusCode,
                            duration
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}