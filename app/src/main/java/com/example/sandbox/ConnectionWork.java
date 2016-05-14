package com.example.sandbox;

import java.util.ArrayList;

/**
 * Created by nadman on 15.05.16.
 */
public class ConnectionWork {

    private ArrayList<String> aviableConnections = new ArrayList<>();

    public ArrayList<String> getAviableConnections() {
        aviableConnections.add("connection1");
        aviableConnections.add("connection2");
        aviableConnections.add("connection3");
        aviableConnections.add("connection4");
        return aviableConnections;
    }
}
