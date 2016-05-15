package com.example.sandbox;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nadman on 14.05.16.
 */
//add 2 threads to read information about file and file
public class Client implements Runnable {
    private String name = "bohdan";
    private String macAddress = "indBohdan";
    private String IP;
    private String PORT;
    private Map<Integer, SharingFile> localFiles;

    public LinkedBlockingQueue<Task> pull;
    public Map<Integer, SharingFile> filesList;



    @Override
    public void run() {
        while (true) {
            try {
                Task currentTask = pull.take();
                switch (currentTask.requestType) {
                    case GET:
                        Task task = new Task(currentTask.getGlobalId(), currentTask.getName(), currentTask.getMacAddress(),
                                filesList.get(currentTask.getGlobalId()), request.SEND);
                        //send to socetbrea;
                        break;
                    case SEND:
                        filesList.put(currentTask.getGlobalId(), currentTask.getFile());
                        //todo
                        break;
                    case SEND_INFO:
                        break;
                    case GET_INFO:
                        break;


                }

            } catch (InterruptedException e) {
                System.err.println("problems with task");
            }
        }
    }

    private Map<Integer, SharingFile> remoteFiles;

    public String[] writeLocalFiles() {
        List<String> returnString = new ArrayList<>();
        for (int i = 0; i < localFiles.size(); i++) {
            returnString.add(localFiles.get(i).getFileLocation());
        }
        return (String[]) returnString.toArray();
    }

    //searching in local android repository
    public void getLocalFilesInfo() {
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        for (File aFile : file) {
            localFiles.put(0, new SharingFile(0, "", true));
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
    public void getRemoteFilesInfo() {
        sendTask(new Task(0, name, macAddress, null, request.GET));
        localFiles.put(0, waitingForFileInfo());
    }

    public void getRemoteFile(Integer remoteFileId) {
        sendTask(new Task(remoteFileId, name, macAddress, null, request.GET));
        localFiles.put(0, waitingForFile());
    }

    public void getRemoteFiles() {
        for (int remoteFileId = 0; remoteFileId < remoteFiles.size(); remoteFileId++) {
            sendTask(new Task(remoteFileId, name, macAddress, null, request.GET));
            localFiles.put(0, waitingForFile());
        }
    }

    //TODO: create thread to read information about files
    public SharingFile waitingForFileInfo() {
//        return new SharingFile();
        return null;
    }

    //TODO: create thread to download file
    public SharingFile waitingForFile() {
//        return new SharingFile();
        return null;
    }

    public void sendInfoAboutLocalFiles() {
        for (int i = 0; i < localFiles.size(); i++)
            sendTask(new Task(0, name, macAddress, localFiles.get(i), request.SEND));
    }

    public void sendInfoAboutLocalFile(Integer idFileToSend) {
//        sendTask(new Task(0, name, macAddress, localFiles.get(idFileToSend), Task.request.SEND));
        sendTask(new Task(0, name, macAddress, localFiles.get(idFileToSend), request.SEND_INFO));
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void sendTask(Task send) {
        //Khityov magic send query
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
