package com.example.filedemo.computation.memory;

import com.example.filedemo.beans.MyBean;
import com.example.filedemo.responses.GenericResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("TestBean")
public class MyBeanCalc {
    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public GenericResponse memoryTest(int number, boolean withGC) {

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        // Create a list
        List<MyBean> list = new ArrayList<>();
        // Add new elements to the list
        for (int i = 0; i <= number; i++) {
            list.add(new MyBean("My Bean Name " + i));
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
        System.out.println("Used memory is megabytes: " + bytesToMegabytes(memory));

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        GenericResponse response = new GenericResponse("GenericResponse", number, memory, elapsedTime);
        System.out.println(response);
        return response;

    }
}
