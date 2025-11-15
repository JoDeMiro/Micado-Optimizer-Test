package com.example.filedemo.responses;

public class ExpensesStats {

    private long count;
    private double avgAmount;

    public ExpensesStats() {
    }

    public ExpensesStats(long count, double avgAmount) {
        this.count = count;
        this.avgAmount = avgAmount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(double avgAmount) {
        this.avgAmount = avgAmount;
    }
}
