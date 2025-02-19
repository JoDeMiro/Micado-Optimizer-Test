package com.example.filedemo.controller;

import brave.Span;
import brave.Tracer;
import com.example.filedemo.beans.InfoStats;
import com.example.filedemo.computation.cpu.*;
import com.example.filedemo.computation.download.StaticDownloader;
import com.example.filedemo.computation.ffmpeg.FFMpeg;
import com.example.filedemo.computation.imoges.ImageManipulator;
import com.example.filedemo.computation.io.FileCopier;
import com.example.filedemo.computation.memory.MyBeanCalc;
import com.example.filedemo.computation.io.FileSizeCalc;
import com.example.filedemo.computation.memory.StringSizeCalc;
import com.example.filedemo.computation.network.CreateNetworkData;
import com.example.filedemo.computation.network.DownloadWebPage;
import com.example.filedemo.computation.network.GenerateNetworkTraffic;
import com.example.filedemo.computation.network.GetNetworkTraffic;
import com.example.filedemo.computation.stress.StressNG;
import com.example.filedemo.computation.zip.Zip;
import com.example.filedemo.conf.HttpTraceLogConfiguration;
import com.example.filedemo.responses.*;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.zip.GZIPOutputStream;

@RestController
public class RestsController {

    private static final Logger logger = LoggerFactory.getLogger(RestsController.class);

    private static String ipAddress;

    Counter visitCounter;

