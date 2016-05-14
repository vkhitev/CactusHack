package com.example.sandbox;

import android.graphics.Path;

import java.io.File;

/**
 * Created by glebl_000 on 5/14/2016.
 */
public class SharingFile {
    public static int idCounter = 0;
    private int id;
    private String fileLocation;
    private boolean isLocal;
    private File currentFile;

    public SharingFile(int id, String fileLocation, boolean isLocal) {
        this.id = id;
        this.fileLocation = fileLocation;
        this.isLocal = isLocal;
    }

    public String getFileLocation() {
        return fileLocation;
    }
}
