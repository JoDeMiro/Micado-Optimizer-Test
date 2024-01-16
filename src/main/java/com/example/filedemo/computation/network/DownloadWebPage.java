package com.example.filedemo.computation.network;

import com.example.filedemo.responses.NetworkResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Component("DownloadWebPage")
public class DownloadWebPage {

    public NetworkResponse run(int number, boolean withGC) throws IOException {

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        int byteRead = 0;

        URL website = new URL("https://images8.alphacoders.com/468/468739.jpg");

        try (InputStream in = website.openStream()) {
            // Path target = Paths.get("/tmp/delete.jpg");
            // Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // Create a list
        ArrayList<String> list = new ArrayList<>();
        // Add new elements to the list

        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        if (withGC) {
            runtime.gc();
        }
        // Print out byte read
        System.out.println("Bytes read: " + byteRead);

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        NetworkResponse response = new NetworkResponse("NetworkResponse", number, byteRead, elapsedTime, list);
        System.out.println(response);
        return response;

    }
}
