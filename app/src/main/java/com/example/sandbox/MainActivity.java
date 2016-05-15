package com.example.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

    public static Client client = new Client();
    ArrayAdapter<String> adapter;
    ListView listView;

    String chosenConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File directory = new File(Environment.getExternalStorageDirectory().toString() + "/SandBox");

        if(!directory.exists()) {
            if(directory.mkdir()); //directory is created;
        }

//        List<File> files = client.getListFiles(directory);
        ConnectionWork connectionWork = new ConnectionWork();
        List<String> listOfNames = connectionWork.getAviableConnections();
        String[] writeLocalFiles = new String[listOfNames.size()];
        writeLocalFiles = listOfNames.toArray(writeLocalFiles);

        adapter = new ArrayAdapter<>(
                this,
                R.layout.da_item,
                writeLocalFiles);

        listView = (ListView) findViewById(R.id.listViewMain);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenConnection = (String) listView.getItemAtPosition(position);
                Intent i = new Intent(view.getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

}