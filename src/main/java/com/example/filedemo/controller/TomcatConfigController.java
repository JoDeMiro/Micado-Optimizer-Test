package com.example.filedemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TomcatConfigController {

    @Value("${server.tomcat.max-threads}")
    private int maxThreads;

    @Value("${server.tomcat.threads.min-spare}")
    private int minSpareThreads;

    @GetMapping("/tomcat-config")
    public String getTomcatConfig() {
        return "Max Threads: " + maxThreads + ", Min Spare Threads: " + minSpareThreads;
    }
}

