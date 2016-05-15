package com.example.sandbox;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class BasicActivity extends AppCompatActivity {

    private String workingDirectory;
    private Map<Integer, SharingFile> filesList = new HashMap<>();

    // ИСПОЛЬБЗОВАТЬ ТОЛЬКО ОДИН
    private SocketClient client = null;
    private SocketServer server = null;

    Button buttonOpenDialog;
    Button seeLocalFiles;
    Button seeGlobalFiles;
    Button buttonUp;
    TextView textFolder;
    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;
    File root;
    File curFolder;
    private List<String> fileList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        Integer port = intent.getIntExtra("port", 8787);

        TextView textViewIp = (TextView) findViewById(R.id.textViewIp);

        // IF SERVER
        if(!MainActivity.isClient){
            assert textViewIp != null;
            server = new SocketServer(8787);
            textViewIp.setText("Your IP : " + getIpAddress());
        } else { // IF CLIENT
            assert textViewIp != null;
            textViewIp.setText("");
            Log.i("Client", "Try connect");
            client = new SocketClient(ip, 8787, filesList);
        }

        buttonOpenDialog = (Button) findViewById(R.id.opendialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

        seeLocalFiles = (Button)findViewById(R.id.seeLocalFiles);
        seeLocalFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateListView();
            }
        });

        seeGlobalFiles = (Button)findViewById(R.id.seeGlobalFiles);
        seeGlobalFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client != null) {
                    client.askSyncOfAllFilesRequest();
                } else {
//                    Возможно, локальные устройства сами отсылают.
//                    server.askSyncOfAllFilesRequest();
                }
                populateGlobalListView();
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;

//        Thread thread = new Thread(MainActivity.client);
//        MainActivity.client.pull = commonPull;
//        MainActivity.client.filesList = filesList;
//        thread.start();
    }

    public void populateListView(){
        ArrayList<String> filesNames = new ArrayList<>();
        for (int i = 1; i <= filesList.size(); i++) {
            filesNames.add(filesList.get(i).getFileLocation());
        }
        String strs[] = new String[filesNames.size()];
        strs = filesNames.toArray(strs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.filelists1, strs);
        ListView list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    public void populateGlobalListView(){
        final ArrayList<String> filesNames = new ArrayList<>();
        for (int i = 1; i <= filesList.size(); i++) {
            if(!filesList.get(i).isLocal())
                filesNames.add(filesList.get(i).getFileLocation());
        }
        filesNames.add("remoteFile");// FOR TEST

        String strs[] = new String[filesNames.size()];
        strs = filesNames.toArray(strs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.globalfiles, strs);


        final ListView list = (ListView)findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chosenConnection = (String) list.getItemAtPosition(position);
                for (Map.Entry<Integer, SharingFile> entry : filesList.entrySet()) {
                    if (entry.getValue().equals(chosenConnection)) {
                        System.out.println(entry.getKey() + "/" + entry.getValue());
//                        commonPull.add(new Task(entry.getKey(), request.DOWNLOAD_FILE));
                        if (client != null) {
                            client.downloadFileRequest(entry.getKey());
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(BasicActivity.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);
                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if(selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            SharingFile.idCounter++;
                            SharingFile currentFile = new SharingFile(SharingFile.idCounter, selected.getAbsolutePath(), true);
                            filesList.put(SharingFile.idCounter, currentFile);
//                            commonPull.add(new Task(currentFile.getFileLocation(), request.FILE_ADDED_TO_LOCAL_FOLDER));
                            if (client != null) {
                                client.fileAddedToLocalRequest(currentFile.getFileLocation());
                            }
                            Toast.makeText(BasicActivity.this, selected.toString() + " selected",
                                    Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);
                        }
                    }
                });

                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if(f.equals(root)) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }



    private List<File> getListFiles(File parentDir) {
        String str = "";
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            SharingFile.idCounter++;
            SharingFile currentFile = new SharingFile(SharingFile.idCounter, file.getAbsolutePath(), true);
        }
        return inFiles;
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
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
}
