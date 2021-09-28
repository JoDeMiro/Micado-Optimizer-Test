package com.example.filedemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestConroller {

    private static final Logger logger = LoggerFactory.getLogger(RestConroller.class);

    @GetMapping("/wait")
    public List<String> wait(HttpServletRequest request) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.add("response");
        list.add("wait = 1000");
        list.add("ok");

        return list;
    }

    @GetMapping("/wait/{waitTime}")
    public List<String> wait(@PathVariable String waitTime) {
        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.add("response");
        list.add("wait = " + waitTime);
        list.add("ok");

        return list;
    }

    @GetMapping("/waiting/{waitTime}")
    public WaitResponse waiting(@PathVariable String waitTime) {
        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WaitResponse response = new WaitResponse(Long.parseLong(waitTime), "hello");

        return response;
    }

}
