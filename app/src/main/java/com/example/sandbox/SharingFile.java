package com.example.sandbox;

import android.graphics.Path;

/**
 * Created by glebl_000 on 5/14/2016.
 */
public class SharingFile {
    private static int idCounter = 0;
    private int id;
    private static String fileLocation;

    public String getFileLocation() {
        return fileLocation;
    }

    public SharingFile(int idCounter, int id, String fileLocation){
        SharingFile.idCounter = idCounter;
        this.id = id;
        SharingFile.fileLocation = fileLocation;
    }
}
