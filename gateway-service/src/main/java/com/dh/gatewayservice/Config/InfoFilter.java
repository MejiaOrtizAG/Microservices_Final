package com.dh.gatewayservice.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class InfoFilter extends AbstractGatewayFilterFactory<InfoFilter.Config>
{
    public static Logger LOG = LoggerFactory.getLogger(InfoFilter.class);

    public InfoFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config)
    {
        return (((exchange, chain) ->
        {
            LOG.info("Request Endpoint " + exchange.getRequest().getPath());
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
            {
                LOG.info("Instance Port: " + exchange.getResponse().getHeaders().get("port"));
                LOG.info("Request Process Finished");
            }));
        }));
    }

    public static class Config {
    }
}
