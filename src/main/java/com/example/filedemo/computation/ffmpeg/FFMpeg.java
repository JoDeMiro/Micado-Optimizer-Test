package com.example.filedemo.computation.ffmpeg;

import com.example.filedemo.controller.RestsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.security.SecureRandom;

@Component
public class FFMpeg {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final Logger logger = LoggerFactory.getLogger(FFMpeg.class);

    public int test1() throws ExecutionException, InterruptedException {

        String command = "ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3";

        try {
            Process process = Runtime.getRuntime().exec(command);

            // Várj a parancs befejezésére
            int exitCode = process.waitFor();
            System.out.println("A parancs befejezve kód: " + exitCode);

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/1", e);
        }
        return -900;
    }

    public int test3a() throws ExecutionException, InterruptedException {

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    // "bash", "-c", "cpulimit -l 10 -- ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3"
                    "cpulimit -l 10 -- ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3"
            );
            Process process = builder.start();

            // Várj a parancs befejezésére
            int exitCode = process.waitFor();
            System.out.println("A parancs befejezve kód: " + exitCode);

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
        }
        return -900;
    }

    public int test3() throws ExecutionException, InterruptedException {

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "bash", "-c", "cpulimit -l 10 -- ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3"
            );
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("OUTPUT: " + line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }

            int exitCode = process.waitFor();

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
        }
        return -900;
    }

    public int test3(int cpuLimit) throws ExecutionException, InterruptedException {
        try {
            // Dinamikusan létrehozott parancs a CPU limit alapján
            String command = String.format("cpulimit -l %d -- ffmpeg -y -i uploads/sound.wav -b:a 192K -vn kimeneti.mp3", cpuLimit);

            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();

            // Folyamat kimenetének olvasása
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("OUTPUT: " + line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }

            int exitCode = process.waitFor();
            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
            return -900;
        }
    }
    // Uj methodusok tesztelem öket elsősorban a cpu kihasználtság beállítását akarom kitesztelni vele.

    public int test4() throws ExecutionException, InterruptedException {
        // Egyedi fájlnév generálása UUID segítségével
        String outputFileName = "output_" + UUID.randomUUID() + ".mp3";
        String command = "cpulimit -l 10 -- ffmpeg -y -i uploads/sound.wav -b:a 192K -vn " + outputFileName;

        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            System.out.println("A parancs befejezve kód: " + exitCode);

            // Ha a folyamat sikeresen lefutott, töröljük a fájlt
            if (exitCode == 0) {
                File outputFile = new File(outputFileName);
                if (outputFile.exists()) {
                    boolean deleted = outputFile.delete();
                    if (!deleted) {
                        // System.err.println("Nem sikerült törölni a fájlt: " + outputFileName);
                        logger.error("Nem sikerült törölni a fájlt: " + outputFileName);
                    }
                }
            }

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/4", e);
        }
        return -900;
    }

    // ToDo: Már ott probléma van, hogy mindíg ugyan az a mytext fájlt akarom írni
    // egyedi azonosítot kéne, hogy kapjon minden folyamat
    // már van egy generateRanodmString függvényem amit az output nevének előállításához használok
    // ugyan ezt lehetne felhasználni arra is, hogy a mytext + ... filenevet előállítsam.
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
            logger.error("/ffmpeg/test --> createFile()", e);
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
            logger.error("/ffmpeg/test/2", e);
        }

        try {
            Process process = Runtime.getRuntime().exec(clear);
            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/2", e);
        }

        return -900;
    }
}
