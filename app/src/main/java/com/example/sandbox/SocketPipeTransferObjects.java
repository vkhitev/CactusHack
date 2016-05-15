package com.example.sandbox;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


public class SocketPipeTransferObjects {

    static class Action implements  Serializable {
        /**
         * 1 - Syncronize globalFilesInfo
         * 2 - Server -> Client "Give me file with ID PARAM"
         * 3 - Client -> Server "Give me file with ID PARAM"
         */
        public Integer ACT;
        public Integer PARAM;

        Action(Integer _Act, Integer _Param) {
            ACT = _Act;
            PARAM = _Param;
        }
    }

    static class Pass implements Serializable {
        public String Password;
        Pass(String _Pass) {
            Password = _Pass;
        }
    }

    static class localFileInfo implements Serializable {
        public String Title;
        public String Path;
        public Integer Size;
        localFileInfo(String _Title, String _Path, Integer _Size) {
            Title = _Title;
            Path = _Path;
            Size = _Size;
        }
    }

    static class globalFileInfo implements Serializable {
        public Integer Id;
        public String Title;
        public String Path;
        public Integer Size;
        public Integer Owner;

        globalFileInfo(Integer _Id, String _Title, String _Path, Integer _Owner, Integer _Size) {
            Id = _Id;
            Title = _Title;
            Path = _Path;
            Size = _Size;
            Owner = _Owner;
        }
    }

    static class actionWithFile implements Serializable {
        public Integer Id;
        public Integer Action;

        actionWithFile(Integer _Id, Integer _Action) {
            Id = _Id;
            Action = _Action;
        }
    }

    static class UserInfo {
        private Socket SOCKET;
        private Integer ID;
        public ObjectInputStream IN;
        public ObjectOutputStream OUT;

        public Boolean isOnline = true;

        UserInfo(Integer _Id, Socket _Socket) {
            ID = _Id;
            SOCKET = _Socket;
            try {
                OUT = new ObjectOutputStream(_Socket.getOutputStream());
                IN = new ObjectInputStream(_Socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
