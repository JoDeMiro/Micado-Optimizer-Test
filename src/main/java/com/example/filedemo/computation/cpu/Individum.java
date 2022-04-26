package com.example.filedemo.computation.cpu;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Individum {

    public static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0) return n;
        else
            return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }

    public static Long run(int number) throws ExecutionException, InterruptedException {

        Long result = 0L;
        Long sum = 0L;
        for (int i = 0; i < number; i++) {
            BigInteger fib = fib(new BigInteger(String.valueOf(i)));
            result = fib.longValue();
            sum += result;
        }

        return result;
    }
}