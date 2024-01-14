package com.example.filedemo.computation.cpu;

import com.example.filedemo.responses.CpuResponse;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class Prime {

    public static boolean isPrime(BigInteger n) {
        BigInteger counter = BigInteger.ONE.add(BigInteger.ONE);
        boolean isPrime = true;
        while (counter.compareTo(n) == -1) {
            if (n.remainder(counter).compareTo(BigInteger.ZERO) == 0) {
                isPrime = false;
                break;
            }
            counter = counter.add(BigInteger.ONE);
        }
        return isPrime;
    }

    public static CpuResponse run(int max_number) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long sum = 0L;

        long start = System.currentTimeMillis();

        List<Future<Long>> futures = new ArrayList<>();

        for (int j = 0; j < 10; j++) {
            final int ID = j;

            Future<Long> submit = executorService.submit(new Callable<Long>() {
                @Override
                public synchronized Long call() throws Exception {
                    Long result = 0L;
                    BigInteger number = BigInteger.ONE;
                    for (int i = 0; i < max_number; i++) {
                        boolean prime = isPrime(number);
                        // System.out.println(ID+" worker: " + number + ": " + prime);
                        number = number.add(BigInteger.ONE);
                        if (prime) {
                            result += number.longValue() - 1;
                        }
                    }
                    System.out.println(ID+" worker: " + number + ": " + result);
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

        String parameter = String.valueOf(max_number);
        CpuResponse response = new CpuResponse("CpuResponse", parameter, sum, elapsedTime);

        executorService.shutdown();
        executorService.shutdownNow();
        return response;
    }
}
