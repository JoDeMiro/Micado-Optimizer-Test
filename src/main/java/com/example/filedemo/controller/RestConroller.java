package com.example.filedemo.controller;

import com.example.filedemo.computation.cpu.Fibonnaci;
import com.example.filedemo.computation.io.FileSizeCalc;
import com.example.filedemo.responses.CpuResponse;
import com.example.filedemo.responses.WaitResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RestConroller {

    private static final Logger logger = LoggerFactory.getLogger(RestConroller.class);

    @Autowired
    FileSizeCalc fileSizeCalc = new FileSizeCalc();

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

    @GetMapping("/cpu/{number}")
    public CpuResponse cpu(@PathVariable String number) {

        // Start 10 executors

        long start = System.currentTimeMillis();

        Fibonnaci fibonnaci = new Fibonnaci();
        try {
            fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long timeElapsed = stop - start;

        System.out.println("start        = " + start);
        System.out.println("stop         = " + stop);
        System.out.println("elapsed (ms) = " + timeElapsed);

        CpuResponse response = new CpuResponse(timeElapsed, number, "CPU GET Request");

        return response;
    }

    @GetMapping("/io/1/{path}")
    public String directorySize(@PathVariable String path) {

        FileSizeCalc fileSizeCalc = new FileSizeCalc();

        String results = null;

        try {
            final String result = fileSizeCalc.run(10, "c:\\winutils-master");
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/io/2/{path}")
    public String directorySizeAutowired(@PathVariable String path) {

        String results = null;

        try {
            final String result = fileSizeCalc.run(10, "c:\\winutils-master");
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

}
