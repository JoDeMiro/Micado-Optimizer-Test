package com.example.filedemo.queues;

public class Job implements Runnable {

    private Integer count;

    public Job(Integer count) {
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start = " + count);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end = " + count);
    }

    @Override
    public String toString() {
        return "Job{" +
                "count=" + count +
                '}';
    }
}
