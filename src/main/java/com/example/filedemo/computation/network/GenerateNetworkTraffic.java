package com.example.filedemo.computation.network;

import com.example.filedemo.responses.NetworkResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("GenerateNetworkTraffic")
public class GenerateNetworkTraffic {

    public NetworkResponse run(int number, boolean withGC) {

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        // Create a list
        ArrayList<String> list = new ArrayList<>();
        // Add new elements to the list
        for (int i = 0; i <= number; i++) {
            list.add(new String("My String Name " + i));
        }
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        if (withGC) {
            runtime.gc();
        }
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        NetworkResponse response = new NetworkResponse("NetworkResponse", number, memory, elapsedTime, list);
        System.out.println(response);
        return response;

    }
}
