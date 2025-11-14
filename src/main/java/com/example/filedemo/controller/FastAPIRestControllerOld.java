package com.example.filedemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/quiskit____OLD")
public class FastAPIRestControllerOld {

    private final String fastApiBaseUrl;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FastAPIRestControllerOld.class);

    private static String ipAddress;

    static {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddress = ip.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("FastAPIRestController getLocalHost()", e);
        }
    }

    public FastAPIRestControllerOld(
            @Value("${qc.fastapi.base-url}") String fastApiBaseUrl
    ) {
        this.fastApiBaseUrl = fastApiBaseUrl;
        this.restTemplate = new RestTemplate();   // <-- saját RestTemplate példány
    }

    @GetMapping("/{method}/{qubits}/{depth}/{shots}")
    public ResponseEntity<String> heavy(
            @PathVariable("method") String method,
            @PathVariable("qubits") int qubits,
            @PathVariable("depth") int depth,
            @PathVariable("shots") int shots
    ) {

        // FastAPI endpoint URL összeállítása
        String url = String.format(
                "%s/qcproc/%s/%d/%d/%d",
                fastApiBaseUrl, method, qubits, depth, shots
        );

        try {
            // FastAPI hívása
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            // Továbbítjuk a FastAPI válaszát változtatás nélkül
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());

        } catch (RestClientException e) {
            // Ha a FastAPI nem elérhető vagy timeout -> 502 Bad Gateway
            return ResponseEntity
                    .status(502)
                    .body("FastAPI backend hiba: " + e.getMessage());
        }
    }
}
