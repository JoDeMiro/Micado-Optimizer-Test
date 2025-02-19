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

    // nem töröl
    public int test1() throws ExecutionException, InterruptedException {

        String outputFileName = "output_" + UUID.randomUUID() + ".mp3";
        String command = String.format("ffmpeg -y -i uploads/sound_4mp.wav -b:a 192K -vn %s", outputFileName);

        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();

            // Várj a parancs befejezésére
            int exitCode = process.waitFor();
            System.out.println("A parancs test1 befejezve kód: " + exitCode);

            // Ha a folyamat sikeresen lefutott, töröljük a fájlt
            if (exitCode == 0) {
                File outputFile = new File(outputFileName);
                if (outputFile.exists()) {
                    boolean deleted = outputFile.delete();
                    if (!deleted) {
                        logger.error("test1 - Nem sikerült törölni a fájlt: " + outputFileName);
                    }
                }
            }

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/1", e);
        }
        return -900;
    }

    // nem töröl
    // ez jó megoldás
    public int test3() throws ExecutionException, InterruptedException {
        // Egyedi fájlnév generálása UUID segítségével

        int cpuLimit = 10;

        String outputFileName = "output_" + UUID.randomUUID() + ".mp3";
        String command = String.format("cpulimit -l %d -- ffmpeg -y -i uploads/sound_4mp.wav -b:a 192K -vn %s", cpuLimit, outputFileName);

        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
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
            System.out.println("A parancs test3 befejezve kód: " + exitCode);

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
        }
        return -900;
    }

    // töröl
    // ez jó megoldás
    public int test3(int cpuLimit) throws ExecutionException, InterruptedException {
        // Egyedi fájlnév generálása UUID segítségével
        String outputFileName = "output_" + UUID.randomUUID() + ".mp3";
        String command = String.format("cpulimit -l %d -- ffmpeg -y -i uploads/sound_4mp.wav -b:a 192K -vn %s", cpuLimit, outputFileName);

        try {
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
            System.out.println("A parancs test3 befejezve kód: " + exitCode);

            // Ha a folyamat sikeresen lefutott, töröljük a fájlt
            if (exitCode == 0) {
                File outputFile = new File(outputFileName);
                if (outputFile.exists()) {
                    boolean deleted = outputFile.delete();
                    if (!deleted) {
                        logger.error("test3 - Nem sikerült törölni a fájlt: " + outputFileName);
                    }
                }
            }

            return exitCode;

        } catch (IOException | InterruptedException e) {
            logger.error("/ffmpeg/test/3", e);
            return -900;
        }
    }

    // töröl
    // rossz
    // ha nem kapjuk el a kimenetet akkor korábban megszakad a program és az ffmpeg nem fut le
    // ezt kijavítottam a test3() metodusban ami ugyan ezt csinálja de le is fut rendesen
    public int test4() throws ExecutionException, InterruptedException {
        // Egyedi fájlnév generálása UUID segítségével

        int cpuLimit = 10;

        String outputFileName = "output_" + UUID.randomUUID() + ".mp3";
        String command = String.format("cpulimit -l %d -- ffmpeg -y -i uploads/sound_4mp.wav -b:a 192K -vn %s", cpuLimit, outputFileName);

        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();

            int exitCode = process.waitFor();
            System.out.println("A parancs test4 befejezve kód: " + exitCode);

            // Ha a folyamat sikeresen lefutott, töröljük a fájlt
            if (exitCode == 0) {
                File outputFile = new File(outputFileName);
                if (outputFile.exists()) {
                    boolean deleted = outputFile.delete();
                    if (!deleted) {
                        logger.error("test4 - Nem sikerült törölni a fájlt: " + outputFileName);
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
        String textToWrite = "file 'uploads/sound_4mp.wav'\n";
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
