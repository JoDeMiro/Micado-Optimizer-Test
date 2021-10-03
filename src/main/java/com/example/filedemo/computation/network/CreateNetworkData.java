package com.example.filedemo.computation.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component("CreateNetworkData")
public class CreateNetworkData {

    @Autowired(required = false)
    ArrayList<String> list;

    public CreateNetworkData() {

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list.add("Another string created only once when constructor was called " + i);
        }
        this.list = list;
    }

    public CreateNetworkData(Integer l) {

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < l; i++) {
            list.add("Another string created only once when constructor was called " + i);
        }
        this.list = list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return this.list;
    }

    @Override
    public String toString() {
        return "CreateNetworkData{" +
                "list=" + list +
                '}';
    }
}