package com.example.filedemo.computation.ffmpeg;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

@Component
public class FFMpeg {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public int test1() throws ExecutionException, InterruptedException {
        // Itt add meg a kívánt "stress-ng" parancsot
        // String command = "ffmpeg -y -i sound.wav -b:a 192K -vn kimeneti.mp3";
        String command = "ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3";

        try {
            Process process = Runtime.getRuntime().exec(command);

            // Opcionális: Kezeld a parancs kimenetét
            // InputStream inputStream = process.getInputStream();
            // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // String line;
            // while ((line = reader.readLine()) != null) {
            //     System.out.println(line);
            // }

            // Várj a parancs befejezésére
            int exitCode = process.waitFor();
            // System.out.println("A parancs befejezve kód: " + exitCode);

            return exitCode;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -900;
    }

    public void createFile(String n) {
        String textToWrite = "file 'uploads/sound.wav'\n";
        String fileName = "mytext" + n + ".txt";
        int numberOfTimes = Integer.parseInt(n);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < numberOfTimes; i++) {
                writer.write(textToWrite);
            }
            writer.close();
            System.out.println("Text written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public int test2(String n) throws ExecutionException, InterruptedException {
        // Itt add meg a kívánt "stress-ng" parancsot
        String randomString = generateRandomString(9);
        String filename = "output" + randomString + ".wav";
        String command = "ffmpeg -y -f concat -i mytext" + n + ".txt -c copy " + filename;
        String clear = "rm " + filename;

        // Create file amiből kiolvas az FFMpeg
        createFile(n);

        try {
            Process process = Runtime.getRuntime().exec(command);
            // Várj a parancs befejezésére
            int exitCode = process.waitFor();
            // System.out.println("A parancs befejezve kód: " + exitCode);

            // return exitCode;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Process process = Runtime.getRuntime().exec(clear);
            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return -900;
    }
}
