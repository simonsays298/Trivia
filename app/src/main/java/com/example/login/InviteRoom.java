package com.example.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class InviteRoom extends AppCompatActivity {


    private String user;
    private String multi;
    private Button declineAll;
    JSONObject rooms;
    HashMap<String, ArrayList<String>> nameIdMap;
    private RecyclerView mRecyclerView;
    private RoomAdapterActivity mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int noRooms;

    String res;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://firsttry-272817.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserService service = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_room);

        this.setTitle(getString(R.string.your_invites));

        user = getIntent().getStringExtra("USERNAME");
        multi = "1";

        declineAll = findViewById(R.id.buttonDecline);

        declineAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject sendUser = new JSONObject();
                try {
                    sendUser.put("username",user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<ResponseBody> mService = service.decline_all(sendUser);
                mService.enqueue(new Callback<ResponseBody>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        assert response.body() != null;
                        res = null;
                        try {
                            res = response.body().string();
                            Toast.makeText(InviteRoom.this, res, Toast.LENGTH_SHORT).show();
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
        });

        try {
            loadRooms();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void loadRooms() throws JSONException {

        Call<ResponseBody> mService = service.get_invites(user);
        mService.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                assert response.body() != null;
                res = null;
                rooms = null;

                try {
                    res = response.body().string();
//                    Log.v("ROOM",res);
                    rooms = new JSONObject(res);
                    nameIdMap = new HashMap<String, ArrayList<String>>();
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
        ArrayList<RoomData> exampleList = new ArrayList<RoomData>();
        noRooms = Integer.parseInt(rooms.getString("no_rooms"));
//        Log.v("INVITESSSS",rooms.getString("no_rooms"));
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
                    exampleList.add(new RoomData(R.drawable.room_multi,"For " + user,  name));
//                    Log.v("ROOM",exampleList.get(0).getText2());
                    nameIdMap.put(name, list);
                }
            }

        }
        mRecyclerView = findViewById(R.id.invitesView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RoomAdapterActivity(exampleList, user, nameIdMap, multi, getApplicationContext(),"invited");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


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
        }, 500);

    }
}
