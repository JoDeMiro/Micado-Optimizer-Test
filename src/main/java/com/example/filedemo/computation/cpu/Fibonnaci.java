package com.example.filedemo.computation.cpu;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.*;

@Component
public class Fibonnaci {

    public static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0 ) return n;
        else
            return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }

    public static Long run(int number) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long sum = 0L;

        for (int j = 0; j < 10; j++) {
            final int ID = j;

            Future<Long> submit = executorService.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    Long result = 0L;
                    for (int i = 0; i < number; i++) {
                        BigInteger fib = fib(new BigInteger(String.valueOf(i)));
                        // System.out.println(ID + " worker: " + i + ": " + fib);
                        result = fib.longValue();
                    }
                    return result;
                }
            });

            Long result = submit.get();
            sum += result;
        }
        System.out.println("Sum = " + sum);
        executorService.shutdown();
        executorService.shutdownNow();
        return sum;
    }
}
