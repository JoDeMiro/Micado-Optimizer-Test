package com.example.filedemo.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfoBean {

    @Value("${name}")
    private String name;
    @Value("${server.tomcat.max-threads}")
    private String serverTomcatMaxThreads;
    @Value("${file.upload-dir}")
    private String fileUploadDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerTomcatMaxThreads() {
        return serverTomcatMaxThreads;
    }

    public void setServerTomcatMaxThreads(String serverTomcatMaxThreads) {
        this.serverTomcatMaxThreads = serverTomcatMaxThreads;
    }

    public String getFileUploadDir() {
        return fileUploadDir;
    }

    public void setFileUploadDir(String fileUploadDir) {
        this.fileUploadDir = fileUploadDir;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "name='" + name + '\'' +
                ", serverTomcatMaxThreads='" + serverTomcatMaxThreads + '\'' +
                ", fileUploadDir='" + fileUploadDir + '\'' +
                '}';
    }
}
