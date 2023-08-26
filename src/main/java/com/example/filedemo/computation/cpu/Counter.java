package com.example.filedemo.computation.cpu;

import org.springframework.stereotype.Component;

public class Counter implements Runnable {
    private final long countUntil;

    Counter(long countUntil) {
        this.countUntil = countUntil;
    }

    @Override
    public void run() {
        long sum = 0;
        for (long i = 1; i < countUntil; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
