package com.example.smartlms;

public class DataModel {

    Integer req_id;
    String uname;

    public DataModel(String name, Integer id) {
        this.uname=name;
        this.req_id=id;


    }

    public String getName() {
        return uname;
    }

    public Integer getId() {
        return req_id;
    }

}