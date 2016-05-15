package com.example.sandbox;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class SocketClient {
    private String ADDRESS;
    private Integer PORT;
    private Socket SOCKET;
    private ObjectInputStream IN;
    private ObjectOutputStream OUT;


    private Map<Integer, SharingFile> filesList;

    public SocketClient(String _Address, Integer _Port, Map<Integer, SharingFile> filesList) {
        Log.i("Client", "Connect");
        this.filesList = filesList;
        ADDRESS = _Address;
        PORT = _Port;
        try {
            SOCKET = new Socket(ADDRESS, PORT);
            OUT = new ObjectOutputStream(SOCKET.getOutputStream());
            IN = new ObjectInputStream(SOCKET.getInputStream());

            new Thread(new listenRequests()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class listenRequests extends Thread {
        @Override
        public void run() {
            Object obj = new Object();
            while (true) {
                try {
                    obj = IN.readObject();
                    if (obj.getClass() == SocketPipeTransferObjects.globalFileInfo.class) { // Обновить глобал
                        SocketPipeTransferObjects.globalFileInfo globalFileInfo =
                                (SocketPipeTransferObjects.globalFileInfo) obj;
                        addGlobalFileInfo(globalFileInfo);

                    } else if (obj.getClass() == SocketPipeTransferObjects.localFileInfo.class){ // Обновить локал

                        Log.d("Client listens", "File is shared");

                    } else if (obj.getClass() == SocketPipeTransferObjects.actionWithFile.class) { // Скачать файл
                        FileWrap file = (FileWrap) obj;
                        file.byteToFile();
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void askSyncOfAllFilesRequest(){
        sendObjectToServer(new SocketPipeTransferObjects.Action(1, 0));
    }

    public void fileAddedToLocalRequest(String _Path) {
        File file = new File(_Path);
        Integer Size = 1;
        SocketPipeTransferObjects.localFileInfo locFile = new SocketPipeTransferObjects.localFileInfo(file.getName(), _Path, Size);
        sendObjectToServer(locFile);
    }

    public void downloadFileRequest(Integer key) {
        sendObjectToServer(new SocketPipeTransferObjects.Action(3, key));
    }

    private void addGlobalFileInfo(SocketPipeTransferObjects.globalFileInfo _globalFileInfo) {
        filesList.put(_globalFileInfo.Id,
                new SharingFile(_globalFileInfo.Id, _globalFileInfo.Path, false));
    }



    private Boolean sendObjectToServer(Object obj) {
        try {
            OUT.writeObject(obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void syncGlobalFilesInfo() {

    }

    /**Send*/

}
