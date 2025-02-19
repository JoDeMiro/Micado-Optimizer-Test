package com.example.filedemo.computation.zip;

import com.example.filedemo.controller.RestsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
// @Scope("prototype")
public class Zip {

    private static final Logger logger = LoggerFactory.getLogger(Zip.class);

    public void test1(String type) throws ExecutionException, InterruptedException {
        // Itt add meg a kívánt "zip" parancsot
        // String command = "zip -q -9 tmp.zip uploads/sound.wav";
        //
        String command = "";

        switch(type) {
            case "zip1":
                command = "zip -q -9 uploads/tmp.zip uploads/sound.wav";
                break;
            case "zip2":
                command = "zip -q -9 uploads/tmp.zip uploads/4096_4096.png";
                break;
            case "zip3":
                command = "zip -q -9 uploads/tmp.zip uploads/4096_4096.png > /dev/null 2>&1";
                break;
            default:
                // code block
        }

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

        } catch (IOException | InterruptedException e) {
            logger.error("Zip class", e);
        }
    }
}