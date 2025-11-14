package com.example.filedemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/quiskit")
public class FastAPIRestController {

    private static final Logger logger = LoggerFactory.getLogger(FastAPIRestController.class);

    private final String fastApiBaseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static String ipAddress = "unknown";
    private static String hostName = "unknown";

    static {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddress = ip.getHostAddress();
            hostName = ip.getHostName();
        } catch (UnknownHostException e) {
            logger.error("FastAPIRestController getLocalHost()", e);
        }
    }

    public FastAPIRestController(
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

        String url = String.format(
                "%s/qcproc/%s/%d/%d/%d",
                fastApiBaseUrl, method, qubits, depth, shots
        );

        String requestId = UUID.randomUUID().toString();
        long t0 = System.nanoTime();

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            long t1 = System.nanoTime();
            long backendLatencyMs = (t1 - t0) / 1_000_000L;

            String body = response.getBody();

            Map<String, Object> envelope = new LinkedHashMap<>();
            envelope.put("requestId", requestId);
            envelope.put("backendStatus", response.getStatusCodeValue());
            envelope.put("workerHostName", hostName);
            envelope.put("workerIPAddress", ipAddress);
            envelope.put("backendLatencyMs", backendLatencyMs);
            envelope.put("springLatencyMs", backendLatencyMs); // közel ugyanaz ebben a setupban

            // 🔹 PRÓBÁLJUK MEG PARSE-OLNI a FastAPI JSON-t
            try {
                Object parsed = objectMapper.readValue(body, Object.class);
                envelope.put("backendBody", parsed);  // <-- beágyazott objektum, NINCSENEK perjelek
            } catch (Exception parseEx) {
                // Ha valamiért nem JSON a body, akkor is adjuk vissza stringként
                envelope.put("backendBody", body);
                envelope.put("backendBodyParseError", parseEx.getClass().getSimpleName());
            }

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(envelope);

        } catch (RestClientException e) {
            long t1 = System.nanoTime();
            long backendLatencyMs = (t1 - t0) / 1_000_000L;

            Map<String, Object> errorEnvelope = new LinkedHashMap<>();
            errorEnvelope.put("requestId", requestId);
            errorEnvelope.put("backendStatus", 502);
            errorEnvelope.put("workerHostName", hostName);
            errorEnvelope.put("workerIPAddress", ipAddress);
            errorEnvelope.put("backendLatencyMs", backendLatencyMs);
            errorEnvelope.put("springLatencyMs", backendLatencyMs);
            errorEnvelope.put("error", "FastAPI backend hiba");
            errorEnvelope.put("errorDetail", e.getMessage());

            return ResponseEntity
                    .status(502)
                    .body(errorEnvelope);
        }
    }
}
