package com.example.filedemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestControllerExtra {

    @GetMapping("/extra/simulate1/{cvalue}/{wvalue}/{rvalue}")
    public Map<String, Object> simulateLoad(@PathVariable String cvalue, @PathVariable String wvalue, @PathVariable String rvalue) {
        long start = System.currentTimeMillis();

        int clevel = Integer.parseInt(cvalue);
        int wlevel = Integer.parseInt(wvalue);
        int rlevel = Integer.parseInt(rvalue);

        // CPU-intensive: hash computation
        for (int i = 0; i < clevel * 5000; i++) {
            sha256("TestString" + i);
        }

        // I/O-intensive: writing and reading temporary file
        String fileName = "tmp_loadfile_" + System.nanoTime() + ".tmp";

        try {
            // WRITE
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < wlevel * 1000; i++) {
                writer.write("This is a test line to simulate disk I/O load.\n");
            }
            writer.close();

            // READ
            // BufferedReader reader = new BufferedReader(new FileReader(fileName));
            // while (reader.readLine() != null) {
                // Just reading line by line
            // }
            // reader.close();

            // FILE READ - valódi disk access RandomAccessFile-lal
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            byte[] buffer = new byte[4096];
            for (int i = 0; i < rlevel * 25; i++) {
                raf.seek((long) i * 4096);
                raf.read(buffer);
            }
            raf.close();

            // DELETE
            new File(fileName).delete();

        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        String ipAddress = "unknown";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {}

        // JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("ip", ipAddress);
        response.put("clevel", clevel);
        response.put("wlevel", wlevel);
        response.put("rlevel", rlevel);
        response.put("computationMillis", duration);
        response.put("status", "ok");

        return response;
    }

    private void sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(input.getBytes());
            digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

