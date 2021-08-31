package com.antongoncharov.demo.camel.otel;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * @author antongoncharov
 */
@Controller
public class DispatchController {

    private final ProducerTemplate template;
    private static final Logger logger = Logger.getLogger(DispatchController.class.getName());

    public DispatchController(ProducerTemplate template) {
        this.template = template;
    }

    @GetMapping("/dispatch")
    @ResponseBody
    public String sayHello(@RequestParam String name) {
        logger.info("Got request for " + name);
        Object obj = template.sendBody("direct:dispatcher", ExchangePattern.InOut, name);
        logger.info("Sent request for " + name);
        return obj.toString();
    }

}
