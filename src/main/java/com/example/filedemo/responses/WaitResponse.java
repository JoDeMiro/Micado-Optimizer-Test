package com.example.filedemo.responses;

import org.springframework.stereotype.Component;

@Component
public class WaitResponse {

    String name;
    Long waitTime;
    Long executionTime;
    String workerIPAddress;


    public WaitResponse() {
    }

    public WaitResponse(String name, Long waitTime, Long executionTime) {
        this.name = name;
        this.waitTime = waitTime;
        this.executionTime = executionTime;
    }

    public WaitResponse(String name, Long waitTime, Long executionTime, String workerIPAddress) {
        this.name = name;
        this.waitTime = waitTime;
        this.executionTime = executionTime;
        this.workerIPAddress = workerIPAddress;
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

    public String getWorkerIPAddress() {
        return workerIPAddress;
    }

    public void setWorkerIPAddress(String workerIPAddress) {
        this.workerIPAddress = workerIPAddress;
    }

    @Override
    public String toString() {
        return "WaitResponse{" +
                "name='" + name + '\'' +
                ", waitTime=" + waitTime +
                ", executionTime=" + executionTime +
                ", workerIPAddress='" + workerIPAddress + '\'' +
                '}';
    }
}
