package com.example.filedemo.controller;

import com.example.filedemo.beans.InfoBean;
import com.example.filedemo.beans.MemoryStats;
import com.example.filedemo.beans.MyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelperController {

    @Autowired
    InfoBean infoBean = new InfoBean();

    @Autowired
    MyBean myBean = new MyBean();


    @GetMapping("/memory-status")
    public MemoryStats getMemoryStatistics() {
        MemoryStats stats = new MemoryStats();
        stats.setHeapSize(Runtime.getRuntime().totalMemory()/(1024*1024));
        stats.setHeapMaxSize(Runtime.getRuntime().maxMemory()/(1024*1024));
        stats.setHeapFreeSize(Runtime.getRuntime().freeMemory()/(1024*1024));
        return stats;
    }

    @GetMapping("/info")
    public InfoBean getApplicationProperties() {

        System.out.println(infoBean);

        return infoBean;

    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Map<String, Object> hello(@RequestParam(value = "name", defaultValue = "Micado-Tester_service") String name) {
        Map<String, Object> result = new HashMap<>();
        result.put("greeting", "Hello " + name + "!");
        return result;
    }

    @GetMapping("/helloMyBean")
    public String helloMyBean() {

        myBean.run();

        String result = myBean.getName();

        return result;
    }

}
