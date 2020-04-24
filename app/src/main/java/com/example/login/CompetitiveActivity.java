package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CompetitiveActivity extends AppCompatActivity {


    private ListView room;
    private Button createRoom;
    private String user;
    private String roomName;
    List<String> roomsList;
//    FirebaseDatabase databse = FirebaseDatabase.getInstance();
//    DatabaseReference roomsRef = databse.getReference()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive);

        user = getIntent().getStringExtra("USERNAME");
        room = findViewById(R.id.listView);
        createRoom = findViewById(R.id.createRoom);

        roomsList = new ArrayList<String>();

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom.setText("Creating Room");
                createRoom.setEnabled(false);
                //addRoomEventListener();
                roomName = user;

            }
        });

        room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomName = roomsList.get(position);
                //addRoomEventListener();
            }
        });
    }

//    private void addRoomEventListener(){
//
//
//    }
}
