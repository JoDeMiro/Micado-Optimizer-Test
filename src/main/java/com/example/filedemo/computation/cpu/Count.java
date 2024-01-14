package com.example.filedemo.computation.cpu;

import com.example.filedemo.responses.CpuResponse;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class Count {

    public static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0 ) return n;
        else
            return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }

    public static Long run(int number, int thread) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(thread);

        Long sum = 0L;

        long start = System.currentTimeMillis();

        List<Future<Long>> futures = new ArrayList<>();

        for (int j = 0; j < thread; j++) {
            final int ID = j;

            Future<Long> submit = executorService.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    Long result = 0L;
                    for (int i = 0; i < number; i++) {
                        BigInteger fib = fib(new BigInteger(String.valueOf(i)));
                        // System.out.println(ID + " worker: " + i + ": " + fib);
                        result = result + fib.longValue();
                    }
                    // System.out.println(ID+" worker: " + number + ": " + result);
                    return result;
                }
            });

            futures.add(submit);
        }

        for (Future<Long> future : futures) {
            Long result = future.get();
            sum += result;
        }

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        String parameter = String.valueOf(number);
        CpuResponse response = new CpuResponse("CpuResponse", parameter, sum, elapsedTime);

        // System.out.println("Sum = " + sum);
        executorService.shutdown();
        executorService.shutdownNow();
        return sum;
    }
}