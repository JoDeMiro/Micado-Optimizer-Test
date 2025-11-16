package com.example.filedemo.responses;

public class AdvancedExpenseStats {

    private long count;
    private Double avgAmount;
    private Double minAmount;
    private Double maxAmount;
    private Double sumAmount;
    private Double stdDevAmount;
    private Double avgNameLength;

    public AdvancedExpenseStats() {
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

    public Double getStdDevAmount() {
        return stdDevAmount;
    }

    public void setStdDevAmount(Double stdDevAmount) {
        this.stdDevAmount = stdDevAmount;
    }

    public Double getAvgNameLength() {
        return avgNameLength;
    }

    public void setAvgNameLength(Double avgNameLength) {
        this.avgNameLength = avgNameLength;
    }
}
