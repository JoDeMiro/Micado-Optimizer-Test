package com.example.filedemo.responses;

public class GenericResponse<String, P, R, Long> {

    String name;
    P parameter;
    R returnValue;
    Long executionTime;

    public GenericResponse() {
    }

    public GenericResponse(String name, P parameter, R returnValue, Long executionTime) {
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

    @Override
    public java.lang.String toString() {
        return "GenericResponse{" +
                "name=" + name +
                ", parameter=" + parameter +
                ", returnValue=" + returnValue +
                ", executionTime=" + executionTime +
                '}';
    }
}
