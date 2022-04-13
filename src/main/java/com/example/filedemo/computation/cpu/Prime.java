package com.example.filedemo.computation.cpu;

import com.example.filedemo.responses.CpuResponse;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
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
                    return result;
                }
            });

            Long result = submit.get();
            sum += result;
        }
        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;
        System.out.println("Sum = " + sum);
        String parameter = String.valueOf(max_number);
        CpuResponse response = new CpuResponse("CpuResponse", parameter, sum, elapsedTime);
        System.out.println(response);
        executorService.shutdown();
        executorService.shutdownNow();
        return response;
    }
}
