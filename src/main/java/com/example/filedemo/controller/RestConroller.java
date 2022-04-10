package com.example.filedemo.controller;

import com.example.filedemo.computation.io.FileCopier;
import com.example.filedemo.computation.memory.MyBeanCalc;
import com.example.filedemo.computation.cpu.Fibonnaci;
import com.example.filedemo.computation.cpu.Prime;
import com.example.filedemo.computation.io.FileSizeCalc;
import com.example.filedemo.computation.memory.StringSizeCalc;
import com.example.filedemo.computation.network.CreateNetworkData;
import com.example.filedemo.computation.network.GenerateNetworkTraffic;
import com.example.filedemo.computation.network.GetNetworkTraffic;
import com.example.filedemo.responses.*;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class RestConroller {

    private static final Logger logger = LoggerFactory.getLogger(RestConroller.class);

    @Autowired
    FileSizeCalc fileSizeCalc = new FileSizeCalc();

    @Autowired
    StringSizeCalc stringSizeCalc = new StringSizeCalc();

    @Autowired
    MyBeanCalc myBeanCalc = new MyBeanCalc();

    @Autowired
    GenerateNetworkTraffic generateNetworkTraffic = new GenerateNetworkTraffic();

    @Autowired
    private static final CreateNetworkData createNetworkData = new CreateNetworkData(20000);

    @Autowired
    private RestartEndpoint restartEndpoint;

    @GetMapping("/restart")
    public void restart(HttpServletRequest request) {
        restartEndpoint.restart();
    }
    
    @GetMapping("/gc")
    public void garbage(HttpServletRequest request) {
        System.gc();
        Runtime.gc();
    }

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
    public WaitResponse wait(@PathVariable String waitTime) {

        long start = System.currentTimeMillis();

        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        WaitResponse response = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime);

        return response;
    }

    @GetMapping("/waiting/{waitTime}")
    public WaitResponse waiting(@PathVariable String waitTime) {

        long start = System.currentTimeMillis();

        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        WaitResponse response = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime);

        return response;
    }

    @GetMapping("/cpu/fibonacci/{number}")
    public CpuResponse cpu(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Fibonnaci fibonnaci = new Fibonnaci();

        Long result = 0L;

        try {
            result = fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime);

        System.gc();
        Runtime.gc();

        return response;
    }

    @GetMapping("/cpu/prime/{number}")
    public CpuResponse prime(@PathVariable String number) {

        Prime prime = new Prime();

        CpuResponse response = null;

        try {
            response = prime.run(Integer.parseInt(number));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/io/1/{path:.+}/{iteration}")
    public IoResponse directorySize(@PathVariable String path, @PathVariable String iteration) {

        FileSizeCalc fileSizeCalc = new FileSizeCalc();

        IoResponse response = null;

        try {
            response = fileSizeCalc.run(Integer.parseInt(iteration), "c:\\winutils-master");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/io/2/{path:.+}/{iteration}")
    public IoResponse directorySizeAutowired(@PathVariable String path, @PathVariable String iteration) {

        IoResponse results = null;

        try {
            final IoResponse result = fileSizeCalc.run(Integer.parseInt(iteration), "c:\\winutils-master");
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/memory/string/1/{number}")
    public GenericResponse stringSizeCalc(@PathVariable String number) {

        StringSizeCalc stringSizeCalc = new StringSizeCalc();

        GenericResponse results = null;

        try {
            final GenericResponse result = stringSizeCalc.run(Integer.parseInt(number));
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/memory/beans/1/{number}/{withGC}")
    public GenericResponse myBeanCalc(@PathVariable int number, @PathVariable boolean withGC) {

        MyBeanCalc myBeanCalc = new MyBeanCalc();

        GenericResponse results = null;

        try {
            final GenericResponse result = myBeanCalc.memoryTest(number, withGC);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/memory/beans/2/{number}/{withGC}")
    public GenericResponse myBeanCalcAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        GenericResponse results = null;

        try {
            final GenericResponse result = myBeanCalc.memoryTest(number, withGC);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/network/1/{number}/{withGC}")
    public NetworkResponse generateNetworkTraffic(@PathVariable int number, @PathVariable boolean withGC) {

        GenerateNetworkTraffic generateNetworkTraffic = new GenerateNetworkTraffic();

        NetworkResponse results = null;

        try {
            final NetworkResponse result = generateNetworkTraffic.run(number, withGC);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @GetMapping("/network/2/{number}/{withGC}")
    public NetworkResponse generateNetworkTrafficAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        NetworkResponse results = null;

        try {
            final NetworkResponse result = generateNetworkTraffic.run(number, withGC);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    // Itt is az a lényeg, hogy egy példányból kéri el az adatokat és nem hozza őket létre újra és újra
    @GetMapping("/network/1/2/{number}")
    public NetworkResponse getNetworkTrafficAutowired(@PathVariable int number) {

        /*
        NetworkResponse results = null;

        try {
            final NetworkResponse result = createNetworkData.run(number);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        ArrayList<String> dataList = this.createNetworkData.getList();

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        NetworkResponse results = new NetworkResponse("NetworkResponse", 1, 1, elapsedTime, dataList);

        return results;
    }

    // Itt a run method megkapja createNetworkData példányt és nem hoz létre újra és újra
    @GetMapping("/network/2")
    public NetworkResponse one() {

        GetNetworkTraffic getNetworkTraffic = new GetNetworkTraffic();

        NetworkResponse results = null;

        try {
            final NetworkResponse result = getNetworkTraffic.run(createNetworkData, 0, false);
            results = result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/io/copy/{times}")
    public IoResponse copyTest(@PathVariable int times) {

        FileCopier fileCopier = new FileCopier(fileStorageService);

        IoResponse result = fileCopier.run(times);

        return result;
    }

}
