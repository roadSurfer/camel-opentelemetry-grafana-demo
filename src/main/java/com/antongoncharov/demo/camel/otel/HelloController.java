package com.antongoncharov.demo.camel.otel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * @author antongoncharov
 */
@Controller
public class HelloController {
    private static final Logger logger = Logger.getLogger(HelloController.class.getName());

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(@RequestParam String name) {
        logger.info("Saying 'Hi'' to " + name);
        return "Hi, " + name;
    }

}
