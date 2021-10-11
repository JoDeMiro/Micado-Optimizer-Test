package com.example.filedemo.queues;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class Factory {

    @Bean
    public void run() {

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
