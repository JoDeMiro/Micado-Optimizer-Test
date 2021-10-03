package com.example.filedemo.computation.network;

import com.example.filedemo.responses.NetworkResponse;
import org.springframework.stereotype.Component;

@Component("GetNetworkTraffic")
public class GetNetworkTraffic {

    public NetworkResponse run(CreateNetworkData createNetworkData, int number, boolean withGC) {

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

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

        NetworkResponse response = new NetworkResponse("NetworkResponse", number, memory, elapsedTime, createNetworkData.getList());
        // System.out.println(response);
        return response;
    }
}
