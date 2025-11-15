package com.example.filedemo.responses;

public class ExtendedExpenseStats {

    private long count;
    private Double avgAmount;
    private Double minAmount;
    private Double maxAmount;
    private Double sumAmount;

    public ExtendedExpenseStats() {
    }

    public ExtendedExpenseStats(long count, Double avgAmount, Double minAmount, Double maxAmount, Double sumAmount) {
        this.count = count;
        this.avgAmount = avgAmount;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.sumAmount = sumAmount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(Double avgAmount) {
        this.avgAmount = avgAmount;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Double sumAmount) {
        this.sumAmount = sumAmount;
    }
}

