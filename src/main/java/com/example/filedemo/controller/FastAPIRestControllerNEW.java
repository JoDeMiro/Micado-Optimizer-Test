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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/quiskitNEW")
public class FastAPIRestControllerNEW {

    private final String fastApiBaseUrl;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FastAPIRestControllerNEW.class);

    private static String ipAddress;
    private static String hostName;

    static {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddress = ip.getHostAddress();
            hostName = ip.getHostName();
        } catch (UnknownHostException e) {
            ipAddress = "unknown";
            hostName  = "unknown";
            logger.error("Nem sikerült lekérdezni a lokális IP/host nevet", e);
        }
    }

    public FastAPIRestControllerNEW(
            @Value("${qc.fastapi.base-url}") String fastApiBaseUrl
    ) {
        this.fastApiBaseUrl = fastApiBaseUrl;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/{method}/{qubits}/{depth}/{shots}")
    public ResponseEntity<Map<String, Object>> heavy(
            @PathVariable("method") String method,
            @PathVariable("qubits") int qubits,
            @PathVariable("depth") int depth,
            @PathVariable("shots") int shots
    ) {

        // FastAPI URL
        String url = String.format(
                "%s/qcproc/%s/%d/%d/%d",
                fastApiBaseUrl, method, qubits, depth, shots
        );

        String requestId = UUID.randomUUID().toString();
        long tStart = System.nanoTime();

        try {
            long tApiStart = System.nanoTime();
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);
            long tApiEnd = System.nanoTime();

            long backendLatencyMs = (tApiEnd - tApiStart) / 1_000_000;
            long springLatencyMs  = (System.nanoTime() - tStart) / 1_000_000;

            // "Envelope" objektum – NEM nyúlunk a FastAPI JSON-hoz,
            // csak betesszük a backendBody mezőbe STRING-ként.
            Map<String, Object> envelope = new HashMap<>();
            envelope.put("workerIPAddress", ipAddress);
            envelope.put("workerHostName", hostName);
            envelope.put("springLatencyMs", springLatencyMs);
            envelope.put("backendLatencyMs", backendLatencyMs);
            envelope.put("backendStatus", response.getStatusCodeValue());
            envelope.put("requestId", requestId);
            envelope.put("backendBody", response.getBody()); // <-- eredeti FastAPI JSON, String-ben

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(envelope);

        } catch (RestClientException e) {
            long springLatencyMs = (System.nanoTime() - tStart) / 1_000_000;

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "FastAPI backend hiba");
            errorBody.put("message", e.getMessage());
            errorBody.put("workerIPAddress", ipAddress);
            errorBody.put("workerHostName", hostName);
            errorBody.put("springLatencyMs", springLatencyMs);
            errorBody.put("requestId", requestId);

            return ResponseEntity
                    .status(502)
                    .body(errorBody);

        } catch (Exception e) {
            long springLatencyMs = (System.nanoTime() - tStart) / 1_000_000;

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "Ismeretlen hiba a Spring oldalon");
            errorBody.put("message", e.getMessage());
            errorBody.put("workerIPAddress", ipAddress);
            errorBody.put("workerHostName", hostName);
            errorBody.put("springLatencyMs", springLatencyMs);
            errorBody.put("requestId", requestId);

            return ResponseEntity
                    .status(500)
                    .body(errorBody);
        }
    }
}