    static {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddress = ip.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("RestsController getLocalHost()", e);
        }
    }

    @Autowired
    private Tracer tracer;

    @Autowired
    FileSizeCalc fileSizeCalc = new FileSizeCalc();

    // @Autowired
    // StringSizeCalc stringSizeCalc = new StringSizeCalc();

    @Autowired
    MyBeanCalc myBeanCalc = new MyBeanCalc();

    @Autowired
    GenerateNetworkTraffic generateNetworkTraffic = new GenerateNetworkTraffic();

    @Autowired
    private static final CreateNetworkData createNetworkData = new CreateNetworkData(20000);

    // @Autowired
    // DownloadWebPage downloadWebPage = new DownloadWebPage();

    // @Autowired
    // private RestartEndpoint restartEndpoint;

    @Autowired
    Fibonnaci fibonnaci = new Fibonnaci();

    @Autowired
    Parallel parallel = new Parallel();

    @Autowired
    Multitask multitask = new Multitask();

    @Autowired
    Count count = new Count();

    @Autowired
    Instance instance = new Instance();

    @Autowired
    Prime prime = new Prime();

    @Autowired
    Primer primer = new Primer();

    @Autowired
    InfoStats infoStats = new InfoStats();

    @Autowired
    StressNG stress = new StressNG();

    @Autowired
    Zip zip = new Zip();

    @Autowired
    FFMpeg ffmpeg = new FFMpeg();

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private StaticDownloader staticDownloader;

    // @Autowired
    // private WaitResponse waitResponse;

    @Autowired
    private HttpTraceLogConfiguration httpTraceLogConfiguration;


    public RestsController(MeterRegistry registry) {
        visitCounter = Counter.builder("visit_counter")
                .description("Number of visits to the site")
                .register(registry);
    }

    // @GetMapping("/restart")
    // public void restart(HttpServletRequest request) {
    //    restartEndpoint.restart();
    // }

    @GetMapping("/counter")
    public String counter(HttpServletRequest request) {
        visitCounter.increment();
        return "Hello Counter. Counter is available in /actuator/prometheus";
    }

    @GetMapping("/gc")
    public void garbage(HttpServletRequest request) {
        System.gc();
        Runtime.getRuntime().gc();
    }

    @GetMapping("/os")
    public String os(HttpServletRequest request) {

        Properties properties = System.getProperties();
        String results;
        results = properties.getProperty("os.name");

        return results;
    }

    @GetMapping("/stats")
    public List<String> stats(HttpServletRequest request) {
        ArrayList<String> list = infoStats.getUsage();

        list.add("response");
        list.add("Stats");
        list.add("InfoStats");
        list.add("ok");

        return  list;
    }

    public static void deleteDirectoryLegacyIO(File file) {

        File[] list = file.listFiles();
        if (list != null) {
            for (File temp : list) {
                if(!temp.getName().contains("_")){
                    //recursive delete
                    System.out.println("Visit " + temp);
                    deleteDirectoryLegacyIO(temp);
                }
            }
        }

        if (file.delete()) {
            System.out.printf("Delete : %s%n", file);
        } else {
            System.err.printf("Unable to delete file or directory : %s%n", file);
        }

    }

    @GetMapping("/cleardisk")
    public void cleardisk(HttpServletRequest request) {
        try {
            // Unix / Window Problem could be handled here

            Path uploadDirLocation = fileStorageService.getFileStorageLocation();
            System.out.println("fileStorageService.getFileStorageLocation = " + uploadDirLocation);

            File file = new File(String.valueOf(uploadDirLocation));
            deleteDirectoryLegacyIO(file);
        } catch (Exception e) {
            logger.error("/clear-disk/", e);
        }
    }

    @GetMapping("/wait")
    public List<String> wait(HttpServletRequest request) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("/wait/", e);
        }

        List<String> list = new ArrayList<>();
        list.add("response");
        list.add("wait = 1000");
        list.add("ok");
        list.add(ipAddress);

        return list;
    }

    @GetMapping("/wait/{waitTime}")
    public WaitResponse wait(@PathVariable String waitTime) {

        long start = System.currentTimeMillis();

        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            logger.error("/wait/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        WaitResponse response;
        response = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/waiting/{waitTime}")
    public WaitResponse waiting(@PathVariable String waitTime) {

        long start = System.currentTimeMillis();

        try {
            Thread.sleep(Long.parseLong(waitTime));
        } catch (InterruptedException e) {
            logger.error("/waiting/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        WaitResponse response;
        response = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/fibonacci/{number}")
    public CpuResponse cpu(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/fibonacci/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/fibonacci_no_gc/{number}")
    public CpuResponse cpu_no_gc(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/fibonacci/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/fibonacci_no_gc_new_instance/{number}")
    public CpuResponse cpu_no_gc_new_instance(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/fibonacci_no_gc_new_instance/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/parallel_no_gc/{number}/{thread}")
    public CpuResponse cpu_no_gc_parallel(@PathVariable String number, @PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Parallel.run(Integer.parseInt(number), Integer.parseInt(thread));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/parallel_no_gc/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/multitask_no_gc/summarizer/{thread}")
    public CpuResponse cpu_no_gc_multitask_summarizer(@PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            Multitask.summarizer(Integer.parseInt(thread));
        } catch (Exception e) {
            logger.error("/cpu/multitask_no_gc/summarizer/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", thread, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/multitask_no_gc/counter/{thread}")
    public CpuResponse cpu_no_gc_multitask_counter(@PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            Multitask.counter(Integer.parseInt(thread));
        } catch (Exception e) {
            logger.error("/cpu/multitask_no_gc/counter/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", thread, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/prime/{number}")
    public CpuResponse prime(@PathVariable String number) {

        CpuResponse response = new CpuResponse();

        try {
            response = Prime.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/cpu/prime/", e);
        }

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/prime_no_gc/{number}")
    public CpuResponse prime_no_gc(@PathVariable String number) {

        CpuResponse response = new CpuResponse();

        try {
            response = Prime.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/cpu/prime_no_gc/", e);
        }

        return response;
    }

    @GetMapping("/cpu/primer_no_gc/{number}")
    public CpuResponse primer_no_gc(@PathVariable String number) {

        CpuResponse response = new CpuResponse();

        try {
            response = Primer.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/cpu/primer_no_gc/", e);
        }

        return response;
    }

    @GetMapping("/cpu/count/{number}/{thread}")
    public CpuResponse count(@PathVariable String number, @PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Count.run(Integer.parseInt(number), Integer.parseInt(thread));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/count/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/count_no_gc/{number}/{thread}")
    public CpuResponse count_no_gc(@PathVariable String number, @PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Count.run(Integer.parseInt(number), Integer.parseInt(thread));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/count_no_gc/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/instance/{number}")
    public CpuResponse instance(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Instance.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/instance/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/instance_no_gc/{number}")
    public CpuResponse instance_no_gc(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Instance.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/instance_no_gc/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/cpu/individual/{number}")
    public CpuResponse individual(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Individum.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/individual/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/individual_no_gc/{number}")
    public CpuResponse individual_no_gc(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = Individum.run(Integer.parseInt(number));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/cpu/individual/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/io/1/{path:.+}/{iteration}")
    public IoResponse directorySize(@PathVariable String path, @PathVariable String iteration) {

        FileSizeCalc fileSizeCalc = new FileSizeCalc();

        IoResponse response = null;

        try {
            // response = fileSizeCalc.run(Integer.parseInt(iteration), "c:\\winutils-master");
            response = fileSizeCalc.run(Integer.parseInt(iteration), "/home/ubuntu/");
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/io/1/", e);
        }
        return response;
    }

    @GetMapping("/io/2/{path:.+}/{iteration}")
    public IoResponse directorySizeAutowired(@PathVariable String path, @PathVariable String iteration) {

        IoResponse response = null;

        try {
            // response = fileSizeCalc.run(Integer.parseInt(iteration), "c:\\winutils-master");
            response = fileSizeCalc.run(Integer.parseInt(iteration), "/home/ubuntu/");
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/io/2/", e);
        }
        return response;
    }

    @GetMapping("/io/3/")
    public IoResponse directorySizeAutowiredRequest(@RequestParam("path") String path, @RequestParam("iteration") String iteration) {

        IoResponse response = null;

        try {
            // response = fileSizeCalc.run(Integer.parseInt(iteration), "c:\\winutils-master");
            // response = fileSizeCalc.run(Integer.parseInt(iteration), "/home/ubuntu/");

            response = fileSizeCalc.run(Integer.parseInt(iteration), path);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/io/3/", e);
        }
        return response;
    }

    @GetMapping("/memory/string/1/{number}")
    public GenericResponse<String, Integer, Integer, Long> stringSizeCalc(@PathVariable String number) {

        StringSizeCalc stringSizeCalc = new StringSizeCalc();

        GenericResponse<String, Integer, Integer, Long>  response = null;

        try {
            response = stringSizeCalc.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/memory/string/1/", e);
        }

        return response;
    }

    @GetMapping("/memory/beans/1/{number}/{withGC}")
    public GenericResponse<String, Integer, Long, Long> myBeanCalc(@PathVariable int number, @PathVariable boolean withGC) {

        MyBeanCalc myBeanCalc = new MyBeanCalc();

        GenericResponse<String, Integer, Long, Long> response = null;

        try {
            response = myBeanCalc.memoryTest(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/memory/beans/1/", e);
        }
        return response;
    }

    @GetMapping("/memory/beans/2/{number}/{withGC}")
    public GenericResponse<String, Integer, Long, Long> myBeanCalcAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        GenericResponse<String, Integer, Long, Long> response = null;

        try {
            response = myBeanCalc.memoryTest(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/memory/beans/2/", e);
        }
        return response;
    }

    @GetMapping("/network/1/{number}/{withGC}")
    public NetworkResponse<String, Integer, Long, Long> generateNetworkTraffic(@PathVariable int number, @PathVariable boolean withGC) {

        GenerateNetworkTraffic generateNetworkTraffic = new GenerateNetworkTraffic();

        NetworkResponse<String, Integer, Long, Long> response = new NetworkResponse<>();

        try {
            response = generateNetworkTraffic.run(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/network/1/", e);
        }
        return response;
    }

    @GetMapping("/network/2/{number}/{withGC}")
    public NetworkResponse<String, Integer, Long, Long> generateNetworkTrafficAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        NetworkResponse<String, Integer, Long, Long> response = new NetworkResponse<>();

        try {
            response = generateNetworkTraffic.run(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/network/1/", e);
        }
        return response;
    }

    // Singleton
    @GetMapping("/network/1/2/{number}")
    public NetworkResponse<String, Integer, Long, Long> getNetworkTrafficAutowired(@PathVariable int number) {

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

        ArrayList<String> dataList = createNetworkData.getList();

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        NetworkResponse<String, Integer, Long, Long> response = new NetworkResponse("NetworkResponse", 1, 1, elapsedTime, dataList);
        response.setWorkerIPAddress(ipAddress);

        return response;
    }

    // Itt a run method megkapja createNetworkData példányt és nem hoz létre újra és újra
    @GetMapping("/network/2/")
    public NetworkResponse one() {

        GetNetworkTraffic getNetworkTraffic = new GetNetworkTraffic();

        NetworkResponse response = null;

        try {
            response = getNetworkTraffic.run(createNetworkData, 0, false);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/network/2/", e);
        }

        return response;
    }

    @GetMapping("/network/download/{number}/{withGC}")
    public NetworkResponse downloadWebPage(@PathVariable int number, @PathVariable boolean withGC) {

        DownloadWebPage downloadWebPage = new DownloadWebPage();

        NetworkResponse response = null;

        try {
            response = downloadWebPage.run(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            logger.error("/network/download/", e);
        }
        return response;
    }

    @GetMapping("/io/copy/{times}")
    public IoResponse copyTest(@PathVariable int times) {

        FileCopier fileCopier = new FileCopier(fileStorageService);

        IoResponse response = fileCopier.run(times);
        response.setWorkerIPAddress(ipAddress);

        return response;
    }

    @GetMapping("/image/manipulator/{size}/{times}")
    public IoResponse imageTest(@PathVariable int times, @PathVariable String size) {

        System.out.println("------------ 1 ---------------");
        ImageManipulator imageManipulator = new ImageManipulator(fileStorageService);

        System.out.println("------------ 2 ---------------");
        IoResponse response = imageManipulator.run(times, size);
        response.setWorkerIPAddress(ipAddress);

        return response;
    }

    @GetMapping("/downloader/static/{length}")
    public String downloader1(@PathVariable int length) {

        String result;
        result = staticDownloader.getStaticData(length);

        return result;
    }

    @GetMapping("/stress/test/{type}/{param}")
    public CpuResponse stress1(@PathVariable String type, @PathVariable String param) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            stress.test1(type, param);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/stress/test/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", param, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/zip/test/{type}")
    public CpuResponse zip_singleton(@PathVariable String type) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            zip.test1(type);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/zip/test/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", type, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/zip/M/test/{type}")
    public CpuResponse zip_instance(@PathVariable String type) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            // Not singleton
            // zip.test1(type);
            Zip zip = new Zip();
            zip.test1(type);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/zip/M/test/", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response;
        response = new CpuResponse("CpuResponse", type, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/test/1")
    public CpuResponse ffmpeg_singleton() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            result = ffmpeg.test1();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/test/1", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/M/test/1")
    public CpuResponse ffmpeg_instance() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            // Not singleton
            // result = ffmpeg.test1();
            FFMpeg ffmpeg = new FFMpeg();
            result = ffmpeg.test1();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/M/test/1", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/test/3")
    public CpuResponse ffmpeg_instance3() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            result = ffmpeg.test3();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/M/test/3")
    public CpuResponse ffmpeg_instance3M() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            // Not singleton
            // result = ffmpeg.test1();
            FFMpeg ffmpeg = new FFMpeg();
            result = ffmpeg.test3();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/M/test/3", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }




    @GetMapping("/ffmpeg/test/3/{cpuLimit}")
    public CpuResponse ffmpeg_instance3CPU(@PathVariable String cpuLimit) {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            // Not singleton
            result = ffmpeg.test3(Integer.parseInt(cpuLimit));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/MP/test/3/{cpuLimit}")
    public CpuResponse ffmpeg_instance3MP(@PathVariable String cpuLimit) {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            // Not singleton
            // result = ffmpeg.test1();
            FFMpeg ffmpeg = new FFMpeg();
            result = ffmpeg.test3(Integer.parseInt(cpuLimit));
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/MP/test/3", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/test/4")
    public CpuResponse ffmpeg_instance4() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            result = ffmpeg.test4();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/test/4", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/M/test/4")
    public CpuResponse ffmpeg_instance4M() {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            // Not singleton
            // result = ffmpeg.test1();
            FFMpeg ffmpeg = new FFMpeg();
            result = ffmpeg.test4();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/M/test/4", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/ffmpeg/test/2/{n}")
    public CpuResponse stress1(@PathVariable String n) {

        long start = System.currentTimeMillis();

        long result = -400L;

        try {
            result = ffmpeg.test2(n);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("/ffmpeg/test/2", e);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        // result = -400 ha nem fut le a try
        // result = -900 ha ffmpeg.test() hibát dob
        // result = 1 ha ffmpeg.text() proces hibát ad vissza
        // result = 0 ha minden ok

        String number = "0";
        CpuResponse response;
        response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        return response;
    }

    @GetMapping("/traceid")
    public String getSleuthTraceId() {
        logger.info("Hello with Sleuth");
        Span span = tracer.currentSpan();
        if (span != null) {
            logger.info("Span ID hex {}", span.context().spanIdString());
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID hex {}", span.context().traceIdString());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return "Hello from Sleuth";
    }

    // In this case the response time should be changed exponentially related to the parameter
    @GetMapping("/processImageSize/{size}")
    public GenericResponse processImageSize(@PathVariable int size) {

        long start = System.currentTimeMillis();

        BufferedImage image = generateImage(size, size);

        // BufferedImage edgeImage = applyEdgeDetection(image);
        image = applyEdgeDetection(image);

        // BufferedImage blurredImage = applyGaussianBlur(edgeImage);
        image = applyGaussianBlur(image);

        // Save to  file (if needed)
        // ImageIO.write(image, "jpg", new File("edge_detected_image.jpg"));

        double averageIntensity = calculateAverageIntensity(image);

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        return new GenericResponse("ImageProcess", size, averageIntensity, elapsedTime, ipAddress);
    }

    // In this case the response time should be changed linearly related to the parameter
    @GetMapping("/processImageFor/{times}")
    public GenericResponse processImageFor(@PathVariable int times) {

        long start = System.currentTimeMillis();

        int size = 20;

        double averageIntensity = 0.0;

        for (int i = 0; i < times; i++) {
            BufferedImage image = generateImage(size, size);

            // BufferedImage edgeImage = applyEdgeDetection(image);
            image = applyEdgeDetection(image);

            // BufferedImage blurredImage = applyGaussianBlur(edgeImage);
            image = applyGaussianBlur(image);

            averageIntensity = calculateAverageIntensity(image);
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        return new GenericResponse("ImageProcess", size, averageIntensity, elapsedTime, ipAddress);
    }

    // Create an image with the given size, with random colors
    public static BufferedImage generateImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Random color pixels
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = new Color((int)(Math.random() * 0xFFFFFF)).getRGB();
                image.setRGB(x, y, rgb);
            }
        }
        graphics.dispose();
        return image;
    }

    // Simple edge detection (Sobel filter implementation)
    public static BufferedImage applyEdgeDetection(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[][] filter = {
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };

        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                int sum = 0;
                for (int fx = 0; fx < 3; fx++) {
                    for (int fy = 0; fy < 3; fy++) {
                        int rgb = new Color(image.getRGB(x + fx - 1, y + fy - 1)).getRed();
                        sum += rgb * filter[fx][fy];
                    }
                }

                // Clamping the result to fit in the range [0, 255]
                int edgeColor = Math.min(Math.max(sum, 0), 255);
                result.setRGB(x, y, new Color(edgeColor, edgeColor, edgeColor).getRGB());
            }
        }

        return result;
    }

    // Gaussian Blur apply (simple 3x3-as kernel implementation)
    public static BufferedImage applyGaussianBlur(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 3x3-as Gaussian filter
        float[][] kernel = {
                {1 / 16f, 2 / 16f, 1 / 16f},
                {2 / 16f, 4 / 16f, 2 / 16f},
                {1 / 16f, 2 / 16f, 1 / 16f}
        };

        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                float redSum = 0, greenSum = 0, blueSum = 0;

                // A 3x3-as kernel alkalmazása
                for (int fx = 0; fx < 3; fx++) {
                    for (int fy = 0; fy < 3; fy++) {
                        int rgb = image.getRGB(x + fx - 1, y + fy - 1);
                        Color color = new Color(rgb);

                        redSum += color.getRed() * kernel[fx][fy];
                        greenSum += color.getGreen() * kernel[fx][fy];
                        blueSum += color.getBlue() * kernel[fx][fy];
                    }
                }

                // Színek összekapcsolása és a képre alkalmazás
                int red = Math.min(Math.max((int) redSum, 0), 255);
                int green = Math.min(Math.max((int) greenSum, 0), 255);
                int blue = Math.min(Math.max((int) blueSum, 0), 255);
                result.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }

        return result;
    }

    // Calculate mean pixel intensity
    public static double calculateAverageIntensity(BufferedImage image) {
        long sum = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int intensity = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                sum += intensity;
            }
        }

        return sum / (double) (image.getWidth() * image.getHeight());
    }

    @GetMapping("/compress/{size}")
    public GenericResponse compressText(@PathVariable int size) throws IOException {

        long start = System.currentTimeMillis();

        // Generate random String
        String randomText = generateRandomText(size);

        // Compress
        byte[] compressedData = compress(randomText);

        long fileSize = compressedData.length;
        double fileSizeInKB = fileSize / 1024.0;
        double originalSizeInKB = randomText.length() / 1024.0;

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;
        double elapsedTimeInSeconds = elapsedTime / 1000.0;
        String formattedTime = String.format("%.5f", elapsedTimeInSeconds);

        GenericResponse<String, Integer, Double, String> response = new GenericResponse<>("GZipResponse", size, fileSizeInKB, ipAddress);
        return  response;
    }

    // Generate Random text with given length
    private String generateRandomText(int size) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    // String GZIP compression
    private byte[] compress(String data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(data.getBytes());
        }
        return byteStream.toByteArray();
    }

    @GetMapping("/compressAndSave/{size}")
    public GenericResponse compressTextAndSave(@PathVariable int size) throws IOException {

        long start = System.currentTimeMillis();

        // Generate random String
        String randomText = generateRandomText(size);

        // Compress and Save file
        File tempFile = compressAndSaveToTempFile(randomText);

        // Get compressed file size
        long fileSize = tempFile.length();
        double fileSizeInKB = fileSize / 1024.0;
        double originalSizeInKB = randomText.length() / 1024.0;

        // Delete the compressed file
        if (tempFile.delete()) {
            long stop = System.currentTimeMillis();
            long elapsedTime = stop - start;
            double elapsedTimeInSeconds = elapsedTime / 1000.0;
            String formattedTime = String.format("%.5f", elapsedTimeInSeconds);

            GenericResponse<String, Integer, Double, Long> response = new GenericResponse<>("GZipResponse", size, fileSizeInKB, elapsedTime, ipAddress);
            return  response;
        } else {
            return new GenericResponse<>("GZipResponse Failed", "0", "0", ipAddress);
        }
    }

    // Compress Text and save to file
    private File compressAndSaveToTempFile(String data) throws IOException {
        // Create temp file with unique name
        File tempFile = File.createTempFile("compressed_data_", ".gz");

        // Write to file GZIP format
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             GZIPOutputStream gzipStream = new GZIPOutputStream(fos)) {
            gzipStream.write(data.getBytes());
        }

        return tempFile;
    }

}
