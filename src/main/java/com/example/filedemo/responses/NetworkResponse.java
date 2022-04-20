package com.example.filedemo.responses;

import java.util.ArrayList;

public class NetworkResponse<String, P, R, Long> {

    String name;
    P parameter;
    R returnValue;
    Long executionTime;
    ArrayList<String> message;
    String workerIPAddress;

    public NetworkResponse() {
    }

    public NetworkResponse(String name, P parameter, R returnValue, Long executionTime, ArrayList<String> message) {
        this.name = name;
        this.parameter = parameter;
        this.returnValue = returnValue;
        this.executionTime = executionTime;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public P getParameter() {
        return parameter;
    }

    public void setParameter(P parameter) {
        this.parameter = parameter;
    }

    public R getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(R returnValue) {
        this.returnValue = returnValue;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public String getWorkerIPAddress() {
        return workerIPAddress;
    }

    public void setWorkerIPAddress(String workerIPAddress) {
        this.workerIPAddress = workerIPAddress;
    }

    @Override
    public java.lang.String toString() {
        return "NetworkResponse{" +
                "name=" + name +
                ", parameter=" + parameter +
                ", returnValue=" + returnValue +
                ", executionTime=" + executionTime +
                ", message=" + message +
                ", workerIPAddress=" + workerIPAddress +
                '}';
    }
}
