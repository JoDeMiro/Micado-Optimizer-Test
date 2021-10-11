package com.example.filedemo.controller;

import com.example.filedemo.queues.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

    @Autowired
    Factory factory;

    @GetMapping("/queue")
    public String doJob() {

        factory.run();

        return null;
    }

}