package com.example.filedemo.queues;

import java.util.concurrent.*;

public class ThreadPoolDemo {

    public static void main(String[] args) {

        int corePoolSize = 2;
        int maximumPoolSize = 5;
        int keepAliveTime = 1000;
        int queueSize = 3;

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize), threadFactory, new RejectionHandler());

        for (int i = 0; i < 10; i++) {
            poolExecutor.execute(new Job(i));
        }

        poolExecutor.shutdown();
    }
}
