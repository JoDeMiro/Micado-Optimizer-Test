package com.example.filedemo.computation.memory;

import com.example.filedemo.responses.GenericResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Random;

@Component("StringSizeCalc")
public class StringSizeCalc {

    public GenericResponse<String, Integer, Integer, Long> run(int iteration) {

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < iteration; i++) {
            byte[] array = new byte[7]; // length is bounded by 7
            new Random().nextBytes(array);
            String randomString = new String(array, Charset.forName("UTF-8"));

            stringBuilder.append(randomString).append(" ");
        }

        int length = stringBuilder.length();
        System.out.println("Length = " + length);

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        GenericResponse<String, Integer, Integer, Long> response;
        response = new GenericResponse<String, Integer, Integer, Long> ("GenericResponse", iteration, length, elapsedTime);

        return response;
    }
}
