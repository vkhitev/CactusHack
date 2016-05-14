package com.example.sandbox;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nadman on 14.05.16.
 */
//add 2 threads to read information about file and file
public class Client {
    private String name = "bohdan";
    private String macAddress = "indBohdan";
    private Map<Integer, SharingFile> localFiles;
    private Map<Integer, SharingFile> remoteFiles;

    public String[] writeLocalFiles(){
        List<String> returnString = new ArrayList<>();
        for (int i = 0; i < localFiles.size(); i++) {
            returnString.add(localFiles.get(i).getFileLocation());
        }
        return (String[]) returnString.toArray();
    }

    //searching in local android repository
    public void getLocalFilesInfo(){
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        for (File aFile : file) {
            localFiles.put(0, new SharingFile(0, 0, aFile.getAbsolutePath()));
        }
    }

    public List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    //query to server to getFiles
    public void getRemoteFilesInfo(){
        sendTask(new Task(0, name, macAddress, null, Task.request.GET));
        localFiles.put(0, waitingForFileInfo());
    }

    public void getRemoteFile(Integer remoteFileId){
        sendTask(new Task(remoteFileId, name, macAddress, null, Task.request.GET));
        localFiles.put(0, waitingForFile());
    }

    public void getRemoteFiles(){
        for (int remoteFileId = 0; remoteFileId < remoteFiles.size(); remoteFileId++) {
            sendTask(new Task(remoteFileId, name, macAddress, null, Task.request.GET));
            localFiles.put(0, waitingForFile());
        }
    }

    //TODO: create thread to read information about files
    public SharingFile waitingForFileInfo(){
//        return new SharingFile();
        return null;
    }

    //TODO: create thread to download file
    public SharingFile waitingForFile(){
//        return new SharingFile();
        return null;
    }

    public void sendInfoAboutLocalFiles(){
        for (int i = 0; i < localFiles.size(); i++)
            sendTask(new Task(0, name, macAddress, localFiles.get(i), Task.request.SEND));
    }

    public void sendInfoAboutLocalFile(Integer idFileToSend){
        sendTask(new Task(0, name, macAddress, localFiles.get(idFileToSend), Task.request.SEND));
    }


    //!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void sendTask(Task send){
        //Khityov magic send query
    }
}
