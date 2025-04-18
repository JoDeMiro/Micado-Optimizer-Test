package com.example.filedemo.service;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SleuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Tracer tracer;

    @Autowired
    public SleuthService(Tracer tracer) {
        this.tracer = tracer;
    }

    public void doSomeWorkSameSpan() {
        try {
            Thread.sleep(1000L);
            logger.info("Doing some work");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doSomeWorkNewSpan() throws InterruptedException {
        logger.info("I'm in the original span");

        Span newSpan = tracer.nextSpan().name("newSpan").start();
        try (SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            Thread.sleep(1000L);
            logger.info("I'm in the new span doing some cool work that needs its own span");
        } finally {
            newSpan.finish();
        }

        logger.info("I'm in the original span");
    }
}
