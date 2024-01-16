package com.example.filedemo;

import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Component
public class Initializer {

    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private FileStorageService fileStorageService;

    public static void deleteDirectoryLegacyIO(File file) {

        File[] list = file.listFiles();
        if (list != null) {
            for (File temp : list) {
                //recursive delete
                System.out.println("Visit " + temp);
                deleteDirectoryLegacyIO(temp);
            }
        }

        if (file.delete()) {
            System.out.printf("Delete : %s%n", file);
        } else {
            System.err.printf("Unable to delete file or directory : %s%n", file);
        }

    }

    @Bean
    @Order(1)
    public void clearDirectory() {

        // Unix / Window Problem could be handled here

        Path uploadDirLocation = this.fileStorageService.getFileStorageLocation();
        System.out.println("fileStorageService.getFileStorageLocation = " + uploadDirLocation);

        // File file = new File("C:\\uploads\\");
        File file = new File(String.valueOf(uploadDirLocation));
        deleteDirectoryLegacyIO(file);

        try {
            System.out.println(" -------------------------------------------------------- ");
            System.out.println("           DELETE DIRECTORY THREAD SLEEP                  ");
            System.out.println(" -------------------------------------------------------- ");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Bean
    @Order(2)
    public void setupDownloadFile() {
        System.out.println("Hello Initializer");

        // Unix / Window Problem could be handled here

        Path uploadDirLocation = this.fileStorageService.getFileStorageLocation();
        System.out.println("fileStorageService.getFileStorageLocation = " + uploadDirLocation);

        // -------------------------------------------------------------------

        // Create Directory
        try {
            // Path path = Paths.get("C:\\uploads");
            Path path = Paths.get(uploadDirLocation.toString());
            //java.nio.file.Files;
            Files.createDirectories(path);
            System.out.println("Directory is created!");
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }

        try {
            System.out.println(" -------------------------------------------------------- ");
            System.out.println("           CREATE DIRECTORY THREAD SLEEP                  ");
            System.out.println(" -------------------------------------------------------- ");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // version 5
        try {
            System.out.println("---------version 5---------------");

            ClassPathResource cpr = new ClassPathResource("install/4096_4096.png");

            InputStream inputStream = cpr.getInputStream();

            ClassPathResource cprLinux = new ClassPathResource("install/4096_4096.png");

            InputStream inputStreamLinux = cpr.getInputStream();

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\4096_4096.png");

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/4096_4096.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);


        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 6
        try {
            System.out.println("---------version 6---------------");

            ClassPathResource cpr = new ClassPathResource("install/2048_2048.png");

            InputStream inputStream = cpr.getInputStream();

            ClassPathResource cprLinux = new ClassPathResource("install/2048_2048.png");

            InputStream inputStreamLinux = cprLinux.getInputStream();

            // Path targetLocation = Paths.get("C:\\uploads\\6_2048_2048.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\2048_2048.png");

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/2048_2048.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 7
        try {
            System.out.println("---------version 7---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/1024_1024.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/1024_1024.png");

            // Path targetLocation = Paths.get("C:\\uploads\\7_1024_1024.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\1024_1024.png");

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/1024_1024.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 8
        try {
            System.out.println("---------version 8---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/1024_768.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/1024_768.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\1024_768.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/1024_768.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 6
        try {
            System.out.println("---------version 6---------------");

            ClassPathResource cpr = new ClassPathResource("install/2048_2048.png");

            InputStream inputStream = cpr.getInputStream();

            ClassPathResource cprLinux = new ClassPathResource("install/2048_2048.png");

            InputStream inputStreamLinux = cprLinux.getInputStream();

            // Path targetLocation = Paths.get("C:\\uploads\\6_2048_2048.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\2048_2048.png");

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/2048_2048.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 7
        try {
            System.out.println("---------version 7---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/1024_1024.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/1024_1024.png");

            // Path targetLocation = Paths.get("C:\\uploads\\7_1024_1024.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\1024_1024.png");

            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/1024_1024.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 8
        try {
            System.out.println("---------version 8---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/1024_768.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/1024_768.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\1024_768.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/1024_768.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 9
        try {
            System.out.println("---------version 9---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/512_512.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/512_512.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\512_512.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/512_512.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 10
        try {
            System.out.println("---------version 10--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/256_256.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/256_256.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\256_256.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/256_256.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 11
        try {
            System.out.println("---------version 11--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/128_128.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/128_128.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\128_128.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/128_128.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 12
        try {
            System.out.println("---------version 12--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/64_64.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/64_64.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\64_64.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/64_64.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 13
        try {
            System.out.println("---------version 13--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/32_32.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/32_32.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\32_32.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/32_32.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 14
        try {
            System.out.println("---------version 14--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/16_16.png");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/16_16.png");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\16_16.png");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/16_16.png");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 15
        try {
            System.out.println("---------version 15--------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/sound.wav");
            InputStream inputStreamLinux = getClass().getResourceAsStream("/install/sound.wav");

            // Ez a Windows path
            Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\sound.wav");

            System.out.println("-------------------------------------------------------------------------");
            System.out.println(targetLocation);
            System.out.println("-------------------------------------------------------------------------");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Ez a Linux path
            Path targetLocLinux = Paths.get(uploadDirLocation.toString() + "/sound.wav");

            Files.copy(inputStreamLinux, targetLocLinux, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }


        // -------------------------------------------------------------------

        // Create Directory
        /*
        try {
            Path path = Paths.get("C:\\uploads");
            //java.nio.file.Files;
            Files.createDirectories(path);
            System.out.println("Directory is created!");
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }


        try {
            System.out.println(" -------------------------------------------------------- ");
            System.out.println("           CREATE DIRECTORY THREAD SLEEP                  ");
            System.out.println(" -------------------------------------------------------- ");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        */

    }
}
