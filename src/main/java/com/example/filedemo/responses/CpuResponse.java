package com.example.filedemo.responses;

public class CpuResponse {

    String name;
    String parameter;
    Long returnValue;
    Long executionTime;
    String workerIPAddress;

    public CpuResponse() {
    }

    public CpuResponse(String name, String parameter, Long returnValue, Long executionTime) {
        this.name = name;
        this.parameter = parameter;
        this.returnValue = returnValue;
        this.executionTime = executionTime;
    }

    public CpuResponse(String name, String parameter, Long returnValue, Long executionTime, String ip) {
        this.name = name;
        this.parameter = parameter;
        this.returnValue = returnValue;
        this.executionTime = executionTime;
        this.workerIPAddress = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Long getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Long returnValue) {
        this.returnValue = returnValue;
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
        return "CpuResponse{" +
                "name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", returnValue=" + returnValue +
                ", executionTime=" + executionTime +
                ", workerIPAddress='" + workerIPAddress + '\'' +
                '}';
    }
}
