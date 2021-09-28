package com.example.filedemo.responses;

public class WaitResponse {

    Long waitTime;
    String name;

    public WaitResponse() {
    }

    public WaitResponse(Long waitTime, String name) {
        this.waitTime = waitTime;
        this.name = name;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WaitResponse{" +
                "waitTime=" + waitTime +
                ", name='" + name + '\'' +
                '}';
    }
}
