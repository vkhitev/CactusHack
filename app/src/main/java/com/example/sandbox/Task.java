package com.example.sandbox;

import java.io.File;

/**
 * Created by glebl_000 on 5/14/2016.
 */
public class Task {
    private int localId;
    private int globalId;
    private String name;
    private String macAddress;
    private SharingFile file;

    public Task(int globalId, String name, String macAddress, SharingFile file){
        this.globalId = globalId;
        this.name = name;
        this.macAddress = macAddress;
        this.file = file;
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

    private enum request{SEND, GET};

}
