package com.example.filedemo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://www.baeldung.com/spring-mvc-long-polling

@RestController
@RequestMapping("/queue")
public class LongPollingConroller {

    @GetMapping("/bake")
    public void hello() {
        System.out.println("Hello Baker");
    }

    @GetMapping("/bake/{bakedGood}")
    public void helloQueue(@PathVariable String bakedGood) {
        System.out.println("Hello " + bakedGood);
    }

    @GetMapping("/bake3/{bakedGood}")
    public DeferredResult<String> publisherThree(@PathVariable String bakedGood) {
        Integer bakeTime = 20;
        DeferredResult<String> output = new DeferredResult<>();
        try {
            Thread.sleep(bakeTime);
            output.setResult("Queue waited for " + bakeTime + " ms to complete and order dispatched. Result = " + bakedGood);
            System.out.println("Completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    // ----------------------------------------------------------------
    // This method wont be limited by anything but the the max.threads
    // mapped to the Apache Tom Cat Server
    // ----------------------------------------------------------------
    // It is expected to execute every request in the same time
    // ----------------------------------------------------------------
    @GetMapping("/bake4/{bakedGood}/{bakeTime}")
    public DeferredResult<String> publisherFour(@PathVariable String bakedGood, @PathVariable Integer bakeTime) {
        DeferredResult<String> output = new DeferredResult<>();
        try {
            Thread.sleep(bakeTime);
            output.setResult("Queue waited for " + bakeTime + " ms to complete and order dispatched. Result = " + bakedGood);
            System.out.println("Completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


    // --------------------------
    // I set Tread Number One = 1
    // --------------------------
    // It is expected to execute the second request only when the first request is processed.
    // So if two request is sent to the server right after each other than the second one's
    // response time will be the double of the first one's.
    private ExecutorService bakers = Executors.newFixedThreadPool(1);

    @GetMapping("/bake5/{bakedGood}/{bakeTime}")
    public DeferredResult<String> publisherFive(@PathVariable String bakedGood, @PathVariable Integer bakeTime) {
        DeferredResult<String> output = new DeferredResult<>();
        bakers.execute(() -> {
            try {
                Thread.sleep(bakeTime);
                output.setResult("Queue waited for " + bakeTime + " ms to complete and order dispatched. Result = " + bakedGood);
            } catch (Exception e) {
                // e.printStackTrace();
                output.setErrorResult("Something went wrong with your order!");
            }
        });
        return output;
    }

}
