package com.example.filedemo.responses;

public class WaitResponse {

    String name;
    Long waitTime;
    Long executionTime;

    public WaitResponse() {
    }

    public WaitResponse(String name, Long waitTime, Long executionTime) {
        this.name = name;
        this.waitTime = waitTime;
        this.executionTime = executionTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "WaitResponse{" +
                "name='" + name + '\'' +
                ", waitTime=" + waitTime +
                ", executionTime=" + executionTime +
                '}';
    }
}
