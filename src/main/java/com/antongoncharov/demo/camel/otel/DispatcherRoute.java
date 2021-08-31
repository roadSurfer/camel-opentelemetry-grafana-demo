package com.antongoncharov.demo.camel.otel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author antongoncharov
 */
@Component
public class DispatcherRoute extends RouteBuilder {
    private static final Logger logger = Logger.getLogger(DispatcherRoute.class.getName());

    private final ServiceProperties serviceProperties;

    @Autowired
    public DispatcherRoute(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @Override
    public void configure() {

        logger.info("Creating tracer");
        OpenTelemetryTracer ott = new OpenTelemetryTracer();
        ott.init(this.getContext());
        logger.info("Tracer created");

        from("activemq:queue:dispatcher").routeId("dispatcher-route")
            .log("incoming request, headers = ${headers}")
            .log("incoming request, body = ${body}")
            .to("direct:sayHello");

        from("direct:sayHello").routeId("sayHello-route")
            .log("outgoing request, headers = ${headers}")
            .log("outgoing request, body = ${body}")
            .log("dispatching to /hello endpoint")
            .setHeader("status", constant("dispatched"))
            .toD("undertow:" + serviceProperties.getDownstreamEndpoint() + "?name=${body}")
            .convertBodyTo(String.class);
    }
}

