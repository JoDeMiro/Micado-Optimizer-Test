package com.example.filedemo.controller;

import com.example.filedemo.beans.InfoStats;
import com.example.filedemo.computation.cpu.*;
import com.example.filedemo.computation.download.StaticDownloader;
import com.example.filedemo.computation.imoges.ImageManipulator;
import com.example.filedemo.computation.io.FileCopier;
import com.example.filedemo.computation.memory.MyBeanCalc;
import com.example.filedemo.computation.io.FileSizeCalc;
import com.example.filedemo.computation.memory.StringSizeCalc;
import com.example.filedemo.computation.network.CreateNetworkData;
import com.example.filedemo.computation.network.DownloadWebPage;
import com.example.filedemo.computation.network.GenerateNetworkTraffic;
import com.example.filedemo.computation.network.GetNetworkTraffic;
import com.example.filedemo.conf.HttpTraceLogConfiguration;
import com.example.filedemo.responses.*;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

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
            e.printStackTrace();
        }
    }

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
    DownloadWebPage downloadWebPage = new DownloadWebPage();

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
    InfoStats infoStats = new InfoStats();

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private StaticDownloader staticDownloader;

    @Autowired
    private WaitResponse waitResponse;

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
    public List<String> os(HttpServletRequest request) {
        ArrayList<String> list = new ArrayList<>();

        System.getProperties().list(System.out);

        Properties properties = System.getProperties();

        String os = properties.getProperty("os.name");
        System.out.println("-----------------------");
        System.out.println(os);
        System.out.println("-----------------------");

        return list;
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
                if( temp.getName().contains("_") == false ){
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
            e.printStackTrace();
        }
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
        list.add(ipAddress);

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

        WaitResponse response = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime, ipAddress);

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

        waitResponse = new WaitResponse("WaitResponse", Long.parseLong(waitTime), elapsedTime, ipAddress);

        return waitResponse;
    }

    @GetMapping("/cpu/fibonacci/{number}")
    public CpuResponse cpu(@PathVariable String number) {

        long start = System.currentTimeMillis();

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
            result = fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/fibonacci_no_gc_new_instance/{number}")
    public CpuResponse cpu_no_gc_new_instance(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            // result = fibonnaci.run(Integer.parseInt(number));
            Fibonnaci fibonnaci = new Fibonnaci();
            result = fibonnaci.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/parallel_no_gc/{number}/{thread}")
    public CpuResponse cpu_no_gc_parallel(@PathVariable String number, @PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = parallel.run(Integer.parseInt(number), Integer.parseInt(thread));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/multitask_no_gc/summarizer/{thread}")
    public CpuResponse cpu_no_gc_multitask_summarizer(@PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;
        String number = "0";

        try {
            multitask.summarizer(Integer.parseInt(thread));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/multitask_no_gc/counter/{thread}")
    public CpuResponse cpu_no_gc_multitask_counter(@PathVariable String thread) {

        long start = System.currentTimeMillis();

        Long result = 0L;
        String number = "0";

        try {
            multitask.counter(Integer.parseInt(thread));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/fibonacci_no_gc/{number}/{ts}")
    public CpuResponse cpu_no_gc_ts(@PathVariable String number, @PathVariable String ts) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = fibonnaci.run(Integer.parseInt(number));
            String ts_a = ts;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ts, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/prime/{number}")
    public CpuResponse prime(@PathVariable String number) {

        CpuResponse response = null;

        try {
            response = prime.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/prime_no_gc/{number}")
    public CpuResponse prime_no_gc(@PathVariable String number) {

        CpuResponse response = null;

        try {
            response = prime.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/count/{number}")
    public CpuResponse count(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = count.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/count_no_gc/{number}")
    public CpuResponse count_no_gc(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = count.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/instance/{number}")
    public CpuResponse instance(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        try {
            result = instance.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            result = instance.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/individum/{number}")
    public CpuResponse individum(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        Individum individum = new Individum();

        try {
            result = individum.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        System.gc();
        Runtime.getRuntime().gc();

        return response;
    }

    @GetMapping("/cpu/individum_no_gc/{number}")
    public CpuResponse individum_no_gc(@PathVariable String number) {

        long start = System.currentTimeMillis();

        Long result = 0L;

        Individum individum = new Individum();

        try {
            result = individum.run(Integer.parseInt(number));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        CpuResponse response = new CpuResponse("CpuResponse", number, result, elapsedTime, ipAddress);

        // System.gc();
        // Runtime.getRuntime().gc();

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return response;
    }
    @GetMapping("/memory/string/1/{number}")
    public GenericResponse stringSizeCalc(@PathVariable String number) {

        StringSizeCalc stringSizeCalc = new StringSizeCalc();

        GenericResponse response = null;

        try {
            response = stringSizeCalc.run(Integer.parseInt(number));
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/memory/beans/1/{number}/{withGC}")
    public GenericResponse myBeanCalc(@PathVariable int number, @PathVariable boolean withGC) {

        MyBeanCalc myBeanCalc = new MyBeanCalc();

        GenericResponse response = null;

        try {
            response = myBeanCalc.memoryTest(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/memory/beans/2/{number}/{withGC}")
    public GenericResponse myBeanCalcAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        GenericResponse response = null;

        try {
            response = myBeanCalc.memoryTest(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/network/1/{number}/{withGC}")
    public NetworkResponse generateNetworkTraffic(@PathVariable int number, @PathVariable boolean withGC) {

        GenerateNetworkTraffic generateNetworkTraffic = new GenerateNetworkTraffic();

        NetworkResponse response = null;

        try {
            response = generateNetworkTraffic.run(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/network/2/{number}/{withGC}")
    public NetworkResponse generateNetworkTrafficAutowired(@PathVariable int number, @PathVariable boolean withGC) {

        NetworkResponse response = null;

        try {
            response = generateNetworkTraffic.run(number, withGC);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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

        NetworkResponse response = new NetworkResponse("NetworkResponse", 1, 1, elapsedTime, dataList);
        response.setWorkerIPAddress(ipAddress);

        return response;
    }

    // Itt a run method megkapja createNetworkData példányt és nem hoz létre újra és újra
    @GetMapping("/network/2")
    public NetworkResponse one() {

        GetNetworkTraffic getNetworkTraffic = new GetNetworkTraffic();

        NetworkResponse response = null;

        try {
            response = getNetworkTraffic.run(createNetworkData, 0, false);
            response.setWorkerIPAddress(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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

        String result = staticDownloader.getStaticData(length);

        return result;
    }

}
