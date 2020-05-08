package com.example.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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



    private ArrayList<RoomData> exampleList;
    private Button createRoom;
    private String user;
    private String multi;
    JSONObject rooms;
    List<String> roomsList;
    HashMap<String, ArrayList<String>> nameIdMap;
    private RecyclerView mRecyclerView;
    private RoomAdapterActivity mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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

        Call<ResponseBody> mService = service.get_rooms(user);
        mService.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                assert response.body() != null;
                res = null;
                rooms = null;

                try {
                    res = response.body().string();
                    Log.v("ROOM",res);
                    rooms = new JSONObject(res);
                    nameIdMap = new HashMap<String, ArrayList<String>>();
                    exampleList = new ArrayList<RoomData>();
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showList() throws JSONException {
        noRooms = Integer.parseInt(rooms.getString("no_rooms"));
        if (noRooms > 0) {
            for (int i = 0; i < noRooms; i++) {
                JSONObject nameRoom = rooms.getJSONObject(String.valueOf(i));
                if (!exampleList.stream().anyMatch(o -> o.getText2().equals(nameRoom))) {
                    String name = nameRoom.getString("creator");
                    String id = nameRoom.getString("id");
                    String domain = nameRoom.getString("domain");
                    ArrayList<String> list = new ArrayList<>();
                    list.add(id);
                    list.add(domain);
                    exampleList.add(new RoomData(R.drawable.room_multi,"For Anyone", name));
                    Log.v("ROOM",exampleList.get(0).getText2());
                    nameIdMap.put(name, list);
                }

            }
        }



        mRecyclerView = findViewById(R.id.rooms);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RoomAdapterActivity(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        createRoom = findViewById(R.id.createRoom);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RoomAdapterActivity.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String entryNameRoom = exampleList.get(position).getText2();
                ArrayList<String> list = new ArrayList<>();
                list = nameIdMap.get(entryNameRoom);

                Log.v("TAGULL", "AICI " + list.get(0));

                String myid = list.get(0);
                String domain = list.get(1);
                if (!user.equals(entryNameRoom)) {
                    JSONObject sendJson = new JSONObject();

                    try {
                        sendJson.put("username", user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sendJson.put("id", myid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ResponseBody> mService = service.chooseRoom(sendJson);
                    mService.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String enter = response.body().string();
                                Log.v("ATGUL",enter);
                                if(!enter.contains("Done")) {
                                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                    Log.v("TAGUL", myid);
                                    intent.putExtra("USERNAME", user);
                                    intent.putExtra("GAMESID", myid);
                                    intent.putExtra("TOPIC", "HISTORY");
                                    intent.putExtra("MULTI", multi);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Room already taken", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.v("TAGUL", t.getMessage());

                        }
                    });
                }



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
        }, 100);



    }

}
