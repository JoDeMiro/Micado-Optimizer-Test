package com.example.filedemo.computation.cpu;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Fibonnaci {

    public static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0 ) return n;
        else
            return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }

    public static Integer run(int number) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Integer sum = 0;

        for (int j = 0; j < 10; j++) {
            final int ID = j;

            Future<Integer> submit = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Integer result = 0;
                    for (int i = 0; i < number; i++) {
                        BigInteger fib = fib(new BigInteger(String.valueOf(i)));
                        System.out.println(ID + " worker: " + i + ": " + fib);
                        result = fib.intValue();
                    }
                    return result;
                }
            });

            Integer result = submit.get();
            sum += result;
        }
        System.out.println("Sum = " + sum);
        return sum;
    }
}
