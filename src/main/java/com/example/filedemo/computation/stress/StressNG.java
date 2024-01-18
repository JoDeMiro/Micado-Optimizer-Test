package com.example.filedemo.computation.stress;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class StressNG {

    public void test1(String type, String second) throws ExecutionException, InterruptedException {
        // Itt add meg a kívánt "stress-ng" parancsot
        // String command = "stress-ng --matrix 1 -t 1s";
        // https://smackerelofopinion.blogspot.com/2017/05/simple-job-scripting-in-stress-ng-00800.html
        String command = "";

        switch(type) {
            case "1":
                command = "stress-ng --matrix 1 -t " + second +"s";
                break;
            case "2":
                command = "stress-ng -c 1 -l 80 -t " + second +"s";
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
            e.printStackTrace();
        }
    }
}
