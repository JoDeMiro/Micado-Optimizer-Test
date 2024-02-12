package com.example.filedemo.controller;

import brave.Span;
import brave.Tracer;
import com.example.filedemo.service.SleuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestsControllerWithTrace {

    private static final Logger logger = LoggerFactory.getLogger(RestsControllerWithTrace.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Tracer tracer;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @RequestMapping("/A")
    public String A() throws InterruptedException {
        logger.info("you called A");
        Thread.sleep(200);
        return "Hello World";
    }

    @RequestMapping("/B")
    public String B() throws InterruptedException {
        logger.info("you called B");
        Thread.sleep(500);
        RestTemplate restTemplate = getRestTemplate();
        return restTemplate.getForObject("http://localhost:8080/A", String.class);
    }

    @RequestMapping("/C")
    public String C() throws InterruptedException {
        logger.info("you called C");
        Thread.sleep(1000);
        RestTemplate restTemplate = getRestTemplate();
        return restTemplate.getForObject("http://localhost:8080/B", String.class);
    }

    @GetMapping("/T1")
    public String t1() {
        logger.info("Hello with Sleuth T1");
        Span span = tracer.currentSpan();
        if (span != null) {
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return "Hello T1";
    }

    @GetMapping("/T2")
    public String t2() {
        logger.info("Hello with Sleuth T2");
        RestTemplate restTemplate = getRestTemplate();
        Span span = tracer.currentSpan();
        if (span != null) {
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return restTemplate.getForObject("http://localhost:8080/T1", String.class);
    }

    @GetMapping("/T21")
    public String t21() {
        logger.info("Hello with Sleuth T2");
        RestTemplate restTemplate = getRestTemplate();
        Span span = tracer.currentSpan();
        if (span != null) {
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return restTemplate.getForObject("http://localhost:8080/T2", String.class);
    }

    @Autowired
    private SleuthService sleuthService;

    @GetMapping("/same-span")
    public String helloSleuthSameSpan() throws InterruptedException {
        logger.info("Same Span");
        sleuthService.doSomeWorkSameSpan();
        return "success";
    }

    @GetMapping("/new-span")
    public String helloSleuthNewSpan() throws InterruptedException {
        logger.info("New Span");
        sleuthService.doSomeWorkNewSpan();
        return "success";
    }

}






