package com.example.sandbox;

import android.app.Dialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class BasicActivity extends AppCompatActivity {

    private String workingDirectory;
    private LinkedBlockingQueue<Task> commonPull = new LinkedBlockingQueue<>();;
    private Map<Integer, SharingFile> filesList = new HashMap<>();

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
        //List<File> files = getListFiles(new File(Environment.getExternalStorageDirectory().toString() + "/1"));

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
                populateGlobalListView();
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;

        Thread thread = new Thread(MainActivity.client);
        MainActivity.client.pull = commonPull;
        MainActivity.client.filesList = filesList;
        thread.start();
        //thread.start();

        //EditText editText = (EditText)findViewById(R.id.editText1);


        //TextView textView = (TextView)findViewById(R.id.textView);
        //textView.setText(str);

//        //initialize global files list on all device
//        for(Map.Entry<Integer, User> entry : usersList.entrySet()) {
//            Integer key = entry.getKey();
//            usersList.get(key);
//            // do what you have to do here
//            // In your case, an other loop.
//        }
//
////        Thread thread = new Thread(new PullThread(commonPull, filesList, usersList));
////        thread.start();
//
//        Button btn = (Button)findViewById(R.id.button1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText editText = (EditText)findViewById(R.id.editText1);
//                TextView textView = (TextView)findViewById(R.id.textView);
//                textView.setText(Environment.getDataDirectory().toString());
//                List<File> files = getListFiles(new File("/1"));
//            }
//        });
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
        ArrayList<String> filesNames = new ArrayList<>();
        for (int i = 1; i <= filesList.size(); i++) {
            if(!filesList.get(i).isLocal())
                filesNames.add(filesList.get(i).getFileLocation());
        }
        filesNames.add("remoteFile");

        String strs[] = new String[filesNames.size()];
        strs = filesNames.toArray(strs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.globalfiles, strs);
        ListView list = (ListView)findViewById(R.id.listView1);
        list.setAdapter(adapter);
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
}
