package com.example.filedemo.computation.cpu;

public class Summarizer extends Thread{

    @Override
    public void run() {
        Double number = 1D;
        for (int i = 0; i < 100; i++) {
            number = ((number + 1) * 2)/1.9;
        }
        // System.out.println(number);
    }
}
