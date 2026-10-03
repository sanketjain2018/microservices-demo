package in.sj.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String correlationId =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        ServerHttpRequest updatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header(CORRELATION_ID_HEADER, correlationId)
                        .build();

        ServerWebExchange updatedExchange =
                exchange.mutate()
                        .request(updatedRequest)
                        .build();

        updatedExchange.getResponse()
                .getHeaders()
                .add(CORRELATION_ID_HEADER, correlationId);

        return chain.filter(updatedExchange);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}