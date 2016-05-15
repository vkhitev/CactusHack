package com.example.sandbox;

import android.graphics.Path;

import java.io.File;

/**
 * Created by glebl_000 on 5/14/2016.
 */
enum request{SEND, GET, SEND_INFO, GET_INFO};

public class Task {
    private int localId;
    private int globalId;
    private String name;
    private String filePath;
    private String macAddress;
    private SharingFile file;
    public request requestType;
    private File fileToSend;


//    public Task(int globalId, String name, String macAddress, SharingFile file, request requestType){
//        this.globalId = globalId;
//        this.name = name;
//        this.macAddress = macAddress;
//        this.file = file;
//        this.requestType = requestType;
//    }

    //send file
    public Task(File fileToSend, request request){
        this.fileToSend = fileToSend;
        this.requestType = request;
    }

    //send info about file
    public Task(String filePath, request request){
        this.filePath = filePath;
        this.requestType = request;
    }

    //get files
    public Task(int globalId, request request){
        this.globalId = globalId;
        this.requestType = request;
    }

    //get info about files
    public Task(request request){
        this.requestType = request;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getGlobalId() {
        return globalId;
    }

    public void setGlobalId(int globalId) {
        this.globalId = globalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public SharingFile getFile() {
        return file;
    }

    public void setFile(SharingFile file) {
        this.file = file;
    }



}
