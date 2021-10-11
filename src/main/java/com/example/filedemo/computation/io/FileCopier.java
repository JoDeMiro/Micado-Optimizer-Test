package com.example.filedemo.computation.io;

import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Component("FileCopier")
public class FileCopier {

    private static final Logger logger = LoggerFactory.getLogger(FileCopier.class);

    @Autowired
    private FileStorageService fileStorageService;

    Path uploadDirLocation;

    public FileCopier(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public void run(int times) {

        System.out.println(" Hello FileCopier run() ");

        uploadDirLocation = this.fileStorageService.getFileStorageLocation();
        System.out.println("--------------------");
        System.out.println(this.uploadDirLocation);
        System.out.println("--------------------");


        for (int i = 0; i < times; i++) {

            String a = generateRandomAlphanumericString();

            try {
                ClassPathResource cpr = new ClassPathResource("install/2048_2048.png");
                InputStream inputStream = cpr.getInputStream();
                Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\CopiedFile " + a + ".png");
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.info("File Not Found");
            }
        }

    }

    public String generateRandomAlphanumericString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}
