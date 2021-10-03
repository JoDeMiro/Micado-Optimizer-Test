package com.example.filedemo.controller;

import com.example.filedemo.beans.InfoBean;
import com.example.filedemo.beans.MemoryStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelperController {

    @Autowired
    InfoBean infoBean = new InfoBean();


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

}
