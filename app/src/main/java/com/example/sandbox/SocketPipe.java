package com.example.sandbox;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

class SocketPipe {
    private Integer PORT;
    private String IP;
    private ArrayList<globalFileInfo> globalFilesInfo = new ArrayList<globalFileInfo>();
    private ArrayList<UserInfo> allUsers = new ArrayList<UserInfo>();
    private ServerSocket SERVER_SOCKET;
    private Integer AI = 0;
    private Integer AI_F = 0;

    Thread listenerOfNewRequest;

    SocketPipe() {
        createServer(8787);
    }

    private Boolean createServer(Integer _Port) {
        PORT = _Port;
        try {
            SERVER_SOCKET = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Log.i("IP", getIpAddress());

        Thread listenerOfNewUsers = new Thread(new listenNewUsers());
        listenerOfNewUsers.start();

        listenerOfNewRequest = new Thread(new listenNewRequest());

        return true;
    }

    private class listenNewUsers extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = SERVER_SOCKET.accept();
                    Log.i("USER", "Catch new socket!");
                    addNewUser(AI, socket);
                    Log.i("USER", "Added");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    private class listenNewRequest extends Thread {


        @Override
        public void run() {
            //localFileInfo obj = null;


            //while (true) {
                //for (Integer i = 0; i < allUsers.size(); ++i) {
                    Log.i("IN:", "" + 0);
                    try {
                        //getUserById(i).IN.close();
                        //Log.i("FUCK", getUserById(0).IN.readObject().toString());
                        localFileInfo obj = (localFileInfo) getUserById(0).IN.readObject();
                        Log.i("EDDDDD", obj.toString());
                        //Log.i("IN:", obj.Title);
                       /* if (obj.getClass() == localFileInfo.class) {
                            localFileInfo Obj = (localFileInfo) obj;
                        }*/
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
               // }
            //}
        }
    }

    private void insertNewFile(localFileInfo FileInfo, Integer Owner) {
        globalFilesInfo.add(new globalFileInfo(AI_F, FileInfo.Title, Owner, 100, "afasc"));
        Log.i("FILES", "New file added");
    }


    private UserInfo getUserById(Integer _Id) {
        return allUsers.get(_Id);
    }

    private void addNewUser(Integer _Id, Socket _Soc) {
        Log.i("USERS", "New user " + AI);
        allUsers.add(new UserInfo(_Id, _Soc));
        listenerOfNewRequest.start();
        ++AI;
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

    private class UserInfo {
        private Socket SOCKET;
        private Integer ID;
        public ObjectInputStream IN;
        public ObjectOutputStream OUT;

        UserInfo(Integer _Id, Socket _Socket) {
            ID = _Id;
            SOCKET = _Socket;
            try {
                Log.i("UserInfo", "1");
                OUT = new ObjectOutputStream(_Socket.getOutputStream());
                Log.i("UserInfo", "2");
                IN = new ObjectInputStream(_Socket.getInputStream());
                Log.i("UserInfo", "3");

                //OUT.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }



    private String getIpAddress() {
            String ip = "";
            try {
                Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (enumNetworkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = enumNetworkInterfaces
                            .nextElement();
                    Enumeration<InetAddress> enumInetAddress = networkInterface
                            .getInetAddresses();
                    while (enumInetAddress.hasMoreElements()) {
                        InetAddress inetAddress = enumInetAddress.nextElement();

                        if (inetAddress.isSiteLocalAddress()) {
                            ip += "SiteLocalAddress: "
                                    + inetAddress.getHostAddress() + "\n";
                        }

                    }

                }

            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ip += "Something Wrong! " + e.toString() + "\n";
            }

            return ip;
        }


    class localFileInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        public String Title;
        public String Path;
        public Integer Size;
        localFileInfo(String _Title, String _Path, Integer _Size) {
            Title = _Title;
            Path = _Path;
            Size = _Size;
        }
    }
}

