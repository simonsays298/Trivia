package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashMap;
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
    HashMap<String, ArrayList<String>> nameIdMap= new HashMap<String, ArrayList<String>>();
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
        createRoom.setEnabled(true);

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
                    String name = nameRoom.getString("creator");
                    String id = nameRoom.getString("id");
                    String domain = nameRoom.getString("domain");
                    ArrayList<String> list = new ArrayList<>();
                    list.add(id);
                    list.add(domain);
                    roomsList.add(nameRoom.getString("creator"));
                    nameIdMap.put(name, list);
                }

            }
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomsList);
        room.setAdapter(itemsAdapter);

        room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String entryNameRoom= (String) parent.getAdapter().getItem(position);
                ArrayList<String> list = new ArrayList<>();
                list = nameIdMap.get(entryNameRoom);

                Log.v("TAGULL","AICI " + list.get(0));

                String myid = list.get(0);
                String domain = list.get(1);
                if(!user.equals(entryNameRoom)){
                    JSONObject sendJson = new JSONObject();

                    try {
                        sendJson.put("username", user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendJson.put("id",myid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ResponseBody> mService = service.chooseRoom(sendJson);
                    mService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            Log.v("TAGUL",myid);
                            intent.putExtra("USERNAME", user);
                            intent.putExtra("GAMESID", myid);
                            intent.putExtra("TOPIC","HISTORY");
                            //intent.putExtra("TOPIC", domain.toUpperCase());
                            startActivity(intent);
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.v("TAGUL", t.getMessage());

                        }
                    });
                }


//                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                String getId= pref.getString("ID", null);

            }
        });

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
