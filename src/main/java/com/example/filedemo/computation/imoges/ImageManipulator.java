package com.example.filedemo.computation.imoges;

import com.example.filedemo.computation.io.FileCopier;
import com.example.filedemo.responses.IoResponse;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Random;

@Component("ImageManipulator")
public class ImageManipulator {

    private static final Logger logger = LoggerFactory.getLogger(FileCopier.class);

    @Autowired
    private FileStorageService fileStorageService;

    Path uploadDirLocation;

    public ImageManipulator(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public IoResponse run(int times, String stringSize) {

        System.out.println(" Hello ImageManipulator run() ");

        uploadDirLocation = this.fileStorageService.getFileStorageLocation();
        System.out.println(this.uploadDirLocation);
        System.out.println("--------------------");

        long elapsedTime = 0;
        final long start = System.currentTimeMillis();

        for (int i = 0; i < times; i++) {

            String a = generateRandomAlphanumericString();

            try {

                // Get the name of the OS
                Properties properties = System.getProperties();
                String os = properties.getProperty("os.name");

                System.out.println(os);

                if (os.contains("Linux")) {

                    // file object
                    File f = null;

                    BufferedImage img = null;

                    System.out.println("------------ F ---------------");

                    try {
                        String imagePath = "/home/ubuntu/uploads/" + stringSize + ".png";
                        System.out.println(imagePath);
                        // img = ImageIO.read(new File("/home/ubuntu/uploads/512_512.png"));
                        img = ImageIO.read(new File(imagePath));
                        System.out.println("------------ G ---------------");
                        for (int y = 0; y < img.getHeight(); y++) {
                            for (int x = 0; x < img.getWidth(); x++) {
                                int l = (int)(Math.random()*256);
                                int r = (int)(Math.random()*256);
                                int g = (int)(Math.random()*256);
                                int b = (int)(Math.random()*256);
                                int p = (l<<24) | (r<<16) | (g<<8) | b;

                                img.setRGB(x, y, p);
                            }
                        }
                    } catch (IOException e) {
                    }

                    // write image
                    try
                    {
                        f = new File("/home/ubuntu/logo.png");
                        ImageIO.write(img, "png", f);
                    }
                    catch(IOException e)
                    {
                        System.out.println("Error: " + e);
                    }
                }
            } catch (Exception e) {
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
