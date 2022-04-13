package com.example.filedemo.computation.download;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StaticDownloader {

    public String getStaticData(int targetStringLength) {

        String result = generateRandomAlphanumericString(targetStringLength);

        return result;
    }

    private String generateRandomAlphanumericString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
