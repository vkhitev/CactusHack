package com.example.sandbox;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        Client client = new Client();
//        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/SandBox");
//
//        if(!directory.exists()) {
//            if(directory.mkdir()); //directory is created;
//        }
//
//        List<File> files = client.getListFiles(directory);
//        String[] writeLocalFiles = new String[files.size()];
//        List<String> listOfNames = new ArrayList<>();
//        for (int i = 0; i < files.size(); i++) {
//            listOfNames.add(files.get(i).getAbsolutePath());
//        }
//        writeLocalFiles = listOfNames.toArray(writeLocalFiles);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                R.layout.da_item,
//                writeLocalFiles);
//
//        ListView listView = (ListView) findViewById(R.id.listViewMain);
//        listView.setAdapter(adapter);

    }

}
