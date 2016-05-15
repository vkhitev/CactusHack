package com.example.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

//    public static Client client = new Client();
    public static Boolean isClient;
    ArrayAdapter<String> adapter;
    ListView listView;

    String chosenConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonConnectRoom = (Button) findViewById(R.id.buttonConnectRoom);
        Button buttonCreateRoom = (Button) findViewById(R.id.buttonCreateRoom);

        buttonConnectRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                isClient = true;
                startActivity(i);
            }
        });

        buttonCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BasicActivity.class);
                isClient = false;
                startActivity(i);
            }
        });

    }
}