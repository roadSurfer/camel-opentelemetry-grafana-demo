package com.antongoncharov.demo.camel.otel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.opentelemetry.OpenTelemetryTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author antongoncharov
 */
@Component
public class DispatcherRoute extends RouteBuilder {
    private final ServiceProperties serviceProperties;

    @Autowired
    public DispatcherRoute(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @Override
    public void configure() {

        OpenTelemetryTracer ott = new OpenTelemetryTracer();
        ott.init(this.getContext());

        from("activemq:queue:dispatcher").routeId("dispatcher-route")
                .log("incoming request, headers = ${headers}")
                .log("incoming request, body = ${body}")
                .to("activemq:queue:sayHello")
                .log("incoming response, headers = ${headers}")
                .log("incoming response, body = ${body}");

        from("activemq:queue:sayHello").routeId("sayHello-route")
                .log("dispatching to /hello endpoint")
                .setHeader("status", constant("dispatched"))
                .log("outgoing request, headers = ${headers}")
                .log("outgoing request, body = ${body}")
                .toD("undertow:" + serviceProperties.getDownstreamEndpoint() + "?name=${body}")
                .convertBodyTo(String.class)
                .log("outgoing response, headers = ${headers}")
                .log("outgoing response, body = ${body}");
    }
}

