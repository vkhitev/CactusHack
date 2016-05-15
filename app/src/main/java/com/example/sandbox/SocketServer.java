package com.example.sandbox;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    private Integer PORT;
    private String IP;
    private ArrayList<SocketPipeTransferObjects.globalFileInfo> globalFilesInfo = new ArrayList<SocketPipeTransferObjects.globalFileInfo>();
    private ArrayList<SocketPipeTransferObjects.UserInfo> allUsers = new ArrayList<SocketPipeTransferObjects.UserInfo>();
    private ServerSocket SERVER_SOCKET;
    private Integer AI = 0;
    private Integer AI_F = 0;

    public SocketServer(Integer _Port) {
        PORT = _Port;

        try {
            SERVER_SOCKET = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }



        Thread listenerOfNewUsers = new Thread(new listenNewUsers());
        listenerOfNewUsers.start();

//        Thread listenerOfNewRequest = new Thread(new listenNewRequest());
//        listenerOfNewRequest.start();
    }

    private class listenNewUsers extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = SERVER_SOCKET.accept();
                    addNewUser(AI, socket);
                    Log.i("USER", "New user");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class listenNewRequest extends Thread {
        @Override
        public void run() {
            Object obj = new Object();
            Log.i("SERVER", "Start listen");
            while (true) {
                for (Integer i = 0; i < allUsers.size(); ++i) {
                    try {
                        obj = getUserById(i).IN.readObject();

                        Log.d("Read", "");

                        if (obj.getClass() == SocketPipeTransferObjects.Action.class) {
                            int type = ((SocketPipeTransferObjects.Action) obj).ACT;
                            int param = ((SocketPipeTransferObjects.Action) obj).PARAM;
                            if(type == 1) {
                                for (int j = 0; j < globalFilesInfo.size(); j++) {
                                    sendObjectToUser(i, globalFilesInfo.get(j));
                                }
                            } else if (type == 3) {
                                sendObjectToUser(i, new SocketPipeTransferObjects.Action(3, 0));
                            }

                        } else if (obj.getClass() == SocketPipeTransferObjects.localFileInfo.class) {
                            insertNewFile((SocketPipeTransferObjects.localFileInfo) obj, i);
                        }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void insertNewFile(SocketPipeTransferObjects.localFileInfo FileInfo, Integer Owner) {
        globalFilesInfo.add(new SocketPipeTransferObjects.globalFileInfo(AI_F, FileInfo.Title, FileInfo.Path, Owner, 100));
        Log.i("FILES", "New file added");
    }
    private void shareNewFile(Integer _IdOfFile) {
        for (int i = 0; i < allUsers.size(); i++) {
            if(allUsers.get(i).isOnline) {
                sendObjectToUser(i, globalFilesInfo.get(_IdOfFile));
            }
        }
    }


    private Boolean sendObjectToUser(Integer _Id, Object obj) {
        try {
            getUserById(_Id).OUT.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void addNewUser(Integer _Id, Socket _Soc) {
        Log.i("USERS", "New user #" + AI);
        allUsers.add(new SocketPipeTransferObjects.UserInfo(_Id, _Soc));
        ++AI;
    }
    private void deleteUser(Integer _Id) {
        Log.i("USERS", "Delete user #" + _Id);
        allUsers.get(_Id).isOnline = false;
    }
    private SocketPipeTransferObjects.UserInfo getUserById(Integer _Id) {
        return allUsers.get(_Id);
    }

    private class globalFileInfo {
        private Integer ID;
        private String Title;
        private Integer Owner;
        private Integer Size;
        private String Path;

        globalFileInfo(Integer _ID, String _Title, Integer _Owner, Integer _Size, String _Path) {
            ID = _ID;
            Title = _Title;
            Owner = _Owner;
            Size = _Size;
            Path = _Path;
        }

        public Integer getID() {
            return ID;
        }

        public String getTitle() {
            return Title;
        }

        public Integer getOwner() {
            return Owner;
        }

        public Integer getSize() {
            return Size;
        }

        public String getPath() {
            return Path;
        }
    }
}
