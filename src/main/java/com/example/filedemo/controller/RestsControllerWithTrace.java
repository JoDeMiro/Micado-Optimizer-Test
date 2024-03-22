package com.example.filedemo.controller;

import brave.Span;
import brave.SpanCustomizer;
import brave.Tracer;
import com.example.filedemo.service.SleuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestsControllerWithTrace {

    private static final Logger logger = LoggerFactory.getLogger(RestsControllerWithTrace.class);

    @Value("${remote.service.access.point}")
    private String remoteServiceAccessPoint;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Tracer tracer;


    @RequestMapping("/A")
    public String A() throws InterruptedException {
        logger.info("you called A");
        Thread.sleep(200);
        return "Hello World";
    }

    @RequestMapping("/B")
    public String B() throws InterruptedException {
        logger.info("you called B");
        Thread.sleep(500);
        return restTemplate.getForObject("http://localhost:8080/A", String.class);
    }

    @RequestMapping("/C")
    public String C() throws InterruptedException {
        logger.info("you called C");
        Thread.sleep(1000);
        return restTemplate.getForObject("http://localhost:8080/B", String.class);
    }

    @RequestMapping("/RB")
    public String RB() throws InterruptedException {
        logger.info("you called B");
        Thread.sleep(500);
        return restTemplate.getForObject("http://localhost/A", String.class);
    }

    @RequestMapping("/RC")
    public String RC() throws InterruptedException {
        logger.info("you called RC");
        Thread.sleep(1000);
        return restTemplate.getForObject("http://" + remoteServiceAccessPoint + "/RB", String.class);
    }





    @Autowired
    private SleuthService sleuthService;

    @GetMapping("/same-span")
    public String helloSleuthSameSpan() throws InterruptedException {
        logger.info("Same Span");
        sleuthService.doSomeWorkSameSpan();
        return "success";
    }

    @GetMapping("/new-span")
    public String helloSleuthNewSpan() throws InterruptedException {
        logger.info("New Span");
        sleuthService.doSomeWorkNewSpan();
        return "success";
    }











    @RequestMapping("/RPrime")
    public String RPrime() throws InterruptedException {
        logger.info("you called RPrime");
        Thread.sleep(1000);

        String url = "http://" + remoteServiceAccessPoint + "/cpu/prime/10";
        System.out.println(url);
        return restTemplate.getForObject(url, String.class);
    }

    @RequestMapping(value = "/RP/{interest}")
    public String D(@PathVariable String interest) throws InterruptedException {
        logger.info("you called RP");
        Thread.sleep(1000);

        String url = "http://" + remoteServiceAccessPoint + "/cpu/prime/10";

        String responseNewType = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, interest).getBody();

        logger.info("Return value of responseNewType");
        System.out.println(responseNewType);
        logger.info("-------------------------------");

        String responseOldType = restTemplate.getForObject(url, String.class);

        return responseOldType;
    }

    @RequestMapping("/RD")
    public String RD() throws InterruptedException {
        logger.info("you called RD");
        Thread.sleep(500);

        // Ez a megoldás nem jó
        // RestTemplate restTemplate0 = getRestTemplate();
        // return restTemplate0.getForObject("http://localhost:8082/A", String.class);

        // Ez amit Bean-ben van és ha jól értem akkor Autowire-d és jó megoldás
        return restTemplate.getForObject("http://localhost:8082/A", String.class);
    }

    // Most éppen ezt vizsgálgatok, hogy kell-e külön a ResponseEntytit megcsálnom
    // vagy működik-e anélkül is
    // Ha így csinálom meg akkor megjelenik ugyan a zipkinbe, de nincs összekötve
    // Hanem külön trace ID-t kap hiába hív át egy másik APP-bva egy másik servicebe
    @GetMapping("/ZA")
    public String za() {
        logger.info("Hello with Sleuth ZA");
        Span span = tracer.currentSpan();
        if (span != null) {
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return restTemplate.getForObject("http://localhost:8082/ZB", String.class);
    }

    @GetMapping("/ZB")
    public String zb() {
        logger.info("Hello with Sleuth ZB");
        Span span = tracer.currentSpan();

        // Extra information attached to the span
        SpanCustomizer customizer = span.customizer();
        customizer.tag("egyke", "kettőke");

        if (span != null) {
            logger.info("Span ID decimal {}", span.context().spanId());
            logger.info("Trace ID decimal {}", span.context().traceId());
        }
        return "Hello ZB";
    }



    // Ez is jó megoldás, az összes a Bean restTemplate objektumot használja ez nem

    // Ehhez egyébként kétszer kell elindítani a Springet.
    // Az egyik a 8080 porton fut
    // A másik viszont a 8082 proton fut
    // ava -Xms1024m -Xmx2048m -jar file-demo-0.0.1-SNAPSHOT.jar --server.port=8082 --spring.application.name=ServiceB

    @GetMapping("/KA")
    public String ka() {
        logger.info("Hello KA");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "farok");

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8082/KB",
                HttpMethod.GET,
                request,
                String.class
        );

        String result = response.getBody();
        logger.info("Reply = " + result);

        return "Hello from /a - " + "Faszom" + ", " + result;
    }

    @GetMapping("/KB")
    String kb(@RequestHeader(name = "Authorization") String authToken) {
        logger.info("Handling b - " + "appName");
        return "Hello from /b - " + "appName";
    }

}
