package com.example.filedemo.computation.cpu;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Multitask {

    // https://www.vogella.com/tutorials/JavaConcurrency/article.html

    public static void summarizer(int thread) {
        Summarizer summarizer1 = new Summarizer();
        Summarizer summarizer2 = new Summarizer();

        summarizer1.start();
        summarizer2.start();

        for (int i = 0; i < thread; i++) {
            Summarizer summarizer = new Summarizer();
            summarizer.start();
        }
    }

    public static void counter(int thread) {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < thread; i++) {
            Runnable task = new Counter(10000000L + i);
            Thread worker = new Thread(task);
            // We can set the name of the thread
            worker.setName(String.valueOf(i));
            // Start the thread, never call method run() direct
            worker.start();
            // Remember the thread for later usage
            threads.add(worker);
        }
        int running = 0;
        do {
            running = 0;
            for (Thread thread1 : threads) {
                if (thread1.isAlive()) {
                    running++;
                }
            }
            // System.out.println("We have " + running + " running threads. ");
        } while (running > 0);
    }
}
