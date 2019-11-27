package com.example.smartlms;

import android.app.Application;

public class Configs extends Application
{
    private String SERVER_IP = "http://192.168.43.193:8000/";

    private String StaffID = "H4S";
    private String StaffPass = "h4s";

    public String getPrivData() {
        return SERVER_IP;
    }

    public String getStaffID() {
        return StaffID;
    }

    public String getStaffPass() {
        return StaffPass;
    }
}