package com.example.filedemo.computation.ffmpeg;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class FFMpeg {

    public int test1() throws ExecutionException, InterruptedException {
        // Itt add meg a kívánt "stress-ng" parancsot
        // String command = "stress-ng --matrix 1 -t 1s";
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
            System.out.println("A parancs befejezve kód: " + exitCode);

            return exitCode;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return -900;
    }
}
