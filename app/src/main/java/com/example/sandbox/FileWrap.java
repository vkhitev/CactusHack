package com.example.sandbox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.Serializable;

public class FileWrap implements Serializable {
    public byte[] data;
    public Integer size;
    public String name;

    FileWrap(String path) {
        try {
            data = fileToByte(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        size = data.length;
        name = new File(path).getName();
    }

    public File byteToFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(name);
        fos.write(data);
        fos.close();
        return new File(name);
    }

    public static byte[] fileToByte(String file) throws IOException {
        return fileToByte(new File(file));
    }

    public static byte[] fileToByte(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
}
