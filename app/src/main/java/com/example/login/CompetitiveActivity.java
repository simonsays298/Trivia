package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompetitiveActivity extends AppCompatActivity {


    private ListView room;
    private Button createRoom;
    private String user;
    private String multi;
    JSONObject rooms;
    List<String> roomsList = new ArrayList<>();
    private int noRooms;
    private int counter = 0;

    String res;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitive);

        user = getIntent().getStringExtra("USERNAME");
        multi = getIntent().getStringExtra("MULTI");

        room = findViewById(R.id.listView);
        createRoom = findViewById(R.id.createRoom);

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom.setText("Creating Room");
                createRoom.setEnabled(false);

                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("USERNAME", user);
                intent.putExtra("MULTI", multi);
                startActivity(intent);

            }
        });

        try {
            loadRooms();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void loadRooms() throws JSONException {

        Call<ResponseBody> mService = service.get_rooms();
        mService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                res = null;
                rooms = null;

                try {
                    res = response.body().string();
                    rooms = new JSONObject(res);
                    showList();



                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("TAGUL", t.getMessage());

            }
        });



    }
    public void showList() throws JSONException {
        noRooms = Integer.parseInt(rooms.getString("no_rooms"));
        if(noRooms > 0){
            for(int i = 0 ; i < noRooms ; i++){
                JSONObject nameRoom = rooms.getJSONObject(String.valueOf(i));
                if(!roomsList.contains(nameRoom.getString("creator"))){
                    roomsList.add(nameRoom.getString("creator"));
                }

            }
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomsList);
        room.setAdapter(itemsAdapter);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    loadRooms();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },100);
    }
}
