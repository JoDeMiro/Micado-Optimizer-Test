package com.example.filedemo.responses;

public class IoResponse {

    String name;
    String parameter;
    Long returnValue;
    Long executionTime;

    public IoResponse() {
    }

    public IoResponse(String name, String parameter, Long returnValue, Long executionTime) {
        this.name = name;
        this.parameter = parameter;
        this.returnValue = returnValue;
        this.executionTime = executionTime;
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

    @Override
    public String toString() {
        return "IoResponse{" +
                "name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", returnValue=" + returnValue +
                ", executionTime=" + executionTime +
                '}';
    }
}
