package com.example.filedemo.computation.io;

import com.example.filedemo.responses.IoResponse;
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
import java.util.Properties;
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

    public IoResponse run(int times) {

        System.out.println(" Hello FileCopier run() ");

        uploadDirLocation = this.fileStorageService.getFileStorageLocation();
        System.out.println(this.uploadDirLocation);
        System.out.println("--------------------");

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();


        for (int i = 0; i < times; i++) {

            String a = generateRandomAlphanumericString();

            try {
                ClassPathResource cpr = new ClassPathResource("install/2048_2048.png");
                InputStream inputStream = cpr.getInputStream();

                // Get the name of the OS
                Properties properties = System.getProperties();
                String os = properties.getProperty("os.name");

                if (os.contains("Linux")) {
                    Path targetLocation = Paths.get(uploadDirLocation.toString() + "/CopiedFile " + a + ".png");
                    Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Path targetLocation = Paths.get(uploadDirLocation.toString() + "\\CopiedFile " + a + ".png");
                    Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                logger.info("File Not Found");
            }
        }

        final long end = System.currentTimeMillis();
        elapsedTime += (end - start);

        IoResponse response = new IoResponse("IoResponse > FileCopier", String.valueOf(times), 0L, elapsedTime);
        System.out.println(response);
        return response;

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
