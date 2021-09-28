package com.example.filedemo.responses;

public class CpuResponse {

    Long executionTime;
    String parameter;
    String name;

    public CpuResponse() {
    }

    public CpuResponse(Long executionTime, String parameter, String name) {
        this.executionTime = executionTime;
        this.parameter = parameter;
        this.name = name;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CpuResponse{" +
                "executionTime=" + executionTime +
                ", parameter='" + parameter + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
