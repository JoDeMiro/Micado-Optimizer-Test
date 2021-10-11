package com.example.filedemo;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
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

    @Bean
    public void setupDownloadFile() {
        System.out.println("Hello Initializer");

        // A letöltés file helye
        System.out.println();

        // From C:\\
        /*
        try {
            Path from = Paths.get("C:\\1024_768.png");
            Path targetLocation = Paths.get("C:\\uploads\\1024_768.png");
            Files.copy(from, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("Initializer setup files from C:\\ failed.");
            throw new FileStorageException("Could not set file", e);
        }
        */


        // version 5
        try {
            System.out.println("---------version 5---------------");

            ClassPathResource cpr = new ClassPathResource("install/4096_4096.png");

            InputStream inputStream = cpr.getInputStream();

            Path targetLocation = Paths.get("C:\\uploads\\5_4096_4096.png");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 6
        try {
            System.out.println("---------version 6---------------");

            ClassPathResource cpr = new ClassPathResource("install/2048_2048.png");

            InputStream inputStream = cpr.getInputStream();

            Path targetLocation = Paths.get("C:\\uploads\\6_2048_2048.png");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

        // version 7
        try {
            System.out.println("---------version 7---------------");

            InputStream inputStream = getClass().getResourceAsStream("/install/1024_1024.png");

            Path targetLocation = Paths.get("C:\\uploads\\7_1024_1024.png");
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info("File Not Found");
        }

    }
}
